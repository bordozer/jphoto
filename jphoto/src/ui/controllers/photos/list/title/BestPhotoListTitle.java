package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;

public class BestPhotoListTitle extends AbstractPhotoListTitle {

	public BestPhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
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
