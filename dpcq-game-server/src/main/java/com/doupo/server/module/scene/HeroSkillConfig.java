package com.doupo.server.module.scene;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/** 6.9.263 NewSkillBaseConfig 的基础行；不把升星行当成可学习的斗技。 */
final class HeroSkillConfig {
    private static final Map<Integer, JsonNode> BASES = load();

    static JsonNode get(int baseId) {
        return BASES.get(baseId);
    }

    static int quality(int baseId) {
        JsonNode row = get(baseId);
        return row == null ? 0 : row.path("quality").asInt();
    }

    private static Map<Integer, JsonNode> load() {
        try (InputStream in = HeroSkillConfig.class.getResourceAsStream("/skill-learning/base-config.json")) {
            if (in == null) throw new IOException("Missing verified skill learning config");
            Map<Integer, JsonNode> rows = new HashMap<>();
            for (JsonNode row : new ObjectMapper().readTree(in).path("skills")) {
                rows.put(row.path("id").asInt(), row);
            }
            return rows;
        } catch (IOException e) { throw new IllegalStateException(e); }
    }

    private HeroSkillConfig() { }
}
