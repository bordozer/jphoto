package ui.services.breadcrumbs.items;

import core.general.photo.Photo;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotoNameBreadcrumb extends AbstractBreadcrumb {

	private Photo photo;

	public PhotoNameBreadcrumb( final Photo photo, final Services services ) {
		super( services );
		this.photo = photo;
	}

	@Override
	public String getValue( final Language language ) {
		return photo.getNameEscaped();
	}
}
