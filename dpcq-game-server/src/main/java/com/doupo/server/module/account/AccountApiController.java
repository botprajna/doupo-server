package com.doupo.server.module.account;

import javax.servlet.http.HttpServletRequest;

import com.doupo.pojo.account.Account;
import com.doupo.server.module.account.AccountAuthService.AuthException;
import com.doupo.server.module.account.AccountAuthService.LoginResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountApiController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AccountApiController.class);

    private final AccountAuthService authService;

    public AccountApiController(AccountAuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<LoginData> login(
            @RequestBody LoginRequest request,
            HttpServletRequest servletRequest) {
        try {
            LoginResult result = authService.login(
                    request.getAccount(),
                    request.getPassword(),
                    servletRequest.getRemoteAddr());

            Account account = result.getAccount();

            LoginData data = new LoginData(
                    Long.toString(account.getId()),
                    account.getAccountName(),
                    result.getToken());

            return ApiResponse.success(data);
        } catch (AuthException exception) {
            return ApiResponse.failure(
                    exception.getCode(),
                    exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error("HTTP account login failed", exception);
            return ApiResponse.failure(
                    1500,
                    "服务器内部错误");
        }
    }

    @GetMapping(
            value = "/register",
            produces = "text/html;charset=UTF-8")
    public String registerPage() {
        return renderRegisterPage("", "");
    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = "text/html;charset=UTF-8")
    public String register(
            @RequestParam("account") String accountName,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword")
                    String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return renderRegisterPage(
                    accountName,
                    "两次输入的密码不一致");
        }

        try {
            authService.register(accountName, password);

            return renderRegisterPage(
                    accountName,
                    "注册成功，请返回游戏登录");
        } catch (AuthException exception) {
            return renderRegisterPage(
                    accountName,
                    exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error(
                    "HTTP account registration failed",
                    exception);

            return renderRegisterPage(
                    accountName,
                    "服务器内部错误");
        }
    }

    private String renderRegisterPage(
            String accountName,
            String message) {
        return "<!doctype html>"
                + "<html lang=\"zh-CN\"><head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" "
                + "content=\"width=device-width,initial-scale=1\">"
                + "<title>账号注册</title>"
                + "<style>"
                + "body{font-family:sans-serif;background:#f3f5f7;"
                + "margin:0;padding:24px}"
                + ".card{max-width:420px;margin:8vh auto;background:#fff;"
                + "padding:24px;border:1px solid #ddd;border-radius:12px}"
                + "input,button{box-sizing:border-box;width:100%;"
                + "padding:12px;margin-top:12px}"
                + "button{background:#1f5060;color:#fff;border:0;"
                + "border-radius:8px}"
                + ".message{margin-top:14px;color:#1f5060}"
                + "</style></head><body><div class=\"card\">"
                + "<h2>斗破苍穹：斗帝之路</h2>"
                + "<form method=\"post\" action=\"/register\">"
                + "<input name=\"account\" inputmode=\"numeric\" "
                + "maxlength=\"11\" placeholder=\"手机号\" value=\""
                + escapeHtml(accountName)
                + "\" required>"
                + "<input name=\"password\" type=\"password\" "
                + "maxlength=\"32\" "
                + "placeholder=\"密码（6位以上）\" required>"
                + "<input name=\"confirmPassword\" type=\"password\" "
                + "maxlength=\"32\" "
                + "placeholder=\"确认密码\" required>"
                + "<button type=\"submit\">注册</button></form>"
                + (message.isEmpty()
                        ? ""
                        : "<div class=\"message\">"
                                + escapeHtml(message)
                                + "</div>")
                + "</div></body></html>";
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public static final class LoginRequest {

        private String account;
        private String password;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static final class LoginData {

        private final String userId;
        private final String userName;
        private final String token;

        public LoginData(
                String userId,
                String userName,
                String token) {
            this.userId = userId;
            this.userName = userName;
            this.token = token;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getToken() {
            return token;
        }
    }

    public static final class ApiResponse<T> {

        private final int code;
        private final String msg;
        private final T data;

        private ApiResponse(int code, String msg, T data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

        public static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(
                    0,
                    "登录成功",
                    data);
        }

        public static <T> ApiResponse<T> failure(
                int code,
                String msg) {
            return new ApiResponse<>(
                    code,
                    msg,
                    null);
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public T getData() {
            return data;
        }
    }
}
