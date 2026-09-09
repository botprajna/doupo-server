package com.doupo.server.module.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class ResourceMirrorController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ResourceMirrorController.class);

    private final ResourceMirrorService mirrorService;

    public ResourceMirrorController(
            ResourceMirrorService mirrorService) {
        this.mirrorService = mirrorService;
    }

    @GetMapping("/imlj2_android_alpha1/Ljxs/**")
    public ResponseEntity<Resource> resource(
            HttpServletRequest request) {
        String requestPath = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        try {
            Path file = mirrorService.resolve(requestPath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(Files.size(file));
            headers.setContentType(contentType(file));
            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.set(
                    HttpHeaders.CACHE_CONTROL,
                    mirrorService.isVolatileResource(requestPath)
                            ? "no-cache, no-store, must-revalidate"
                            : "public, max-age=31536000, immutable");

            return new ResponseEntity<>(
                    new FileSystemResource(file.toFile()),
                    headers,
                    HttpStatus.OK);
        } catch (FileNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IOException exception) {
            LOGGER.warn(
                    "Resource mirror request failed: path={}",
                    requestPath,
                    exception);
            return ResponseEntity.status(
                    HttpStatus.BAD_GATEWAY).build();
        }
    }

    private MediaType contentType(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".json")) {
            return MediaType.APPLICATION_JSON;
        }
        if (name.endsWith(".hash") || name.endsWith(".txt")) {
            return MediaType.TEXT_PLAIN;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
