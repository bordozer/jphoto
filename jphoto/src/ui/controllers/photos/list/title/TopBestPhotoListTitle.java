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

		if ( criterias.getUser() != null ) {
			translatableMessage.string( " " ).translatableString( "Top best photo list title: of member" ).string( " " ).addUserCardLinkParameter( criterias.getUser() );
		}

		if ( criterias.getGenre() != null ) {
			translatableMessage.string( " " ).translatableString( "Top best photo list title: in category" ).string( " " ).addGenreNameParameter( criterias.getGenre() );
		}

		return translatableMessage.build( getLanguage() );
	}
}
