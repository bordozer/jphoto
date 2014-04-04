package ui.services.breadcrumbs.items;

import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.system.Services;
import core.services.translator.Language;

public class UserAlbumLinkBreadcrumbs extends AbstractBreadcrumb {

	private UserPhotoAlbum photoAlbum;

	public UserAlbumLinkBreadcrumbs( final UserPhotoAlbum photoAlbum, final Services services ) {
		super( services );
		this.photoAlbum = photoAlbum;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getUserPhotoAlbumPhotosLink( photoAlbum, language );
	}
}
