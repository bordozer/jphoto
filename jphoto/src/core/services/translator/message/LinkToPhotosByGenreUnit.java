package core.services.translator.message;

import core.general.genre.Genre;
import core.services.security.Services;
import core.services.translator.Language;

public class LinkToPhotosByGenreUnit extends AbstractTranslatableMessageUnit {

	private Genre genre;

	protected LinkToPhotosByGenreUnit( final Genre genre, final Services services ) {
		super( services );
		this.genre = genre;
	}

	@Override
	public String translate( final Language language ) {
		return getEntityLinkUtilsService().getPhotosByGenreLink( genre, language );
	}
}
