package com.doupo.server.module.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResourceMirrorService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ResourceMirrorService.class);

    private static final int BUFFER_SIZE = 64 * 1024;

    private final Path root;
    private final URL upstream;
    private final boolean captureEnabled;
    private final Set<String> allowedHosts;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final int maxRedirects;
    private final ConcurrentMap<String, Object> downloadLocks =
            new ConcurrentHashMap<>();

    public ResourceMirrorService(
            @Value("${game.resources.root}") String root,
            @Value("${game.resources.upstream}") String upstream,
            @Value("${game.resources.capture-enabled:false}")
                    boolean captureEnabled,
            @Value("${game.resources.allowed-redirect-hosts}")
                    String allowedHosts,
            @Value("${game.resources.connect-timeout-ms:5000}")
                    int connectTimeoutMs,
            @Value("${game.resources.read-timeout-ms:30000}")
                    int readTimeoutMs,
            @Value("${game.resources.max-redirects:5}")
                    int maxRedirects) throws IOException {
        this.root = Paths.get(root).toAbsolutePath().normalize();
        this.upstream = new URL(trimTrailingSlash(upstream));
        this.captureEnabled = captureEnabled;
        this.allowedHosts = parseHosts(allowedHosts);
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
        this.maxRedirects = maxRedirects;

        validateRemoteUrl(this.upstream);
        Files.createDirectories(this.root);
    }

    public Path resolve(String requestPath) throws IOException {
        String relativePath = normalizeRequestPath(requestPath);
        Path destination = root.resolve(relativePath).normalize();

        if (!destination.startsWith(root)) {
            throw new IllegalArgumentException("Resource path escapes mirror root");
        }

        if (isCompleteFile(destination)) {
            return destination;
        }
        if (!captureEnabled) {
            throw new FileNotFoundException(relativePath);
        }

        Object lock = downloadLocks.computeIfAbsent(
                relativePath,
                ignored -> new Object());

        try {
            synchronized (lock) {
                if (!isCompleteFile(destination)) {
                    refresh(relativePath, destination);
                }
            }
        } finally {
            downloadLocks.remove(relativePath, lock);
        }

        return destination;
    }

    public boolean isVolatileResource(String requestPath) {
        return isVolatileRelativePath(normalizeRequestPath(requestPath));
    }

    private void refresh(
            String relativePath,
            Path destination) throws IOException {
        byte[] previousHash = null;
        boolean catalogHash = isCatalogHash(relativePath);
        if (catalogHash && isCompleteFile(destination)) {
            previousHash = Files.readAllBytes(destination);
        }

        try {
            download(relativePath, destination);
        } catch (IOException exception) {
            if (isCompleteFile(destination)) {
                LOGGER.warn(
                        "Keep cached resource after upstream refresh failed: path={}",
                        relativePath,
                        exception);
                return;
            }
            throw exception;
        }

        if (catalogHash
                && previousHash != null
                && isCompleteFile(destination)
                && !Arrays.equals(
                        previousHash,
                        Files.readAllBytes(destination))) {
            Path catalogBin = destination.resolveSibling("catalog_Ljxs.bin");
            Files.deleteIfExists(catalogBin);
            LOGGER.info(
                    "Catalog hash changed; dropped cached catalog.bin");
        }
    }

    private void download(
            String relativePath,
            Path destination) throws IOException {
        URL current = new URL(
                upstream.toExternalForm()
                        + "/"
                        + relativePath.replace('\\', '/'));
        validateRemoteUrl(current);

        HttpURLConnection connection = null;
        Path temporary = destination.resolveSibling(
                destination.getFileName()
                        + ".part-"
                        + UUID.randomUUID());

        try {
            for (int redirect = 0; ; redirect++) {
                connection = open(current);
                int status = connection.getResponseCode();

                if (isRedirect(status)) {
                    if (redirect >= maxRedirects) {
                        throw new IOException("Too many upstream redirects");
                    }

                    String location = connection.getHeaderField("Location");
                    if (location == null || location.trim().isEmpty()) {
                        throw new IOException("Redirect has no Location header");
                    }

                    URL redirected = new URL(current, location);
                    validateRemoteUrl(redirected);
                    connection.disconnect();
                    connection = null;
                    current = redirected;
                    continue;
                }

                if (status != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Upstream returned HTTP " + status);
                }

                Files.createDirectories(destination.getParent());
                copyResponse(connection, temporary);

                if (!isCompleteFile(temporary)) {
                    throw new IOException("Upstream returned an empty resource");
                }

                moveIntoPlace(temporary, destination);
                LOGGER.info(
                        "Resource cached: path={}, bytes={}",
                        relativePath,
                        Files.size(destination));
                return;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            Files.deleteIfExists(temporary);
        }
    }

    private HttpURLConnection open(URL url) throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        connection.setInstanceFollowRedirects(false);
        connection.setConnectTimeout(connectTimeoutMs);
        connection.setReadTimeout(readTimeoutMs);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty(
                "User-Agent",
                "dpcq-fixed-resource-mirror/1.0");
        connection.setRequestProperty("Accept-Encoding", "identity");
        return connection;
    }

    private void copyResponse(
            HttpURLConnection connection,
            Path temporary) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        try (InputStream input = connection.getInputStream();
                OutputStream output = Files.newOutputStream(temporary)) {
            int count;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
        }
    }

    private void moveIntoPlace(
            Path temporary,
            Path destination) throws IOException {
        try {
            Files.move(
                    temporary,
                    destination,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(
                    temporary,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void validateRemoteUrl(URL url) {
        String protocol = url.getProtocol();
        if (!("http".equalsIgnoreCase(protocol)
                || "https".equalsIgnoreCase(protocol))) {
            throw new IllegalArgumentException(
                    "Unsupported resource protocol");
        }
        if (url.getUserInfo() != null
                || !allowedHosts.contains(url.getHost().toLowerCase())) {
            throw new IllegalArgumentException(
                    "Resource host is not allowed: " + url.getHost());
        }
    }

    private String normalizeRequestPath(String requestPath) {
        if (requestPath == null || requestPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource path is empty");
        }

        String value = requestPath.replace('\\', '/');
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Resource path is empty");
        }

        Path normalized = Paths.get(value).normalize();
        if (normalized.isAbsolute()
                || normalized.startsWith("..")) {
            throw new IllegalArgumentException("Unsafe resource path");
        }
        return normalized.toString();
    }

    private boolean isCompleteFile(Path path) throws IOException {
        return Files.isRegularFile(path) && Files.size(path) > 0L;
    }

    private static boolean isVolatileRelativePath(String relativePath) {
        String path = unixPath(relativePath).toLowerCase(Locale.ROOT);
        return path.endsWith("/catalog/catalog_ljxs.hash")
                || path.endsWith("/patchsetting/patchsetting.json")
                || path.endsWith("/ver.json")
                || path.endsWith("/assemblies/hotupdateassemblyinfo.json");
    }

    private static boolean isCatalogHash(String relativePath) {
        return unixPath(relativePath)
                .toLowerCase(Locale.ROOT)
                .endsWith("/catalog/catalog_ljxs.hash");
    }

    private static String unixPath(String relativePath) {
        return relativePath.replace('\\', '/');
    }

    private boolean isRedirect(int status) {
        return status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_SEE_OTHER
                || status == 307
                || status == 308;
    }

    private static Set<String> parseHosts(String value) {
        Set<String> result = new HashSet<>();
        for (String host : value.split(",")) {
            String trimmed = host.trim().toLowerCase();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    private static String trimTrailingSlash(String value) {
        String result = value.trim();
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
