package core.services.translator.message;

import core.general.genre.Genre;
import core.services.system.Services;
import core.services.translator.Language;

public class GenreNameParameter extends AbstractTranslatableMessageParameter {

	private Genre genre;

	protected GenreNameParameter( final Genre genre, final Services services ) {
		super( services );
		this.genre = genre;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translateGenre( genre, language );
	}
}
