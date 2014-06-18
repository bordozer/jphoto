package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

public class PhotoGalleryTitle extends AbstractPhotoListTitle {

	public PhotoGalleryTitle( final PhotoListCriterias criterias, final Language language, final Services services ) {
		super( criterias, language, services );
	}

	@Override
	public String getPhotoListTitle() {
		return new TranslatableMessage( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services ).build( getLanguage() );
	}

	@Override
	public String getPhotoListDescription() {
		return new TranslatableMessage( "Photo gallery title: descriptions", services ).build( getLanguage() );
	}
}
