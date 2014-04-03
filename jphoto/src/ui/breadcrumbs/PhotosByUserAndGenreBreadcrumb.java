package ui.breadcrumbs;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotosByUserAndGenreBreadcrumb extends AbstractBreadcrumb {

	private User user;
	private Genre genre;

	public PhotosByUserAndGenreBreadcrumb( final User user, final Genre genre, final Services services ) {
		super( services );
		this.user = user;
		this.genre = genre;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getPhotosByUserByGenreLink( user, genre, language );
	}
}
