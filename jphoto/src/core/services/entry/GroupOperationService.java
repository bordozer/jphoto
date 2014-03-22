package core.services.entry;

import core.general.photo.group.PhotoGroupOperationMenu;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;

import java.util.List;

public interface GroupOperationService {

	PhotoGroupOperationMenuContainer getPhotoListPhotoGroupOperationMenuContainer( final User user );

	PhotoGroupOperationMenuContainer getPhotoListPhotoGroupOperationMenuContainer( final PhotoGroupOperationMenuContainer customGroupOperationMenuContainer, final boolean isBestPhotoList, final User user );

	PhotoGroupOperationMenuContainer getUserCardCustomPhotoListPhotoGroupOperationMenuContainer( final User userCardOwner, final User accessor );

	PhotoGroupOperationMenuContainer getNoPhotoGroupOperationMenuContainer();

	List<PhotoGroupOperationMenu> getUserOwnPhotosGroupOperationMenus();

	List<PhotoGroupOperationMenu> getSuperAdminGroupOperationMenus();
}
