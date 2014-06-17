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
		final TranslatableMessage translatableMessage = new TranslatableMessage( "Best photo list title", services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}

	@Override
	public String getPhotoListDescription() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "Best photo list description", services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		addMarks( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}
}
