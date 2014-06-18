package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotoListTitle extends AbstractPhotoListTitle {

	public PhotoListTitle( final PhotoListCriterias criterias, final Language language, final Services services ) {
		super( criterias, language, services );
	}

	@Override
	public String getPhotoListTitle() {
		return getTitle( "Photo list title" );
	}

	@Override
	public String getPhotoListDescription() {
		return addDescription( "Photo list description" );
	}
}
