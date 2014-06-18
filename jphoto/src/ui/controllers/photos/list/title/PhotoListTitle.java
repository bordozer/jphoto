package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;

public class PhotoListTitle extends AbstractPhotoListTitle {

	public PhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public TranslatableMessage getPhotoListTitle() {
		return getTitle( "Photo list title" );
	}

	@Override
	public TranslatableMessage getPhotoListDescription() {
		return addDescription( "Photo list description" );
	}
}
