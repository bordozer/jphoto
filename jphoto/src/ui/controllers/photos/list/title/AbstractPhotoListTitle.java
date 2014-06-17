package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;
import ui.context.EnvironmentContext;

public abstract class AbstractPhotoListTitle {

	protected PhotoListCriterias criterias;
	protected Services services;

	public abstract String getPhotoListTitle();

	public AbstractPhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		this.criterias = criterias;
		this.services = services;
	}

	public static AbstractPhotoListTitle getInstance( final PhotoListCriterias criterias, final boolean isDescription, final Language language, final Services services ) {

		if ( criterias.isTopBestPhotoList() ) {

			if ( isDescription ) {
				return new TopBestPhotoListDescription( criterias, services );
			}

			return new TopBestPhotoListTitle( criterias, services );
		}

		if ( isDescription ) {
			return new PhotoListDescription( criterias, services );
		}

		return new PhotoListTitle( criterias, services );
	}

	public static AbstractPhotoListTitle getEmptyPhotoListTitle() {
		return new EmptyPhotoListTitle();
	}

	public Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
