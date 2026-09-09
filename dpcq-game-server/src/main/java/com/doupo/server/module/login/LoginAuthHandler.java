package com.doupo.server.module.login;

import com.doupo.pojo.account.Account;
import com.doupo.protocol.LoginAuthReq;
import com.doupo.protocol.LoginAuthResp;
import com.doupo.server.foundation.GameServerClock;
import com.doupo.server.foundation.player.PlayerConnectionContext;
import com.doupo.server.module.account.AccountAuthService;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class LoginAuthHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LoginAuthHandler.class);

    private final AccountAuthService authService;
    private final GameServerClock serverClock;

    public LoginAuthHandler(
            AccountAuthService authService,
            GameServerClock serverClock) {
        this.authService = authService;
        this.serverClock = serverClock;
    }

    @PlayerCmd(needLogin = false)
    public LoginAuthResp login(
            IPlayerContext playerContext,
            LoginAuthReq request) {
        Account account = authService.authenticateByGuideSignature(
                request.getAccount(),
                request.getTime(),
                request.getMd5Check());

        if (account == null
                || request.getServerId() != 1) {
            LOGGER.warn(
                    "LoginAuth rejected: account={}, serverId={}",
                    request.getAccount(),
                    request.getServerId());

            return response(1, request.getDomainId(), 0);
        }

        ((PlayerConnectionContext) playerContext)
                .bindAccount(account);

        LOGGER.info(
                "LoginAuth accepted: account={}, playerId={}",
                account.getAccountName(),
                playerContext.getId());

        return response(
                0,
                request.getDomainId(),
                (int) account.getId());
    }

    private LoginAuthResp response(
            int result,
            int domainId,
            int reconnectKey) {
        long now = System.currentTimeMillis();

        return LoginAuthResp.newBuilder()
                .setResult(result)
                .setServerTime(now)
                .setVersion("local-6.9.263")
                .setReconnectKey(reconnectKey)
                .setServerOpen(true)
                .setServerOpenTime(serverClock.openTimeMillis())
                // 第二次抓包 idx 1；此字段不是 PatchSetting.m_ResourceVersion。
                .setResourceVersion(1)
                .setDomainId(domainId)
                .build();
    }
}
