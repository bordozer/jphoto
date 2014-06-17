package ui.controllers.photos.list.title;

import core.services.translator.Language;

public class PhotoListDescription extends AbstractPhotoListTitle {

	public PhotoListDescription( final Language language ) {
		super( language );
	}

	@Override
	public String getPhotoListTitle() {
		return "PhotoListDescription";
	}
}
