package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;

public class PhotoListDescription extends AbstractPhotoListTitle {

	public PhotoListDescription( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public String getPhotoListTitle() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "The photo list description", services );



		return translatableMessage.build( getLanguage() );
	}
}
