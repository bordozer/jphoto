package ui.controllers.photos.list.title;

import core.services.translator.Language;

public class BestPhotoListDescription extends AbstractPhotoListTitle {

	public BestPhotoListDescription( final Language language ) {
		super( language );
	}

	@Override
	public String getPhotoListTitle() {
		return "BestPhotoListDescription";
	}
}
