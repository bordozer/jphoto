package com.bordozer.jphoto.ui.controllers.users.list;

import com.bordozer.jphoto.core.general.user.UserAvatar;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenu;

public class UserListData {

    private int photosByUser;
    private UserAvatar userAvatar;
    private EntryMenu userMenu;

    public int getPhotosByUser() {
        return photosByUser;
    }

    public void setPhotosByUser(final int photosByUser) {
        this.photosByUser = photosByUser;
    }

    public UserAvatar getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(final UserAvatar userAvatar) {
        this.userAvatar = userAvatar;
    }

    public EntryMenu getUserMenu() {
        return userMenu;
    }

    public void setUserMenu(final EntryMenu userMenu) {
        this.userMenu = userMenu;
    }
}
