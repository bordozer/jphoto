package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;

public class TopBestPhotoListDescription extends AbstractPhotoListTitle {

	public TopBestPhotoListDescription( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public String getPhotoListTitle() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "Top best photo list description: Photos", services );

		if ( criterias.getGenre() != null ) {
			translatableMessage.string( " " ).translatableString( "in category" ).string( " " ).addGenreNameParameter( criterias.getGenre() );
		}

		return translatableMessage.build( getLanguage() );
	}
}
