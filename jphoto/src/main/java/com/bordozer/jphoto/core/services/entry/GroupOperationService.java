package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenu;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenuContainer;
import com.bordozer.jphoto.core.general.user.User;

import java.util.List;

public interface GroupOperationService {

    PhotoGroupOperationMenuContainer getPhotoListPhotoGroupOperationMenuContainer(final User user);

    PhotoGroupOperationMenuContainer getNoPhotoGroupOperationMenuContainer();

    List<PhotoGroupOperationMenu> getUserOwnPhotosGroupOperationMenus();

    List<PhotoGroupOperationMenu> getSuperAdminGroupOperationMenus();
}
