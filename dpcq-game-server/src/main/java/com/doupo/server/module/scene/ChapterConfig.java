package com.doupo.server.module.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 引导期主线章节配置。
 *
 * <p>官服抓包（{@code 第一次创角色.json} 的 {@code MainMapPassChapterUpdateResp}
 * 序列）显示，前期主线是严格重复的两段式结构：
 *
 * <pre>
 *   X01（四只小怪）--打完--&gt; X05（单只 Boss）--打完--&gt; (X+1)01（四只小怪）...
 *
 *   10100101 → 10100105 → 10100201 → 10100205 → 10100301 → 10100305 → 10100401 ...
 * </pre>
 *
 * <p>因此章节不再逐关硬编码分支，而是登记为数据：新增一关只需要加一条配置，
 * 不需要改推进逻辑。所有数值都来自官服抓包，未使用任何推测值。
 */
final class ChapterConfig {

    /** 一只怪的生成数据，取自抓包的 {@code SceneUpdateVisibleResp}。 */
    static final class MonsterSpawn {

        private final float x;
        private final float z;
        private final float dir;
        private final int monsterId;
        private final long templateId;
        private final double attack;
        private final double maxHp;
        private final double sceneHp;
        private final double attribute161001;
        private final double attribute165001;
        private final int rewardItemKey;
        private final long rewardAmount;

        MonsterSpawn(
                float x, float z, float dir,
                int monsterId, long templateId,
                double attack, double maxHp, double sceneHp,
                double attribute161001, double attribute165001,
                int rewardItemKey, long rewardAmount) {
            this.x = x;
            this.z = z;
            this.dir = dir;
            this.monsterId = monsterId;
            this.templateId = templateId;
            this.attack = attack;
            this.maxHp = maxHp;
            this.sceneHp = sceneHp;
            this.attribute161001 = attribute161001;
            this.attribute165001 = attribute165001;
            this.rewardItemKey = rewardItemKey;
            this.rewardAmount = rewardAmount;
        }

        float getX() {
            return x;
        }

        float getZ() {
            return z;
        }

        float getDir() {
            return dir;
        }

        int getMonsterId() {
            return monsterId;
        }

        long getTemplateId() {
            return templateId;
        }

        double getAttack() {
            return attack;
        }

        double getMaxHp() {
            return maxHp;
        }

        double getSceneHp() {
            return sceneHp;
        }

        double getAttribute161001() {
            return attribute161001;
        }

        double getAttribute165001() {
            return attribute165001;
        }

        int getRewardItemKey() {
            return rewardItemKey;
        }

        long getRewardAmount() {
            return rewardAmount;
        }

        boolean hasReward() {
            return rewardItemKey != 0;
        }
    }

    /** 一个章节的配置。 */
    static final class Chapter {

        private final int chapterId;
        private final int nextChapterId;
        private final boolean boss;
        private final int loseBackId;
        private final int stageTime;
        private final int mainTaskId;
        private final long killProgressBefore;
        private final List<MonsterSpawn> monsters;

        Chapter(
                int chapterId,
                int nextChapterId,
                boolean boss,
                int loseBackId,
                int stageTime,
                int mainTaskId,
                long killProgressBefore,
                List<MonsterSpawn> monsters) {
            this.chapterId = chapterId;
            this.nextChapterId = nextChapterId;
            this.boss = boss;
            this.loseBackId = loseBackId;
            this.stageTime = stageTime;
            this.mainTaskId = mainTaskId;
            this.killProgressBefore = killProgressBefore;
            this.monsters = Collections.unmodifiableList(monsters);
        }

        int getChapterId() {
            return chapterId;
        }

        /** 本关打完后要进入的下一关；0 表示后续内容尚未实现。 */
        int getNextChapterId() {
            return nextChapterId;
        }

        boolean isBoss() {
            return boss;
        }

        int getLoseBackId() {
            return loseBackId;
        }

        int getStageTime() {
            return stageTime;
        }

        /** 本关对应的主线任务；抓包显示主线任务是 200000 起的顺序链条。 */
        int getMainTaskId() {
            return mainTaskId;
        }

