package ui.breadcrumbs;

import core.general.genre.Genre;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotosByGenreBreadcrumb extends AbstractBreadcrumb {

	private Genre genre;

	public PhotosByGenreBreadcrumb( final Genre genre, final Services services ) {
		super( services );
		this.genre = genre;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getPhotosByGenreLink( genre, language );
	}
}
