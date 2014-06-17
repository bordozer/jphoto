package ui.controllers.photos.list.title;

import core.services.translator.Language;

public class BestPhotoListTitle extends AbstractPhotoListTitle {

	public BestPhotoListTitle( final Language language ) {
		super( language );
	}

	@Override
	public String getPhotoListTitle() {
		return "BestPhotoListTitle";
	}
}
