package core.services.translator.message;

import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.system.Services;
import core.services.translator.Language;

public class UserAlbumLinkParameter extends AbstractTranslatableMessageParameter {

	private UserPhotoAlbum userPhotoAlbum;

	public UserAlbumLinkParameter( final UserPhotoAlbum userPhotoAlbum, final Services services ) {
		super( services );
		this.userPhotoAlbum = userPhotoAlbum;
	}

	@Override
	public String getValue( final Language language ) {
		return services.getEntityLinkUtilsService().getUserPhotoAlbumPhotosLink( userPhotoAlbum, language );
	}
}
