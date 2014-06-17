package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

public class PhotoListTitle extends AbstractPhotoListTitle {

	public PhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public String getPhotoListTitle() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services );



		return translatableMessage.build( getLanguage() );
	}

	@Override
	public String getPhotoListDescription() {
		return null;
	}
}
