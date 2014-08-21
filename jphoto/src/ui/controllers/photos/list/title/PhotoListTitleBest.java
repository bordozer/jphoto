package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;

@Deprecated
public class PhotoListTitleBest extends AbstractPhotoListTitle {

	public PhotoListTitleBest( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public TranslatableMessage getPhotoListTitle() {
		return getTitle( "Best photo list title" );
	}

	@Override
	public TranslatableMessage getPhotoListDescription() {
		return addDescription( "Best photo list description" );
	}

}
