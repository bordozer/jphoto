package com.bordozer.jphoto.core.general.user;

import com.bordozer.jphoto.core.interfaces.Cacheable;

import java.io.File;

public class UserAvatar implements Cacheable {

    private final int userId;
    private File file;
    private String userAvatarFileUrl;

    public UserAvatar(final int userId) {
        this.userId = userId;
    }

    public boolean isHasAvatar() {
        return file != null;
    }

    public int getUserId() {
        return userId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public void setUserAvatarFileUrl(final String userAvatarFileUrl) {
        this.userAvatarFileUrl = userAvatarFileUrl;
    }

    public String getUserAvatarFileUrl() {
        return userAvatarFileUrl;
    }
}
