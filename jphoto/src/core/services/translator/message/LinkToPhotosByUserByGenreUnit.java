package core.services.translator.message;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.security.Services;
import core.services.translator.Language;

public class LinkToPhotosByUserByGenreUnit extends AbstractTranslatableMessageUnit {

	private final User user;
	private final Genre genre;

	protected LinkToPhotosByUserByGenreUnit( final User user, final Genre genre, final Services services ) {
		super( services );
		this.user = user;
		this.genre = genre;
	}

	@Override
	public String translate( final Language language ) {
		return getEntityLinkUtilsService().getPhotosByUserByGenreLink( user, genre, language );
	}
}
