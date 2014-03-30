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

	private List<AbstractTranslatableMessageParameter> messageParameters = newArrayList();

	private final Services services;

	public TranslatableMessage( final String nerd, final Services services ) {
		this.nerd = nerd;
		this.services = services;
	}

	public TranslatableMessage addStringParameter( final String value ) {
		messageParameters.add( new StringParameter( value, services ) );
		return this;
	}

	public TranslatableMessage addStringTranslatableParameter( final String value ) {
		messageParameters.add( new StringTranslatableParameter( value, services ) );
		return this;
	}

	public TranslatableMessage addIntegerParameter( final int value ) {
		messageParameters.add( new IntegerParameter( value, services ) );
		return this;
	}

	public TranslatableMessage addPhotosByGenreLinkParameter( final Genre genre ) {
		messageParameters.add( new PhotosByGenreLinkParameter( genre, services ) );
		return this;
	}

	public TranslatableMessage addPhotosByGenreLinkParameter( final int genreId ) {
		messageParameters.add( new PhotosByGenreLinkParameter( services.getGenreService().load( genreId ), services ) );
		return this;
	}

	public TranslatableMessage addUserCardLinkParameter( final User user ) {
		messageParameters.add( new UserCardLinkParameter( user, services ) );
		return this;
	}

	public TranslatableMessage addUserCardLinkParameter( final int userId ) {
		messageParameters.add( new UserCardLinkParameter( services.getUserService().load( userId ), services ) );
		return this;
	}

	public TranslatableMessage addPhotosByUserByGenreLinkParameter( final User user, final Genre genre ) {
		messageParameters.add( new PhotosByUserByGenreLinkParameter( user, genre, services ) );
		return this;
	}

	public TranslatableMessage addPhotoCardLinkParameter( final Photo photo ) {
		messageParameters.add( new PhotoCardLinkParameter( photo, services ) );
		return this;
	}

	public TranslatableMessage addFormattedDateTimeUnit( final Date actionTime ) {
		messageParameters.add( new FormattedDateTimeParameter( actionTime, services ) );
		return this;
	}

	public String build( final Language language ) {

		String result = services.getTranslatorService().translate( nerd, language );
		int i = 1;
		for ( final AbstractTranslatableMessageParameter messageParameter : messageParameters ) {
			result = result.replace( String.format( "$%d", i++ ), messageParameter.getValue( language ) );
		}

		return result;
	}
}
