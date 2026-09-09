package com.doupo.server.module.scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Player-Actor-owned continuation state, matching the existing in-memory scene model. */
final class Chapter17Progress {
    final Set<Integer> tasks = new HashSet<>();
    final Set<Integer> finished = new HashSet<>();
    final Set<Integer> rewarded = new HashSet<>();
    final Set<Integer> bossRewarded = new HashSet<>();
    final Map<Integer, Long> items = new HashMap<>();
    final Map<Integer, Long> currencies = new HashMap<>();
    boolean arrivedSixteen;
    int watchedEquips;
    boolean commonDrawn;
    boolean towerOpen;
    boolean towerCG;
    int towerPass;
    int towerPending;
    boolean hosting;
    int hostGeneration;
    int hostStartPass;
    long towerRewardStart;
    long killCount = 261;
}
