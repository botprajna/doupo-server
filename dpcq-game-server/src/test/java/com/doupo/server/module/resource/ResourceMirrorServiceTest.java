package com.doupo.server.module.resource;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ResourceMirrorServiceTest {

    private static final byte[] CONTENT =
            "fixed-resource".getBytes(StandardCharsets.UTF_8);
    private static final byte[] PATCH_V1 =
            "{\"m_ResourceVersion\":1}".getBytes(StandardCharsets.UTF_8);
    private static final byte[] PATCH_V263 =
            "{\"m_ResourceVersion\":263}".getBytes(StandardCharsets.UTF_8);
    private static final byte[] HASH_V1 =
            "16a8ba2f736b26d6b3327d007c8c2e8e"
                    .getBytes(StandardCharsets.US_ASCII);
    private static final byte[] HASH_V263 =
            "8d6476b427d99fb180d19170bb55ba74"
                    .getBytes(StandardCharsets.US_ASCII);

    @Rule
    public TemporaryFolder temporaryFolder =
            new TemporaryFolder();

    private HttpServer server;
    private String upstream;
    private final AtomicReference<byte[]> patchSetting =
            new AtomicReference<byte[]>(PATCH_V1);
    private final AtomicReference<byte[]> catalogHash =
            new AtomicReference<byte[]>(HASH_V1);
    private final AtomicInteger patchHits = new AtomicInteger();

    @Before
    public void setUp() throws IOException {
        server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 0),
                0);
        server.createContext(
                "/source/imlj2_android_alpha1/Ljxs/test.bundle",
                exchange -> {
                    exchange.getResponseHeaders().add(
                            "Location",
                            "/actual/test.bundle");
                    exchange.sendResponseHeaders(302, -1);
                    exchange.close();
                });
        server.createContext(
                "/actual/test.bundle",
                exchange -> {
                    exchange.sendResponseHeaders(
                            200,
                            CONTENT.length);
                    exchange.getResponseBody().write(CONTENT);
                    exchange.close();
                });
        server.createContext(
                "/source/imlj2_android_alpha1/Ljxs/PatchSetting/patchSetting.json",
                exchange -> {
                    byte[] body = patchSetting.get();
                    patchHits.incrementAndGet();
                    exchange.sendResponseHeaders(200, body.length);
                    exchange.getResponseBody().write(body);
                    exchange.close();
                });
        server.createContext(
                "/source/imlj2_android_alpha1/Ljxs/Catalog/catalog_Ljxs.hash",
                exchange -> {
                    byte[] body = catalogHash.get();
                    exchange.sendResponseHeaders(200, body.length);
                    exchange.getResponseBody().write(body);
                    exchange.close();
                });
        server.start();
        upstream = "http://127.0.0.1:"
                + server.getAddress().getPort()
                + "/source";
    }

    @After
    public void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    public void downloadsRedirectOnceAndThenUsesCache()
            throws Exception {
        Path root = temporaryFolder.newFolder("mirror").toPath();
        ResourceMirrorService service = service(root, true);
        String path =
                "/imlj2_android_alpha1/Ljxs/test.bundle";

        Path downloaded = service.resolve(path);
        assertArrayEquals(CONTENT, Files.readAllBytes(downloaded));

        server.stop(0);
        server = null;
        assertEquals(downloaded, service.resolve(path));
    }

    @Test
    public void keepsPinnedPatchSettingEvenIfUpstreamChanges()
            throws Exception {
        Path root = temporaryFolder.newFolder("volatile").toPath();
        ResourceMirrorService service = service(root, true);
        String path =
                "/imlj2_android_alpha1/Ljxs/PatchSetting/patchSetting.json";

        assertArrayEquals(PATCH_V1, Files.readAllBytes(service.resolve(path)));
        patchSetting.set(PATCH_V263);
        assertArrayEquals(PATCH_V1, Files.readAllBytes(service.resolve(path)));
        assertEquals(1, patchHits.get());
    }

    @Test
    public void keepsCachedVolatileFileWhenUpstreamRefreshFails()
            throws Exception {
        Path root = temporaryFolder.newFolder("fallback").toPath();
        ResourceMirrorService service = service(root, true);
        String path =
                "/imlj2_android_alpha1/Ljxs/PatchSetting/patchSetting.json";

        Path cached = service.resolve(path);
        assertArrayEquals(PATCH_V1, Files.readAllBytes(cached));

        server.stop(0);
        server = null;
        assertArrayEquals(PATCH_V1, Files.readAllBytes(service.resolve(path)));
    }

    @Test
    public void pinnedCatalogHashKeepsCachedCatalogBin()
            throws Exception {
        Path root = temporaryFolder.newFolder("catalog").toPath();
        ResourceMirrorService service = service(root, true);
        String hashPath =
                "/imlj2_android_alpha1/Ljxs/Catalog/catalog_Ljxs.hash";
        Path catalogDir = root
                .resolve("imlj2_android_alpha1")
                .resolve("Ljxs")
                .resolve("Catalog");
        Files.createDirectories(catalogDir);
        Path catalogBin = catalogDir.resolve("catalog_Ljxs.bin");
        Files.write(catalogBin, "old-catalog".getBytes(StandardCharsets.UTF_8));

        assertArrayEquals(HASH_V1, Files.readAllBytes(service.resolve(hashPath)));
        assertTrue(Files.isRegularFile(catalogBin));

        catalogHash.set(HASH_V263);
        assertArrayEquals(HASH_V1, Files.readAllBytes(service.resolve(hashPath)));
        assertTrue(Files.isRegularFile(catalogBin));
        assertEquals(
                "old-catalog",
                new String(
                        Files.readAllBytes(catalogBin),
                        StandardCharsets.UTF_8));
    }

    @Test
    public void marksVersionIndexFilesAsVolatile() throws Exception {
        ResourceMirrorService service = service(
                temporaryFolder.newFolder("index").toPath(),
                true);

        assertTrue(service.isVolatileResource(
                "/imlj2_android_alpha1/Ljxs/PatchSetting/patchSetting.json"));
        assertTrue(service.isVolatileResource(
                "/imlj2_android_alpha1/Ljxs/Catalog/catalog_Ljxs.hash"));
        assertTrue(service.isVolatileResource(
                "/imlj2_android_alpha1/Ljxs/ver.json"));
        assertTrue(service.isVolatileResource(
                "/imlj2_android_alpha1/Ljxs/Assemblies/hotUpdateAssemblyInfo.json"));
        assertFalse(service.isVolatileResource(
                "/imlj2_android_alpha1/Ljxs/ServerData/ab/foo.bundle"));
    }

    @Test(expected = FileNotFoundException.class)
    public void frozenMirrorRejectsMissingResource()
            throws Exception {
        ResourceMirrorService service = service(
                temporaryFolder.newFolder("frozen").toPath(),
                false);
        service.resolve(
                "/imlj2_android_alpha1/Ljxs/missing.bundle");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsPathTraversal() throws Exception {
        ResourceMirrorService service = service(
                temporaryFolder.newFolder("safe").toPath(),
                true);
        service.resolve("/../../outside.bundle");
    }

    private ResourceMirrorService service(
            Path root,
            boolean captureEnabled) throws IOException {
        return new ResourceMirrorService(
                root.toString(),
                upstream,
                captureEnabled,
                "127.0.0.1",
                1000,
                1000,
                3);
    }
}
