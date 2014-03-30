package core.services.translator.message;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import core.services.translator.Language;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TranslatableMessage {

	private final String nerd;

	private List<AbstractTranslatableMessageUnit> messageUnits = newArrayList();

	private final Services services;

	public TranslatableMessage( final String nerd, final Services services ) {
		this.nerd = nerd;
		this.services = services;
	}

	public TranslatableMessage addStringUnit( final String value ) {
		messageUnits.add( new StringUnit( value, services ) );
		return this;
	}

	public TranslatableMessage addStringTranslatableUnit( final String value ) {
		messageUnits.add( new StringTranslatableUnit( value, services ) );
		return this;
	}

	public TranslatableMessage addIntegerUnit( final int value ) {
		messageUnits.add( new IntegerUnit( value, services ) );
		return this;
	}

	public TranslatableMessage addPhotosByGenreLinkUnit( final Genre genre ) {
		messageUnits.add( new LinkToPhotosByGenreUnit( genre, services ) );
		return this;
	}

	public TranslatableMessage addPhotosByGenreLinkUnit( final int genreId ) {
		messageUnits.add( new LinkToPhotosByGenreUnit( services.getGenreService().load( genreId ), services ) );
		return this;
	}

	public TranslatableMessage addUserCardLinkUnit( final User user ) {
		messageUnits.add( new LinkToUserCardUnit( user, services ) );
		return this;
	}

	public TranslatableMessage addUserCardLinkUnit( final int userId ) {
		messageUnits.add( new LinkToUserCardUnit( services.getUserService().load( userId ), services ) );
		return this;
	}

	public TranslatableMessage addPhotosByUserByGenreLinkUnit( final User user, final Genre genre ) {
		messageUnits.add( new LinkToPhotosByUserByGenreUnit( user, genre, services ) );
		return this;
	}

	public TranslatableMessage addPhotoCardLinkUnit( final Photo photo ) {
		messageUnits.add( new LinkToPhotoCardUnit( photo, services ) );
		return this;
	}

	public TranslatableMessage addFormattedDateTimeUnit( final Date actionTime ) {
		messageUnits.add( new FormattedDateTimeUnit( actionTime, services ) );
		return this;
	}

	public String build( final Language language ) {

		String result = services.getTranslatorService().translate( nerd, language );
		int i = 1;
		for ( final AbstractTranslatableMessageUnit messageUnit : messageUnits ) {
			result = result.replace( String.format( "$%d", i++ ), messageUnit.getValue( language ) );
		}

		return result;
	}
}
