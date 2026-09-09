package com.doupo.server.module.scene;

/**
 * 境界表，数值来自 Luban {@code playerlevelconfig} / {@code playerstagetaskconfig}。
 *
 * <p>斗之气 MaxLevel=7，斗者 MaxLevel=16。{@code Exp} 是升到该级消耗的修为；
 * {@code NeedUpStage=1} 时客户端走 {@code HeroLevelBreak}，否则走
 * {@code HeroLevelUpgrade}。大境界顶格走 {@code HeroStepUpgrade}。
 */
final class PlayerRealmConfig {

    private static final int[] EXP_TO_REACH = {
            0,
            0,    // 1 三段
            500,  // 2 四段
            600,  // 3 五段
            700,  // 4 六段
            800,  // 5 七段
            900,  // 6 八段
            1000, // 7 九段
            1100, // 8 一星斗者
            800,  // 9 二星斗者
            800,  // 10 三星斗者
            1000, // 11 四星斗者
            1000, // 12 五星斗者
            1200, // 13 六星斗者
            1200, // 14 七星斗者
            1400, // 15 八星斗者
            1600, // 16 九星斗者
            1600  // 17 一星斗师
    };

    private static final int[] NEED_UP_STAGE = {
            0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1
    };

    private static final int[] STAGE_MAX_LEVEL = {
            0, 7, 16, 43
    };

    private static final RealmStats[] STATS = {
            null,
            stats(438, 107, 10968, 0, 0, 0),
            stats(560, 168, 14012, 0, 0, 0),
            stats(685, 230, 17130, 0, 0, 0),
            stats(812, 294, 20324, 0, 0, 0),
            stats(943, 359, 23594, 100, 50, 2500),
            stats(1077, 426, 26940, 100, 50, 2500),
            stats(1214, 495, 30362, 100, 50, 2500),
            stats(1354, 565, 33860, 1300, 650, 32500),
            stats(1497, 636, 37435, 1400, 700, 35000),
            stats(1643, 709, 41087, 1500, 750, 37500),
            stats(1792, 784, 44817, 1600, 800, 40000),
            stats(1944, 860, 48624, 1700, 850, 42500),
            stats(2100, 938, 52509, 1800, 900, 45000),
            stats(2258, 1017, 56473, 1900, 950, 47500),
            stats(2420, 1098, 60514, 2000, 1000, 50000),
            stats(2585, 1180, 64635, 2100, 1050, 52500),
            stats(2753, 1264, 68834, 8100, 4050, 202500)
    };

    /** 抓包 idx 9597：一星斗者战斗属性相对 base+FixNum 的额外部分。 */
    private static final double FIGHTER_ATK_OTHER = 3449.48976 - 1354 - 1300;
    private static final double FIGHTER_DEF_OTHER = 1508 - 565 - 650;
    private static final double FIGHTER_HP_OTHER = 88506.5 - 33860 - 32500;
    private static final double FIGHTER_FORCE_AT_STAR_ONE = 60577;
    private static final double FIGHTER_COMBAT_ATK_AT_STAR_ONE = 3449.48976;

    private PlayerRealmConfig() {
    }

    static long expToReach(int level) {
        if (level <= 0 || level >= EXP_TO_REACH.length) {
            return Long.MAX_VALUE;
        }
        return EXP_TO_REACH[level];
    }

    static long breakRequiredExp(int currentLevel) {
        return expToReach(currentLevel + 1);
    }

    static boolean needLevelBreak(int currentLevel) {
        return currentLevel >= 0
                && currentLevel < NEED_UP_STAGE.length
                && NEED_UP_STAGE[currentLevel] == 1;
    }

    static int stageMaxLevel(int stage) {
        if (stage <= 0 || stage >= STAGE_MAX_LEVEL.length) {
            return Integer.MAX_VALUE;
        }
        return STAGE_MAX_LEVEL[stage];
    }

    static boolean atStageCap(int level, int stage) {
        return level >= stageMaxLevel(stage);
    }

    static RealmStats statsOf(int level) {
        if (level <= 0 || level >= STATS.length) {
            return STATS[STATS.length - 1];
        }
        return STATS[level];
    }

    static double combatAtk(int level) {
        RealmStats stats = statsOf(level);
        return stats.atk + stats.atkFix + FIGHTER_ATK_OTHER;
    }

    static double combatDef(int level) {
        RealmStats stats = statsOf(level);
        return stats.def + stats.defFix + FIGHTER_DEF_OTHER;
    }

    static double combatHp(int level) {
        RealmStats stats = statsOf(level);
        return stats.hp + stats.hpFix + FIGHTER_HP_OTHER;
    }

    static double fightForce(int level) {
        return FIGHTER_FORCE_AT_STAR_ONE
                * combatAtk(level)
                / FIGHTER_COMBAT_ATK_AT_STAR_ONE;
    }

    private static RealmStats stats(
            double atk,
            double def,
            double hp,
            double atkFix,
            double defFix,
            double hpFix) {
        return new RealmStats(atk, def, hp, atkFix, defFix, hpFix);
    }

    static final class RealmStats {
        final double atk;
        final double def;
        final double hp;
        final double atkFix;
        final double defFix;
        final double hpFix;

        private RealmStats(
                double atk,
                double def,
                double hp,
                double atkFix,
                double defFix,
                double hpFix) {
            this.atk = atk;
            this.def = def;
            this.hp = hp;
            this.atkFix = atkFix;
            this.defFix = defFix;
            this.hpFix = hpFix;
        }
    }
}
