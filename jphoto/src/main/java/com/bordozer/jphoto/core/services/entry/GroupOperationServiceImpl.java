package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenu;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenuContainer;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service("groupOperationService")
public class GroupOperationServiceImpl implements GroupOperationService {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private SecurityService securityService;

    @Override
    public PhotoGroupOperationMenuContainer getPhotoListPhotoGroupOperationMenuContainer(final User user) {
        if (securityService.isSuperAdminUser(user.getId())) {
            return new PhotoGroupOperationMenuContainer(getSuperAdminGroupOperationMenus());
        }

        return getNoPhotoGroupOperationMenuContainer();
    }

    @Override
    public PhotoGroupOperationMenuContainer getNoPhotoGroupOperationMenuContainer() {
        return new PhotoGroupOperationMenuContainer(Collections.<PhotoGroupOperationMenu>emptyList());
    }

    @Override
    public List<PhotoGroupOperationMenu> getUserOwnPhotosGroupOperationMenus() {
        return newArrayList(PhotoGroupOperationMenu.ARRANGE_PHOTO_ALBUMS
                , PhotoGroupOperationMenu.ARRANGE_TEAM_MEMBERS
                , PhotoGroupOperationMenu.SEPARATOR_MENU
                , PhotoGroupOperationMenu.ARRANGE_NUDE_CONTENT_MENU
                , PhotoGroupOperationMenu.MOVE_TO_GENRE_MENU
                , PhotoGroupOperationMenu.SEPARATOR_MENU
                , PhotoGroupOperationMenu.DELETE_PHOTOS_MENU
        );
    }

    @Override
    public List<PhotoGroupOperationMenu> getSuperAdminGroupOperationMenus() {
        final List<PhotoGroupOperationMenu> adminGroupOperationMenus = newArrayList();

        adminGroupOperationMenus.addAll(new PhotoGroupOperationMenuContainer(PhotoGroupOperationMenu.ARRANGE_NUDE_CONTENT_MENU, PhotoGroupOperationMenu.MOVE_TO_GENRE_MENU).getGroupOperationMenus());

        if (configurationService.getBoolean(ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS)) {
            adminGroupOperationMenus.addAll(new PhotoGroupOperationMenuContainer(PhotoGroupOperationMenu.SEPARATOR_MENU, PhotoGroupOperationMenu.DELETE_PHOTOS_MENU).getGroupOperationMenus());
        }

        return adminGroupOperationMenus;
    }

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setSecurityService(final SecurityService securityService) {
        this.securityService = securityService;
    }
}
