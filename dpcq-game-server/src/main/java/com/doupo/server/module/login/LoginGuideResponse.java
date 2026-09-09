package com.doupo.server.module.login;

public final class LoginGuideResponse {

    public int state;
    public String msg;
    public GuideData data;

    public static final class GuideData {

        public String game;
        public String plat;
        public String gid;
        public String pid;
        public String account;
        public int current;
        public String sign;
        public int time;
        public String ext1;
        public String ext2;
        public String ext3;
        public GuideServerData guideServer;
    }

    public static final class GuideServerData {

        public int state;
        public int serverId;
        public String domain;
        public String ip;
        public int port;
        public String name;
        public long openTime;
        public int zoneId;
        public String zoneName;
        public int zoneOrder;
        public boolean newServer;
        public int recommond;
        public boolean banCreateRole;
        public int maintainFinishTime;
        public String serverVersion;
    }
}