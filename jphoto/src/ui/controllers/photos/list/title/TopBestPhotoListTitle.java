package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;

public class TopBestPhotoListTitle extends AbstractPhotoListTitle {

	public TopBestPhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public String getPhotoListTitle() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "TOP best photos", services );

		if ( criterias.getUser() != null ) {
//			translatableMessage.string(  )
		}

		return translatableMessage.build( getLanguage() );
	}
}