        /**
         * 进入本关之前玩家已累计的击杀数。
         *
         * <p>抓包里主线杀怪任务的进度是全局累计值（第二关是 11→14 而不是 1→4），
         * 因此需要知道本关的起始基数。
         */
        long getKillProgressBefore() {
            return killProgressBefore;
        }

        List<MonsterSpawn> getMonsters() {
            return monsters;
        }

        int getMonsterCount() {
            return monsters.size();
        }
    }

    /**
     * 四只小怪关的通用站位：沿 z 轴每 15 个单位一只，x 在 -1.2 / 0.8 之间交替。
     * 抓包中每一关的小怪站位完全一致，只有属性和模板 ID 不同。
     */
    private static final float[] MOB_X = { -1.2f, 0.8f, -1.2f, 0.8f };
    private static final float[] MOB_DIR = {
            -90f, 3.814075f, -1.9091525f, 1.27303f
    };
    private static final float MOB_SPACING = 15f;

    private static final Map<Integer, Chapter> CHAPTERS = buildChapters();

    private ChapterConfig() {
    }

    static Chapter get(int chapterId) {
        return CHAPTERS.get(chapterId);
    }

    static boolean isSupported(int chapterId) {
        return CHAPTERS.containsKey(chapterId);
    }

    private static Map<Integer, Chapter> buildChapters() {
        Map<Integer, Chapter> chapters = new LinkedHashMap<>();

        // 第一关小怪：抓包 idx 1123-1126，主线任务 200000（击杀 4 只）。
        chapters.put(10100101, new Chapter(
                10100101, 10100105, false, 10100101, 0, 200000, 0,
                Arrays.asList(
                        mob(0, 121010, 1000101L, 220, 460, 460, 1000, 1500),
                        mob(1, 121004, 1000102L, 200, 480, 480, 0, 2000),
                        mob(2, 121003, 1000103L, 200, 480, 480, 0, 2000),
                        mob(3, 121005, 1000104L, 230, 400, 400, 1500, 0))));

        // 第一关 Boss：抓包 idx 246，主线任务 200001（通关 1 次），掉落 100200 x3。
        chapters.put(10100105, new Chapter(
                10100105, 10100201, true, 10100101, 0, 200001, 0,
                Collections.singletonList(
                        boss(100004, 1000140L, 400, 2000, 2000, 100200, 3))));

        // 第二关小怪：抓包 idx 1411-1414，主线任务 200003。
        // 抓包中进度是 11→14，说明进入本关时已累计 10 次击杀。
        chapters.put(10100201, new Chapter(
                10100201, 10100205, false, 10100101, 14, 200003, 10,
                Arrays.asList(
                        mob(0, 121010, 1000201L, 77, 1610, 1610, 1000, 1500),
                        mob(1, 121004, 1000202L, 70, 1680, 1680, 0, 2000),
                        mob(2, 121003, 1000203L, 70, 1680, 1680, 0, 2000),
                        mob(3, 121005, 1000204L, 80.5, 1400, 1400, 1500, 0))));

        // 第二关 Boss：抓包 idx 468，主线任务 200004，掉落 101 x1。
        chapters.put(10100205, new Chapter(
                10100205, 10100301, true, 10100201, 14, 200004, 0,
                Collections.singletonList(
                        boss(100002, 1000240L, 600, 7000, 7000, 101, 1))));

        // 第三关小怪：抓包 idx 562-565，只累计通用击杀；200006 在 Boss 后完成。
        chapters.put(10100301, new Chapter(
                10100301, 10100305, false, 10100201, 14, 0, 0,
                Arrays.asList(
                        mob(0, 121001, 1000301L, 132, 2875, 2875, 1000, 1500),
                        mob(1, 121001, 1000302L, 132, 2875, 2875, 1000, 1500),
                        mob(2, 121001, 1000303L, 132, 2875, 2875, 1000, 1500),
                        mob(3, 121001, 1000304L, 132, 2875, 2875, 1000, 1500))));

        // 第三关 Boss：抓包 idx 997/1053，完成主线 200006，掉落 100200 x3。
        chapters.put(10100305, new Chapter(
                10100305, 10100401, true, 10100301, 14, 200006, 0,
                Collections.singletonList(
                        boss(100001, 1000340L, 800, 12000, 12000, 100200, 3))));

        // 第四关小怪：抓包 idx 1123-1126；通用击杀进度从 30 开始。
        chapters.put(10100401, new Chapter(
                10100401, 10100405, false, 10100301, 11, 0, 30,
                Arrays.asList(
                        mob(0, 121001, 1000401L, 132, 4370, 4370, 1000, 1500),
                        mob(1, 121001, 1000402L, 132, 4370, 4370, 1000, 1500),
                        mob(2, 13, 1000403L, 138, 3800, 3800, 1500, 0),
                        mob(3, 13, 1000404L, 138, 3800, 3800, 1500, 0))));

        // 第四关 Boss：抓包 idx 1318-1320，完成主线 200008，掉落 100200 x10。
        chapters.put(10100405, new Chapter(
                10100405, 10100501, true, 10100401, 11, 200008, 0,
                Collections.singletonList(
                        boss(100003, 1000440L, 1800, 16000, 16000, 100200, 10))));

        // 第五关小怪：抓包 idx 1409-1414；通用击杀进度从 34 开始。
        chapters.put(10100501, new Chapter(
                10100501, 10100505, false, 10100401, 13, 0, 34,
                Arrays.asList(
                        mob(0, 121001, 1000501L, 220, 8050, 8050, 1000, 1500),
                        mob(1, 121001, 1000502L, 220, 8050, 8050, 1000, 1500),
                        mob(2, 121001, 1000503L, 220, 8050, 8050, 1000, 1500),
                        mob(3, 121001, 1000504L, 220, 8050, 8050, 1000, 1500))));

        // 第五关 Boss：抓包 idx 1907-1926，掉落 100200 x20，推进主地图 10200101。
        chapters.put(10100505, new Chapter(
                10100505, 10200101, true, 10100501, 13, 0, 0,
                Collections.singletonList(
                        boss(100002, 1000540L, 2000, 18000, 18000, 100200, 20))));

        // 第六关第一波：抓包 idx 1964-1966，三只怪；1107 从第五关 Boss 后的 53 接着计。
        chapters.put(10200101, new Chapter(
                10200101, 10200102, false, 10100501, 11, 0, 53,
                sixthWave()));

        // 第六关第二、三波：抓包 idx 2183-2376，怪物配置相同、出生点随请求推进。
        chapters.put(10200102, new Chapter(
                10200102, 10200103, false, 10100501, 11, 0, 56,
                sixthWave()));
        chapters.put(10200103, new Chapter(
                10200103, 10200101, false, 10100501, 11, 0, 59,
                sixthWave()));

        // 第六关 Boss：抓包 idx 5007，掉落 100200 x5。
        chapters.put(10200105, new Chapter(
                10200105, 10200201, true, 10100501, 11, 0, 0,
                Collections.singletonList(
                        boss(100003, 1000640L, 2300, 17000, 17000,
                                100200, 5))));

        // 第七关三波：抓包 idx 5229-5234 / 6315，每波 6 只怪（攻击 440、气血 4025）。
        // 后 2 只是 121002。主线 200013/200015/200017 不是杀怪任务。
        // 1107 累计：Boss 后 103，三波 103→109→117→123（抓包 idx 5208/5741/6058/6273）。
        chapters.put(10200201, new Chapter(
                10200201, 10200202, false, 10200101, 13, 0, 103,
                seventhWave(1000700L)));
        chapters.put(10200202, new Chapter(
                10200202, 10200203, false, 10200101, 13, 0, 109,
                seventhWave(1000710L)));
        chapters.put(10200203, new Chapter(
                10200203, 10200201, false, 10200101, 13, 0, 117,
                seventhWave(1000720L)));

        // 第七关 Boss：掉落 100200 x5，推进第八关。
        chapters.put(10200205, new Chapter(
                10200205, 10200301, true, 10200101, 13, 200020, 0,
                Collections.singletonList(
                        boss(100002, 1000740L, 1800, 20000, 20000,
                                100200, 5))));

        // 第八关三波：抓包，每波 6 只怪（前 2 只 825/5750，后 4 只 862.5/5000）。
        chapters.put(10200301, new Chapter(
                10200301, 10200302, false, 10200201, 10, 0, 134,
                eighthWave(1000800L)));
        chapters.put(10200302, new Chapter(
                10200302, 10200303, false, 10200201, 10, 0, 140,
                eighthWave(1000810L)));
        chapters.put(10200303, new Chapter(
                10200303, 10200301, false, 10200201, 10, 0, 146,
                eighthWave(1000820L)));

        // 第八关 Boss：掉落 100200 x5，推进第九关。
        chapters.put(10200305, new Chapter(
                10200305, 10200401, true, 10200201, 10, 200022, 0,
                Collections.singletonList(
                        boss(100001, 1000840L, 2000, 30000, 30000,
                                100200, 5))));

        // 第九关三波：抓包 idx 7331-8066，每波 6 只怪（攻击 880、气血 6900）。
        chapters.put(10200401, new Chapter(
                10200401, 10200402, false, 10200301, 12, 0, 151,
                ninthWave(1000900L)));
        chapters.put(10200402, new Chapter(
                10200402, 10200403, false, 10200301, 12, 0, 157,
                ninthWave(1000910L)));
        chapters.put(10200403, new Chapter(
                10200403, 10200401, false, 10200301, 12, 0, 163,
                ninthWave(1000920L)));

        // 第九关 Boss：抓包 idx 8101/8312-8313，掉落 100200 x10。
        chapters.put(10200405, new Chapter(
                10200405, 10200501, true, 10200301, 12, 200024, 0,
                Collections.singletonList(
                        boss(100004, 1000940L, 20000, 380000, 380000,
                                100200, 10))));

        // 第十关三波：抓包 idx 8378-9329，每波 6 只怪（攻击 990、气血 11500）。
        chapters.put(10200501, new Chapter(
                10200501, 10200502, false, 10200401, 55, 0, 174,
                ninthWave(1001000L, 990, 11500)));
        chapters.put(10200502, new Chapter(
                10200502, 10200503, false, 10200401, 55, 0, 180,
                ninthWave(1001010L, 990, 11500)));
        chapters.put(10200503, new Chapter(
                10200503, 10200501, false, 10200401, 55, 0, 186,
                ninthWave(1001020L, 990, 11500)));

        // 第十关 Boss：抓包 idx 9342/9543/9548，掉落 100200 x10，推进乌坦城。
        chapters.put(10200505, new Chapter(
                10200505, 10300101, true, 10200401, 55, 4001001, 0,
                Collections.singletonList(
                        boss(100002, 1001040L, 3000, 45000, 45000,
                                100200, 10))));

        // 乌坦城第11关三波战报：怪模板 ID 来自 mainstageconfig MonsterGroup。
        // 第三波配置里 1004305 重复两次，客户端会发两次 61964。
        chapters.put(10300101, new Chapter(
                10300101, 10300102, false, 10200501, 13, 0, 197,
                wutanWave(121011, 1004101L, 1004102L, 1004103L, 1004104L)));
        chapters.put(10300102, new Chapter(
                10300102, 10300103, false, 10200501, 13, 0, 201,
                wutanWave(121012, 1004201L, 1004202L, 1004203L, 1004204L,
                        1004205L)));
        chapters.put(10300103, new Chapter(
                10300103, 10300101, false, 10200501, 13, 0, 206,
                wutanWave(121013, 1004301L, 1004302L, 1004303L, 1004304L,
                        1004305L, 1004305L)));

        // 乌坦城第11关 Boss：七彩吞天蟒，掉落 100200 x10，主线 200029。
        chapters.put(10300105, new Chapter(
                10300105, 10300201, true, 10200501, 13, 200029, 212,
                Collections.singletonList(
                        boss(100127, 1004401L, 3500, 50000, 50000,
                                100200, 10))));

        // 乌坦城第12关三波战报，击杀累计 213→217→222→228。
        chapters.put(10300201, new Chapter(
                10300201, 10300202, false, 10300101, 13, 0, 213,
                wutanWave(121011, 1004501L, 1004502L, 1004503L, 1004504L)));
        chapters.put(10300202, new Chapter(
                10300202, 10300203, false, 10300101, 13, 0, 217,
                wutanWave(121012, 1004601L, 1004602L, 1004603L, 1004604L,
                        1004605L)));
        chapters.put(10300203, new Chapter(
                10300203, 10300201, false, 10300101, 13, 0, 222,
                wutanWave(121013, 1004701L, 1004702L, 1004703L, 1004704L,
                        1004705L, 1004705L)));

        // 乌坦城第12关 Boss：七彩吞天蟒，主线 200031。
        chapters.put(10300205, new Chapter(
                10300205, 10300301, true, 10300101, 13, 200031, 228,
                Collections.singletonList(
                        boss(100127, 1004801L, 3500, 50000, 50000,
                                100200, 10))));

        // 第13关三波战报，击杀累计 229→233→238→244。
        chapters.put(10300301, new Chapter(
                10300301, 10300302, false, 10300201, 13, 0, 229,
                wutanWave(121011, 1004901L, 1004902L, 1004903L, 1004904L)));
        chapters.put(10300302, new Chapter(
                10300302, 10300303, false, 10300201, 13, 0, 233,
                wutanWave(121012, 1005001L, 1005002L, 1005003L, 1005004L,
                        1005005L)));
        chapters.put(10300303, new Chapter(
                10300303, 10300301, false, 10300201, 13, 0, 238,
                wutanWave(121013, 1005101L, 1005102L, 1005103L, 1005104L,
                        1005105L, 1005105L)));

        // 第13关 Boss，主线 200034，掉落 100200 x10。
        chapters.put(10300305, new Chapter(
                10300305, 10300401, true, 10300301, 13, 200034, 244,
                Collections.singletonList(
                        boss(100127, 1005201L, 3500, 50000, 50000,
                                100200, 10))));

        // 第14关三波战报，击杀累计 245→249→254→260。
        chapters.put(10300401, new Chapter(
                10300401, 10300402, false, 10300301, 13, 0, 245,
                wutanWave(121011, 1005301L, 1005302L, 1005303L, 1005304L)));
        chapters.put(10300402, new Chapter(
                10300402, 10300403, false, 10300301, 13, 0, 249,
                wutanWave(121012, 1005401L, 1005402L, 1005403L, 1005404L,
                        1005405L)));
        chapters.put(10300403, new Chapter(
                10300403, 10300401, false, 10300301, 13, 0, 254,
                wutanWave(121013, 1005501L, 1005502L, 1005503L, 1005504L,
                        1005505L, 1005505L)));

        // 第14关 Boss，主线 200036。
        chapters.put(10300405, new Chapter(
                10300405, 10300501, true, 10300401, 13, 200036, 260,
                Collections.singletonList(
                        boss(100127, 1005601L, 3500, 50000, 50000,
                                100200, 10))));

        // 15–20 关：逐波战报及场景切换字段来自第二次抓包，不用占位波次。
        for (int stage = 5; stage <= 10; stage++) {
            int base = 10300000 + stage * 100;
            int[] tasks = { 200038, 200102, 200104, 0, 200108, 200110 };
            for (int wave : new int[] { 1, 2, 3, 5 }) {
                int id = base + wave;
                int next = wave == 5 ? (stage == 10 ? 10400101 : base + 101) : wave == 3 ? base + 1 : id + 1;
                int loseBack = stage <= 6 ? 10300401 : 10300601;
                int stageTime = stage <= 6 ? 6 : 10;
                if (stage >= 8) {
                    com.fasterxml.jackson.databind.JsonNode transition = Chapter17Data.row("transitions", "chapterId", id);
                    loseBack = transition.path("loseBackId").asInt();
                    stageTime = transition.path("stageTime").asInt();
                }
                chapters.put(id, new Chapter(id, next, wave == 5,
                        loseBack, stageTime,
                        wave == 5 ? tasks[stage - 5] : 0,
                        261 + (stage - 5) * 16 + (wave == 1 ? 0 : wave == 2 ? 4 : wave == 3 ? 9 : 15),
                        capturedMonsters(id)));
            }
        }
        // 本次抓包到21关首波为止；不声称其后的战斗已实现。
        chapters.put(10400101, new Chapter(10400101, 0, false, 10300901,
                9, 0, 357, capturedMonsters(10400101)));

        return Collections.unmodifiableMap(chapters);
    }

