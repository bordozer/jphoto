package ui.controllers.photos.list.title;

import core.services.translator.Language;

public class PhotoListTitle extends AbstractPhotoListTitle {

	public PhotoListTitle( final Language language ) {
		super( language );
	}

	@Override
	public String getPhotoListTitle() {
		return "PhotoListTitle";
	}
}
