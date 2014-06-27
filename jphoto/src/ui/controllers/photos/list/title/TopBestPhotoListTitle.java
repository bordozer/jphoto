package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;

public class TopBestPhotoListTitle extends AbstractPhotoListTitle {

	public TopBestPhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public TranslatableMessage getPhotoListTitle() {
		return getTitle( "Top best photo list title" );
	}

	@Override
	public TranslatableMessage getPhotoListDescription() {
		return addDescription( "Top best photo list description" );
	}
}