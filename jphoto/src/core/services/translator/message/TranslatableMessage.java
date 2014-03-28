package core.services.translator.message;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.security.Services;
import core.services.translator.Language;

import java.util.List;

public class TranslatableMessage {

	private final String nerd;

	private List<AbstractTranslatableMessageUnit> messageUnits;

	private final Services services;

	public TranslatableMessage( final String nerd, final Services services ) {
		this.nerd = nerd;
		this.services = services;
	}

	public TranslatableMessage addString( final String value ) {
		messageUnits.add( new StringUnit( value, services ) );
		return this;
	}

	public TranslatableMessage addLinkToPhotosByGenreUnit( final Genre genre ) {
		messageUnits.add( new LinkToPhotosByGenreUnit( genre, services ) );
		return this;
	}

	public TranslatableMessage addLinkToUserCardUnit( final User user ) {
		messageUnits.add( new LinkToUserCardUnit( user, services ) );
		return this;
	}

	public TranslatableMessage addLinkToPhotosByUserByGenreUnit( final User user, final Genre genre ) {
		messageUnits.add( new LinkToPhotosByUserByGenreUnit( user, genre, services ) );
		return this;
	}

	public String build( final Language language ) {

		String result = nerd;
		int i = 1;
		for ( final AbstractTranslatableMessageUnit messageUnit : messageUnits ) {
			result = result.replace( String.format( "$%d", i++ ), messageUnit.translate( language ) );
		}

		return result;
	}
}
