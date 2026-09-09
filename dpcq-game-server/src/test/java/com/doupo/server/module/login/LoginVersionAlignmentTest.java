package com.doupo.server.module.login;

import com.doupo.pojo.account.Account;
import com.doupo.protocol.LoginAuthReq;
import com.doupo.protocol.LoginAuthResp;
import com.doupo.server.foundation.GameServerClock;
import com.doupo.server.module.account.AccountAuthService;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LoginVersionAlignmentTest {
    @Test
    public void serverLabelDoesNotOverwriteTheCapturedLoginResourceVersion() {
        AccountAuthService auth = new AccountAuthService() {
            @Override
            public Account authenticateByGuideSignature(String account, int time, String md5Check) {
                return null;
            }
        };
        LoginAuthHandler handler = new LoginAuthHandler(auth, GameServerClock.dayOne());
        LoginAuthResp response = handler.login(null, LoginAuthReq.getDefaultInstance());
        assertEquals(1, response.getResult());
        assertEquals("local-6.9.263", response.getVersion());
        // 第二次抓包 idx 1 的登录资源字段是 1，不是 PatchSetting 的 263。
        assertEquals(1, response.getResourceVersion());
    }
}
