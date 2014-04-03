package ui.breadcrumbs;

import core.general.photo.Photo;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotoCardBreadcrumb extends AbstractBreadcrumb {

	private Photo photo;

	public PhotoCardBreadcrumb( final Photo photo, final Services services ) {
		super( services );
		this.photo = photo;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getPhotoCardLink( photo, language );
	}
}
