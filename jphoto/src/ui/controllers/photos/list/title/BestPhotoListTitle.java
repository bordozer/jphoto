package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;

public class BestPhotoListTitle extends AbstractPhotoListTitle {

	public BestPhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public String getPhotoListTitle() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "The best photos", services );

		if ( criterias.getUser() != null ) {
//			translatableMessage.string(  )
		}

		return translatableMessage.build( getLanguage() );
	}

	@Override
	public String getPhotoListDescription() {
		return null;
	}
}
