package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

public class PhotoGalleryTitle extends AbstractPhotoListTitle {

	public PhotoGalleryTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public TranslatableMessage getPhotoListTitle() {
		return new TranslatableMessage( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services );
	}

	@Override
	public TranslatableMessage getPhotoListDescription() {
		return new TranslatableMessage( "Photo gallery title: descriptions", services );
	}
}
