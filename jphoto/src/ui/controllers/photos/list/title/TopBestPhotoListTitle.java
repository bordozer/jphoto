package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;

public class TopBestPhotoListTitle extends AbstractPhotoListTitle {

	public TopBestPhotoListTitle( final PhotoListCriterias criterias, final Language language, final Services services ) {
		super( criterias, language, services );
	}

	@Override
	public String getPhotoListTitle() {
		return getTitle( "Top best photo list title" );
	}

	@Override
	public String getPhotoListDescription() {
		return addDescription( "Top best photo list description" );
	}
}
