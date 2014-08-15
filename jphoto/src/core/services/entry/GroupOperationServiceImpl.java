package core.services.entry;

import core.general.configuration.ConfigurationKey;
import core.general.photo.group.PhotoGroupOperationMenu;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import utils.UserUtils;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GroupOperationServiceImpl implements GroupOperationService {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SecurityService securityService;

	@Override
	public PhotoGroupOperationMenuContainer getPhotoListPhotoGroupOperationMenuContainer( final User user ) {

		if ( securityService.isSuperAdminUser( user.getId() ) ) {
			return new PhotoGroupOperationMenuContainer( getSuperAdminGroupOperationMenus() );
		}

		return getNoPhotoGroupOperationMenuContainer();
	}

	@Override
	public PhotoGroupOperationMenuContainer getUserCardCustomPhotoListPhotoGroupOperationMenuContainer( final User userCardOwner, final User accessor ) {

		if ( UserUtils.isUsersEqual( accessor, userCardOwner ) ) {
			return new PhotoGroupOperationMenuContainer( getUserOwnPhotosGroupOperationMenus() );
		}

		if ( securityService.isSuperAdminUser( accessor.getId() ) ) {
			return new PhotoGroupOperationMenuContainer( getSuperAdminGroupOperationMenus() );
		}

		return getNoPhotoGroupOperationMenuContainer();
	}

	@Override
	public PhotoGroupOperationMenuContainer getNoPhotoGroupOperationMenuContainer() {
		return new PhotoGroupOperationMenuContainer( Collections.<PhotoGroupOperationMenu>emptyList() );
	}

	@Override
	public List<PhotoGroupOperationMenu> getUserOwnPhotosGroupOperationMenus() {
		return newArrayList( PhotoGroupOperationMenu.ARRANGE_PHOTO_ALBUMS
			, PhotoGroupOperationMenu.ARRANGE_TEAM_MEMBERS
			, PhotoGroupOperationMenu.SEPARATOR_MENU
			, PhotoGroupOperationMenu.ARRANGE_NUDE_CONTENT_MENU
			, PhotoGroupOperationMenu.MOVE_TO_GENRE_MENU
			, PhotoGroupOperationMenu.SEPARATOR_MENU, PhotoGroupOperationMenu.DELETE_PHOTOS_MENU
		);
	}

	@Override
	public List<PhotoGroupOperationMenu> getSuperAdminGroupOperationMenus() {
		final List<PhotoGroupOperationMenu> adminGroupOperationMenus = newArrayList();

		adminGroupOperationMenus.addAll( new PhotoGroupOperationMenuContainer( PhotoGroupOperationMenu.ARRANGE_NUDE_CONTENT_MENU, PhotoGroupOperationMenu.MOVE_TO_GENRE_MENU ).getGroupOperationMenus() );

		if ( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS ) ) {
			adminGroupOperationMenus.addAll( new PhotoGroupOperationMenuContainer( PhotoGroupOperationMenu.SEPARATOR_MENU, PhotoGroupOperationMenu.DELETE_PHOTOS_MENU ).getGroupOperationMenus() );
		}

		return adminGroupOperationMenus;
	}
}
