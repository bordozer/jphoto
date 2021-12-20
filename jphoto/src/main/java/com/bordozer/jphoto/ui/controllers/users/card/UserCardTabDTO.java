package com.bordozer.jphoto.ui.controllers.users.card;

import com.bordozer.jphoto.core.enums.UserCardTab;

public class UserCardTabDTO {

    private final UserCardTab userCardTab;
    private int itemsOnTab;

    public UserCardTabDTO(final UserCardTab userCardTab) {
        this.userCardTab = userCardTab;
    }

    public UserCardTabDTO(final UserCardTab userCardTab, final int itemsOnTab) {
        this.userCardTab = userCardTab;
        this.itemsOnTab = itemsOnTab;
    }

    public UserCardTab getUserCardTab() {
        return userCardTab;
    }

    public int getItemsOnTab() {
        return itemsOnTab;
    }

    public void setItemsOnTab(final int itemsOnTab) {
        this.itemsOnTab = itemsOnTab;
    }
}
