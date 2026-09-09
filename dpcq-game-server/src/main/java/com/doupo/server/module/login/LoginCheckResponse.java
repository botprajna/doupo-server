package com.doupo.server.module.login;

public final class LoginCheckResponse {

    public int state;
    public String msg;
    public LoginCheckData data;

    public static final class LoginCheckData {

        public String game;
        public String plat;
        public String gid;
        public String pid;
        public String account;
        public int time;
        public int current;
        public String sign;
        public int loginServer;
        public String ext2;
        public String ext3;
    }
}
