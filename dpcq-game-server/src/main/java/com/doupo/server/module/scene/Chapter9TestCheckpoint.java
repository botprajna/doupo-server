package com.doupo.server.module.scene;

/** Local test fixture: 第一次创角色.json, immediately after idx 7328, before 7329.
 * Not a new-player balance rule. Level 7 means 九段斗之气 in playerlevelconfig.
 */
final class Chapter9TestCheckpoint {
    static final int CHAPTER = 10200401;
    static final int LEVEL = 7;
    static final int POWER = 25778; // idx 6965
    static final double ATTACK = 1846.5465600000002; // idx 7177 / 7330
    static final double HP = 47938.8; // idx 7311
    static final double DEFENSE = 728; // last HeroStatUpdateResp before 7329
    static final int[] SKILLS = {80140001, 80130011, 80130101, 80128011, 100001, 80128001};
    // Last HeroSkillAct/SkillUpdate for all six: star=2; idx 333..6960.
    // 80128001 was already awarded at idx 6465, but slot 3001 is still EMPTY.
    static final int[][] ITEMS = {{6, 80130010, 2}, {12, 80128010, 1},
            {15, 80140000, 1}, {19, 201010, 50}};
    // Latest saved guide ID per group, only from requests before idx 7329.
    // Deliberately excludes group 10043 (the ninth-boss teaching sequence).
    static final int[][] GUIDES = {{901, 60901}, {904, 60904}, {905, 60905},
            {1011, 100101}, {10000, 1000003}, {10011, 1001104}, {10012, 1001203},
            {10019, 1001903}, {10021, 1002102}, {10031, 1003106}, {10033, 1003303},
            {10041, 1004106}, {20001, 2000102}, {20011, 2001103}, {20021, 2002103}};
    static final int[] MODULES = {102, 103, 107, 112, 1510, 1600, 1601, 1608,
            3101, 3102, 3701, 4007, 4008, 4201, 4202, 4203, 4204,
            4304, 4401, 4404, 4411, 4415, 42011};
    static final int[] PASSED_BOSSES = {10100105, 10100205, 10100305, 10100405,
            10100505, 10200105, 10200205, 10200305};

    private Chapter9TestCheckpoint() { }
}
