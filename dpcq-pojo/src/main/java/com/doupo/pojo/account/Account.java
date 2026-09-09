package com.doupo.pojo.account;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

@Repository
@Table(
        name = "account",
        comment = "Game account",
        dbAlias = "game",
        indexs = {
                @Index(
                        name = "uk_account_name",
                        columns = {"account_name"},
                        type = IndexType.UNIQUE)
        })
public class Account extends AbstractEntity {

    @Id(strategy = Strategy.AUTO)
    @Column(comment = "Account ID")
    private long id;

    @Column(
            name = "account_name",
            length = 64,
            comment = "Account name",
            readonly = true)
    private String accountName;

    @Column(comment = "Account status")
    private int status;

    @Column(
            name = "last_login_ip",
            length = 64,
            comment = "Last login IP")
    private String lastLoginIp;

    @Column(
            name = "last_login_time",
            comment = "Last login time")
    private LocalDateTime lastLoginTime;

    @Column(
            name = "password_hash",
            length = 100,
            comment = "BCrypt password hash")
    private String passwordHash;

    @Column(
            name = "session_token_hash",
            length = 64,
            comment = "SHA-256 session token hash")
    private String sessionTokenHash;

    @Column(
            name = "session_expire_time",
            comment = "Session expiration time")
    private LocalDateTime sessionExpireTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSessionTokenHash() {
        return sessionTokenHash;
    }

    public void setSessionTokenHash(String sessionTokenHash) {
        this.sessionTokenHash = sessionTokenHash;
    }

    public LocalDateTime getSessionExpireTime() {
        return sessionExpireTime;
    }

    public void setSessionExpireTime(LocalDateTime sessionExpireTime) {
        this.sessionExpireTime = sessionExpireTime;
    }
}