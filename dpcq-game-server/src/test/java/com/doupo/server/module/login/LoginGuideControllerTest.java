package com.doupo.server.module.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.doupo.pojo.account.Account;
import com.doupo.server.module.account.AccountAuthService;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class LoginGuideControllerTest {

    @Test
    public void returnsUtf8JsonWithContentLength() throws Exception {
        Account account = new Account();
        account.setAccountName("13800000000");
        account.setSessionTokenHash(
                "0123456789abcdef0123456789abcdef"
                        + "0123456789abcdef0123456789abcdef");

        AccountAuthService authService = new AccountAuthService() {
            @Override
            public Account authenticateByToken(String token) {
                return account;
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
        LoginGuideController controller =
                new LoginGuideController(authService, objectMapper);

        setField(controller, "serverIp", "192.168.1.23");
        setField(controller, "serverPort", 19090);
        setField(controller, "serverId", 1);

        ResponseEntity<byte[]> response = controller.guide(
                "sy",
                "1024375",
                "1",
                "6.8.1",
                "test-token",
                "android");

        byte[] body = response.getBody();
        assertNotNull(body);
        assertEquals(body.length,
                response.getHeaders().getContentLength());
        assertEquals(MediaType.APPLICATION_JSON_UTF8,
                response.getHeaders().getContentType());
        assertFalse(response.getHeaders().containsKey(
                "Transfer-Encoding"));

        JsonNode json = objectMapper.readTree(body);
        assertEquals(0, json.get("state").asInt());
        assertEquals("imlj2", json.get("data").get("game").asText());
        assertEquals(19090,
                json.get("data")
                        .get("guideServer")
                        .get("port")
                        .asInt());
    }

    @Test
    public void acceptsGuideSignatureForLoginCheck() throws Exception {
        Account account = account();
        LoginGuideController controller = controller(account);

        JsonNode guide = objectMapper().readTree(
                controller.guide(
                        "sy", "1024375", "1", "6.8.1",
                        "test-token", "android")
                        .getBody());

        String sign = guide.get("data").get("sign").asText();
        int time = guide.get("data").get("time").asInt();

        ResponseEntity<byte[]> response = controller.loginCheck(
                "sy", "1024375", "1", "6.8.1", "android",
                sign, time, "13800000000", "false", 1);

        JsonNode json = objectMapper().readTree(response.getBody());
        assertEquals(0, json.get("state").asInt());
        assertEquals("13800000000",
                json.get("data").get("account").asText());
        assertEquals(1,
                json.get("data").get("loginServer").asInt());
    }

    @Test
    public void rejectsInvalidLoginCheckSignature() throws Exception {
        LoginGuideController controller = controller(account());

        ResponseEntity<byte[]> response = controller.loginCheck(
                "sy", "1024375", "1", "6.8.1", "android",
                "invalid", 123, "13800000000", "false", 1);

        JsonNode json = objectMapper().readTree(response.getBody());
        assertEquals(20701, json.get("state").asInt());
    }

    private LoginGuideController controller(Account account)
            throws Exception {
        AccountAuthService authService = new AccountAuthService() {
            @Override
            public Account authenticateByToken(String token) {
                return account;
            }

            @Override
            public Account authenticateByAccountName(String accountName) {
                return account;
            }

            @Override
            public Account authenticateByGuideSignature(
                    String accountName,
                    int time,
                    String signature) {
                String expected = createGuideSignature(account, time);
                return expected.equals(signature) ? account : null;
            }
        };

        LoginGuideController controller =
                new LoginGuideController(authService, objectMapper());

        setField(controller, "serverIp", "192.168.1.23");
        setField(controller, "serverPort", 19090);
        setField(controller, "serverId", 1);

        return controller;
    }

    private Account account() {
        Account account = new Account();
        account.setAccountName("13800000000");
        account.setSessionTokenHash(
                "0123456789abcdef0123456789abcdef"
                        + "0123456789abcdef0123456789abcdef");
        return account;
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    private void setField(
            Object target,
            String name,
            Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
