package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;

public class BestPhotoListTitle extends AbstractPhotoListTitle {

	public BestPhotoListTitle( final PhotoListCriterias criterias, final Language language, final Services services ) {
		super( criterias, language, services );
	}

	@Override
	public String getPhotoListTitle() {
		return getTitle( "Best photo list title" );
	}

	@Override
	public String getPhotoListDescription() {
		return addDescription( "Best photo list description" );
	}

}
