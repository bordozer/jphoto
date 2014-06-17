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
		final TranslatableMessage translatableMessage = new TranslatableMessage( "Top best photo list title: TOP best photos", services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}

	@Override
	public String getPhotoListDescription() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "Top best photo list title: description", services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		addMarks( translatableMessage );

		addVotingDateRange( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}
}
