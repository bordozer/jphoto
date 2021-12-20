package com.bordozer.jphoto.core.general.user;

import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;

import java.util.Date;

public class UsersSecurity extends AbstractBaseEntity {

    public static final String DEFAULT_DEBUG_PASSWORD = "eM2&gCyRuJX"; // TODO: default password

    private final User user;
    private Date lastLoginTime;
    private String lastLoginIp;
    private String authorizationKey;
    private Date lastActivityTime;

    private String userPassword = DEFAULT_DEBUG_PASSWORD;

    public UsersSecurity(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(final Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(final String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(final String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(final String userPassword) {
        this.userPassword = userPassword;
    }

    public Date getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(final Date lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }
}
