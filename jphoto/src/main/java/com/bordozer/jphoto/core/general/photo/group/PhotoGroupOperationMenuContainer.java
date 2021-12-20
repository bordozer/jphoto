package com.bordozer.jphoto.core.general.photo.group;

import com.google.common.collect.Lists;

import java.util.List;

public class PhotoGroupOperationMenuContainer {

    private final List<PhotoGroupOperationMenu> groupOperationMenus;

    public PhotoGroupOperationMenuContainer(final List<PhotoGroupOperationMenu> groupOperationMenus) {
        this.groupOperationMenus = groupOperationMenus;
    }

    public PhotoGroupOperationMenuContainer(final PhotoGroupOperationMenu groupOperationMenu, final PhotoGroupOperationMenu... groupOperationMenus) {
        this.groupOperationMenus = Lists.asList(groupOperationMenu, groupOperationMenus);
    }

    public List<PhotoGroupOperationMenu> getGroupOperationMenus() {
        return groupOperationMenus;
    }

    public boolean isEmpty() {
        return groupOperationMenus == null || groupOperationMenus.size() == 0;
    }
}