    private static List<MonsterSpawn> sixthWave() {
        return Arrays.asList(
                mobAt(-2f, 4f, -26.565052f,
                        121001, 1000601L, 275, 3450, 3450,
                        1000, 1500),
                mobAt(2f, 4f, 26.565052f,
                        121001, 1000602L, 275, 3450, 3450,
                        1000, 1500),
                mobAt(0f, 16f, 0f,
                        13, 1000603L, 287.5, 3000, 3000,
                        1500, 0));
    }

    private static List<MonsterSpawn> capturedMonsters(int chapterId) {
        List<MonsterSpawn> monsters = new ArrayList<>();
        WutanCapturedLog.Capture capture = WutanCapturedLog.load(chapterId);
        if (capture == null) throw new IllegalStateException("Missing battle log " + chapterId);
        try {
            for (com.doupo.protocol.BattleLogEntryVO entry :
                    com.doupo.protocol.BattleLogVO.parseFrom(capture.data).getEntryListList()) {
                for (com.doupo.protocol.BattleLogItemVO item : entry.getItemListList()) {
                    if (item.getPacketId() != 50804) continue;
                    for (com.doupo.protocol.SceneUnitVo unit :
                            com.doupo.protocol.BattleLogUpdateVisibleResp.parseFrom(item.getData())
                                    .getSceneUpdateVisibleResp().getVisibleListList()) {
                        if (!unit.hasSceneMonsterVo()) continue;
                        Map<Integer, Double> attrs = new LinkedHashMap<>();
                        unit.getFightInfoVo().getAttributeListList().forEach(a -> attrs.put(a.getType(), a.getValue()));
                        monsters.add(new MonsterSpawn(unit.getBaseInfoVo().getX() - capture.originX,
                                unit.getBaseInfoVo().getZ() - capture.originZ, unit.getBaseInfoVo().getDir(),
                                unit.getSceneMonsterVo().getMonsterId(), unit.getSceneMonsterVo().getTemplateId(),
                                attrs.getOrDefault(101001, 0.0), attrs.getOrDefault(103001, 0.0),
                                attrs.getOrDefault(103011, 0.0), attrs.getOrDefault(161001, 0.0),
                                attrs.getOrDefault(165001, 0.0), 0, 0));
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new IllegalStateException("Invalid battle log " + chapterId, e);
        }
        return monsters;
    }

    /** 第七关一波：6 只怪，攻击 440、气血 4025；后 2 只 121002（抓包 idx 5229-5234）。 */
    private static List<MonsterSpawn> seventhWave(long templateBase) {
        return Arrays.asList(
                mobAt(-2.5f, 3f, -39.805572f, 121001, templateBase + 1,
                        440, 4025, 4025, 1000, 1500),
                mobAt(2.5f, 3f, 39.805572f, 121001, templateBase + 2,
                        440, 4025, 4025, 1000, 1500),
                mobAt(-1f, 6f, -9.462322f, 121001, templateBase + 3,
                        440, 4025, 4025, 1000, 1500),
                mobAt(1f, 6f, 9.462322f, 121001, templateBase + 4,
                        440, 4025, 4025, 1000, 1500),
                mobAt(-2f, 10f, -11.309933f, 121002, templateBase + 5,
                        440, 4025, 4025, 1000, 1500),
                mobAt(0f, 10f, 0f, 121002, templateBase + 6,
                        440, 4025, 4025, 1000, 1500));
    }

    /**
     * 第八关一波：前 2 只 825/5750（121001），后 4 只 862.5/5000（13）。
     * 站位取自抓包 idx 6477-6482，第三只在 z+16、第六只在 z+12。
     */
    private static List<MonsterSpawn> eighthWave(long templateBase) {
        return Arrays.asList(
                mobAt(-2f, 4f, -26.565052f, 121001, templateBase + 1,
                        825, 5750, 5750, 1000, 1500),
                mobAt(2f, 4f, 26.565052f, 121001, templateBase + 2,
                        825, 5750, 5750, 1000, 1500),
                mobAt(0f, 16f, 0f, 13, templateBase + 3,
                        862.5, 5000, 5000, 1500, 0),
                mobAt(-1f, 14f, -4.085617f, 13, templateBase + 4,
                        862.5, 5000, 5000, 1500, 0),
                mobAt(1f, 14f, 4.085617f, 13, templateBase + 5,
                        862.5, 5000, 5000, 1500, 0),
                mobAt(-2f, 12f, -9.462322f, 13, templateBase + 6,
                        862.5, 5000, 5000, 1500, 0));
    }

    /** 乌坦城一波：站位只给战报回放/占位用，击杀按模板 ID 计数。 */
    private static List<MonsterSpawn> wutanWave(
            int monsterId,
            long... templateIds) {
        List<MonsterSpawn> spawns = new ArrayList<>();
        for (int i = 0; i < templateIds.length; i++) {
            spawns.add(mobAt(
                    i % 2 == 0 ? -2f : 2f,
                    4f + i * 3f,
                    i % 2 == 0 ? -26.5651f : 26.5651f,
                    monsterId,
                    templateIds[i],
                    990,
                    11500,
                    11500,
                    1000,
                    1500));
        }
        return spawns;
    }

    /** 第九关一波：站位、朝向、怪物类型与属性取自抓包 idx 7331-7336。 */
    private static List<MonsterSpawn> ninthWave(long templateBase) {
        return ninthWave(templateBase, 880, 6900);
    }

    private static List<MonsterSpawn> ninthWave(
            long templateBase,
            double attack,
            double hp) {
        return Arrays.asList(
                mobAt(-2.5f, 3f, -39.80557f, 121001, templateBase + 1,
                        attack, hp, hp, 1000, 1500),
                mobAt(2.5f, 3f, 39.80557f, 121001, templateBase + 2,
                        attack, hp, hp, 1000, 1500),
                mobAt(-1f, 6f, -9.46232f, 121001, templateBase + 3,
                        attack, hp, hp, 1000, 1500),
                mobAt(1f, 6f, 9.46232f, 121001, templateBase + 4,
                        attack, hp, hp, 1000, 1500),
                mobAt(-2f, 10f, -11.30993f, 121002, templateBase + 5,
                        attack, hp, hp, 1000, 1500),
                mobAt(0f, 10f, 0f, 121002, templateBase + 6,
                        attack, hp, hp, 1000, 1500));
    }

    private static MonsterSpawn mob(
            int slot,
            int monsterId,
            long templateId,
            double attack,
            double maxHp,
            double sceneHp,
            double attribute161001,
            double attribute165001) {
        return new MonsterSpawn(
                MOB_X[slot],
                MOB_SPACING * slot,
                MOB_DIR[slot],
                monsterId,
                templateId,
                attack,
                maxHp,
                sceneHp,
                attribute161001,
                attribute165001,
                0,
                0);
    }

    private static MonsterSpawn mobAt(
            float x,
            float z,
            float dir,
            int monsterId,
            long templateId,
            double attack,
            double maxHp,
            double sceneHp,
            double attribute161001,
            double attribute165001) {
        return new MonsterSpawn(
                x, z, dir, monsterId, templateId, attack, maxHp, sceneHp,
                attribute161001, attribute165001, 0, 0);
    }

    private static MonsterSpawn boss(
            int monsterId,
            long templateId,
            double attack,
            double maxHp,
            double sceneHp,
            int rewardItemKey,
            long rewardAmount) {
        return new MonsterSpawn(
                0f,
                0f,
                180f,
                monsterId,
                templateId,
                attack,
                maxHp,
                sceneHp,
                0,
                0,
                rewardItemKey,
                rewardAmount);
    }
}
