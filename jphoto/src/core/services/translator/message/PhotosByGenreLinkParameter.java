package core.services.translator.message;

import core.general.genre.Genre;
import core.services.security.Services;
import core.services.translator.Language;

public class PhotosByGenreLinkParameter extends AbstractTranslatableMessageParameter {

	private Genre genre;

	protected PhotosByGenreLinkParameter( final Genre genre, final Services services ) {
		super( services );
		this.genre = genre;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getPhotosByGenreLink( genre, language );
	}
}
