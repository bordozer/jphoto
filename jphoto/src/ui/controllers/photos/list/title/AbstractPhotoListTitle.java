package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.translator.Language;

public abstract class AbstractPhotoListTitle {

	protected Language language;

	public AbstractPhotoListTitle( final Language language ) {
		this.language = language;
	}

	public abstract String getPhotoListTitle();

	public static AbstractPhotoListTitle getInstance( final PhotoListCriterias criterias, final boolean isDescription, final Language language ) {

		if ( criterias.isTopBestPhotoList() ) {

			if ( isDescription ) {
				return new BestPhotoListDescription( language );
			}

			return new BestPhotoListTitle( language );
		}

		if ( isDescription ) {
			return new PhotoListDescription( language );
		}

		return new PhotoListTitle( language );
	}
}
