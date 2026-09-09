package com.doupo.server.module.account;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.doupo.pojo.account.Account;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountAuthService {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^1[3-9]\\d{9}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[\\u0021-\\u007E]{6,32}$");

    private static final int SESSION_DAYS = 7;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder(12);

    private final SecureRandom secureRandom = new SecureRandom();

    public synchronized Account register(String accountName, String password) {
        String normalizedAccount = normalizeAndValidate(accountName, password);
        BaseRepository<Account> repository = repository();

        if (repository.get("accountName", normalizedAccount) != null) {
            throw new AuthException(1002, "该手机号已注册");
        }

        Account account = new Account();
        account.setAccountName(normalizedAccount);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setStatus(0);
        repository.insert(account);
        return account;
    }

    public synchronized LoginResult login(
            String accountName,
            String password,
            String remoteAddress) {
        String normalizedAccount = normalizeAndValidate(accountName, password);
        BaseRepository<Account> repository = repository();
        Account account = repository.get("accountName", normalizedAccount);

        if (account == null
                || account.getStatus() != 0
                || account.getPasswordHash() == null
                || !passwordEncoder.matches(
                        password,
                        account.getPasswordHash())) {
            throw new AuthException(1001, "账号或密码错误");
        }

        String token = generateToken();
        account.setSessionTokenHash(hashToken(token));
        account.setSessionExpireTime(
                LocalDateTime.now().plusDays(SESSION_DAYS));
        account.setLastLoginIp(remoteAddress);
        account.setLastLoginTime(LocalDateTime.now());
        repository.update(account);

        return new LoginResult(account, token);
    }

    public Account authenticateByToken(String token) {
        String raw = token == null ? null : token.trim();

        if (raw == null || raw.isEmpty()) {
            return null;
        }

        Account account =
                repository().get("sessionTokenHash", hashToken(raw));

        return isSessionValid(account) ? account : null;
    }

    public Account authenticateByAccountName(String accountName) {
        String normalizedAccount =
                accountName == null ? "" : accountName.trim();

        if (normalizedAccount.isEmpty()) {
            return null;
        }

        Account account =
                repository().get("accountName", normalizedAccount);

        return isSessionValid(account) ? account : null;
    }

    public Account authenticateByGuideSignature(
            String accountName,
            int time,
            String signature) {
        Account account = authenticateByAccountName(accountName);

        if (account == null || signature == null) {
            return null;
        }

        byte[] expected = createGuideSignature(account, time)
                .getBytes(StandardCharsets.UTF_8);
        byte[] actual = signature
                .getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(expected, actual)
                ? account
                : null;
    }

    public String createGuideSignature(Account account, int time) {
        try {
            String payload = account.getAccountName() + ":" + time;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    account.getSessionTokenHash()
                            .getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"));

            byte[] signature = mac.doFinal(
                    payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder result =
                    new StringBuilder(signature.length * 2);

            for (byte value : signature) {
                result.append(String.format("%02x", value & 0xff));
            }

            return result.toString();
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to create guide signature",
                    exception);
        }
    }

    private boolean isSessionValid(Account account) {
        return account != null
                && account.getStatus() == 0
                && account.getSessionTokenHash() != null
                && !account.getSessionTokenHash().isEmpty()
                && account.getSessionExpireTime() != null
                && !account.getSessionExpireTime()
                        .isBefore(LocalDateTime.now());
    }

    private String normalizeAndValidate(
            String accountName,
            String password) {
        String normalizedAccount =
                accountName == null ? "" : accountName.trim();

        if (!PHONE_PATTERN.matcher(normalizedAccount).matches()) {
            throw new AuthException(1003, "请输入正确的手机号");
        }

        if (password == null
                || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new AuthException(
                    1004,
                    "密码需为6至32位常用字符");
        }

        return normalizedAccount;
    }

    private BaseRepository<Account> repository() {
        return SlimDao.getRepository(Account.class);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));

            StringBuilder result =
                    new StringBuilder(hash.length * 2);

            for (byte value : hash) {
                result.append(
                        String.format("%02x", value & 0xff));
            }

            return result.toString();
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to hash session token",
                    exception);
        }
    }

    public static final class LoginResult {

        private final Account account;
        private final String token;

        private LoginResult(Account account, String token) {
            this.account = account;
            this.token = token;
        }

        public Account getAccount() {
            return account;
        }

        public String getToken() {
            return token;
        }
    }

    public static final class AuthException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private final int code;

        public AuthException(int code, String message) {
            super(message);
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
