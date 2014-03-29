package core.services.translator.message;

import core.general.photo.Photo;
import core.services.security.Services;
import core.services.translator.Language;

public class LinkToPhotoCardUnit extends AbstractTranslatableMessageUnit {

	private Photo photo;

	protected LinkToPhotoCardUnit( final Photo photo, final Services services ) {
		super( services );
		this.photo = photo;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getPhotoCardLink( photo, language );
	}
}
