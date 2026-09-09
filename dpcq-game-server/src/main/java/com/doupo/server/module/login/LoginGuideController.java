package com.doupo.server.module.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.doupo.pojo.account.Account;
import com.doupo.server.module.account.AccountAuthService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginGuideController {

    private final AccountAuthService authService;
    private final ObjectMapper objectMapper;

    @Value("${game.guide.server-ip}")
    private String serverIp;

    @Value("${game.netty.port}")
    private int serverPort;

    @Value("${game.server.id}")
    private int serverId;

    public LoginGuideController(
            AccountAuthService authService,
            ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(
            value = "/login/v2/imlj2/37wan/client/login/guide",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> guide(
            @RequestParam("publisher") String publisher,
            @RequestParam("gid") String gid,
            @RequestParam("pid") String pid,
            @RequestParam("clientVersion") String clientVersion,
            @RequestParam("token") String token,
            @RequestParam("os") String os) {

        Account account = authService.authenticateByToken(token);

        if (account == null) {
            return jsonResponse(
                    failure(1001, "登录状态已失效，请重新登录"));
        }

        int time = (int) (System.currentTimeMillis() / 1000L);

        LoginGuideResponse.GuideServerData server =
                new LoginGuideResponse.GuideServerData();

        server.state = 0;
        server.serverId = serverId;
        server.domain = serverIp;
        server.ip = serverIp;
        server.port = serverPort;
        server.name = "斗帝本地测试服";
        server.openTime = 0L;
        server.zoneId = 1;
        server.zoneName = "本地测试区";
        server.zoneOrder = 1;
        server.newServer = true;
        server.recommond = 1;
        server.banCreateRole = false;
        server.maintainFinishTime = 0;
        server.serverVersion = "";

        LoginGuideResponse.GuideData data =
                new LoginGuideResponse.GuideData();

        data.game = "imlj2";
        data.plat = "37wan";
        data.gid = gid;
        data.pid = pid;
        data.account = account.getAccountName();
        data.current = serverId;
        data.sign = authService.createGuideSignature(account, time);
        data.time = time;
        data.ext1 = "";
        data.ext2 =
                "{\"wa\":false,\"isNewAccount\":true}";
        data.ext3 = "";
        data.guideServer = server;

        LoginGuideResponse response =
                new LoginGuideResponse();

        response.state = 0;
        response.msg = "";
        response.data = data;

        return jsonResponse(response);
    }

    @GetMapping(
            value = "/login/v2/imlj2/37wan/client/login/check",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> loginCheck(
            @RequestParam("publisher") String publisher,
            @RequestParam("gid") String gid,
            @RequestParam("pid") String pid,
            @RequestParam("clientVersion") String clientVersion,
            @RequestParam("os") String os,
            @RequestParam("sign") String sign,
            @RequestParam("time") int time,
            @RequestParam("account") String accountName,
            @RequestParam("cdn") String cdn,
            @RequestParam("serverId") int requestedServerId) {

        Account account = authService.authenticateByGuideSignature(
                accountName,
                time,
                sign);

        if (account == null) {
            return jsonResponse(
                    loginCheckFailure(
                            20701,
                            "登录状态已失效，请重新登录"));
        }

        if (requestedServerId != serverId) {
            return jsonResponse(
                    loginCheckFailure(
                            20711,
                            "服务器不存在"));
        }

        LoginCheckResponse.LoginCheckData data =
                new LoginCheckResponse.LoginCheckData();

        data.game = "imlj2";
        data.plat = "37wan";
        data.gid = gid;
        data.pid = pid;
        data.account = account.getAccountName();
        data.time = time;
        data.current = serverId;
        data.sign = sign;
        data.loginServer = serverId;
        data.ext2 = "";
        data.ext3 = "";

        LoginCheckResponse response = new LoginCheckResponse();
        response.state = 0;
        response.msg = "";
        response.data = data;

        return jsonResponse(response);
    }

    private ResponseEntity<byte[]> jsonResponse(
            LoginGuideResponse response) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .contentLength(body.length)
                    .body(body);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Unable to serialize guide response",
                    exception);
        }
    }

    private ResponseEntity<byte[]> jsonResponse(
            LoginCheckResponse response) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .contentLength(body.length)
                    .body(body);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Unable to serialize login check response",
                    exception);
        }
    }

    private LoginGuideResponse failure(int state, String message) {
        LoginGuideResponse response =
                new LoginGuideResponse();

        response.state = state;
        response.msg = message;
        response.data = null;

        return response;
    }

    private LoginCheckResponse loginCheckFailure(
            int state,
            String message) {
        LoginCheckResponse response = new LoginCheckResponse();

        response.state = state;
        response.msg = message;
        response.data = null;

        return response;
    }

}
