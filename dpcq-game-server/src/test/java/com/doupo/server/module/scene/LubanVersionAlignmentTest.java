package com.doupo.server.module.scene;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class LubanVersionAlignmentTest {
    @Test
    public void runtimeUsesTheVerified69SnapshotNotAnOldDirectoryLabel() {
        assertEquals("6.9.263", Chapter17Data.DATA.path("configVersion").asText());
        assertEquals("58e6b889dfb4e787a74933e9b52ab0691dee8b283b42d64f69ea6169ca2fb754",
                Chapter17Data.DATA.path("provenance").path("configsDllSha256").asText());
        assertEquals(317782, Chapter17Data.row("chapters", "Id", 10300505).path("Power").asInt());
        assertEquals(324428, Chapter17Data.row("chapters", "Id", 10300705).path("Power").asInt());
        assertEquals(57396, Chapter17Data.row("tower", "Id", 1).path("Power").asInt());
        assertTrue(Chapter17Data.row("tasks", "TaskId", 200035).has("TaskFinish"));
        assertEquals(13, Chapter17Data.DATA.path("provenance").path("tables").size());
        assertEquals(5, Chapter17Data.row("mainEquips", "Id", 15).path("Type").asInt());
    }

    @Test
    public void existingRealmCalculationsMatch69LevelsAndStageBoundaries() {
        for (int level = 1; level <= 17; level++) {
            JsonNode row = Chapter17Data.row("levels", "Level", level);
            assertEquals(row.path("Exp").asLong(), PlayerRealmConfig.expToReach(level));
            if (level < 17) assertEquals(row.path("NeedUpStage").asInt() == 1, PlayerRealmConfig.needLevelBreak(level));
            PlayerRealmConfig.RealmStats stats = PlayerRealmConfig.statsOf(level);
            assertEquals(row.path("Stats").path("atk").asDouble(), stats.atk, 0.001);
            assertEquals(row.path("Stats").path("def").asDouble(), stats.def, 0.001);
            assertEquals(row.path("Stats").path("hp").asDouble(), stats.hp, 0.001);
            assertEquals(row.path("Stats").path("atkFixNum").asDouble(), stats.atkFix, 0.001);
            assertEquals(row.path("Stats").path("defFixNum").asDouble(), stats.defFix, 0.001);
            assertEquals(row.path("Stats").path("hpFixNum").asDouble(), stats.hpFix, 0.001);
        }
        for (int stage = 1; stage <= 3; stage++) {
            assertEquals(Chapter17Data.row("stages", "Id", stage).path("MaxLevel").asInt(),
                    PlayerRealmConfig.stageMaxLevel(stage));
        }
    }

    @Test
    public void version69ChangesDoNotInventEarlyReputationOrElixirRewards() {
        assertEquals(700, Chapter17Data.row("reputation", "Id", 1001).path("Consume").get(0).path("Amount").asInt());
        assertEquals(800, Chapter17Data.row("reputation", "Id", 1002).path("Consume").get(0).path("Amount").asInt());
        assertEquals(280, Chapter17Data.row("elixirAccumulate", "Key", 1102).path("Exp").asInt());
        assertEquals(4, Chapter17Data.row("elixirAccumulate", "Key", 1102).path("Quality").asInt());
        assertEquals(5, Chapter17Data.row("lotteryPools", "Id", 8001).path("Times").asInt());
        assertEquals(1, Chapter17Data.row("lotteryPools", "Id", 8007).path("LotteryTimes").size());
        assertEquals(10, Chapter17Data.row("lotteryPools", "Id", 8008).path("LotteryTimes").get(0).asInt());
    }
}
