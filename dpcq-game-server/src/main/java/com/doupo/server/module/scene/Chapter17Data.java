package com.doupo.server.module.scene;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;

/** Checked-in, reproducible local capture/Luban exports; never fetch from the official server. */
final class Chapter17Data {
    static final JsonNode DATA = readConfig();

    static JsonNode row(String table, String key, int id) {
        for (JsonNode row : DATA.path(table)) {
            if (row.path(key).asInt() == id) return row;
        }
        throw new IllegalArgumentException("Missing verified " + table + ": " + id);
    }

    static ByteString bytes(String name) {
        try (InputStream in = Chapter17Data.class.getResourceAsStream("/chapter17/" + name)) {
            if (in == null) throw new IOException("Missing fixture " + name);
            return ByteString.readFrom(in);
        } catch (IOException e) { throw new IllegalStateException(e); }
    }

    private static JsonNode readConfig() {
        try {
            return new ObjectMapper().readTree(bytes("config.json").toByteArray());
        } catch (IOException e) { throw new IllegalStateException(e); }
    }
}
