package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;

public class PhotoGalleryBreadcrumb extends AbstractBreadcrumb {

	public PhotoGalleryBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, language );
	}
}
