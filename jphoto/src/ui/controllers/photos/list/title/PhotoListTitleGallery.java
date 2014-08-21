package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

@Deprecated
public class PhotoListTitleGallery extends AbstractPhotoListTitle {

	public PhotoListTitleGallery( final PhotoListCriterias criterias, final Services services ) {
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
