package core.services.translator.message;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TranslatableMessage {

	private final String nerd;

	private List<AbstractTranslatableMessageParameter> messageParameters = newArrayList();

	private final Services services;

	public TranslatableMessage( final Services services ) {
		this.nerd = StringUtils.EMPTY;
		this.services = services;
	}

	public TranslatableMessage( final String nerd, final Services services ) {
		this.nerd = nerd;
		this.services = services;
	}

	public TranslatableMessage addTranslatableMessageParameter( final TranslatableMessage translatableMessage ) {
		messageParameters.add( new TranslatableMessageParameter( translatableMessage, services ) );
		return this;
	}

	public TranslatableMessage string( final String value ) {
		messageParameters.add( new StringParameter( value, services ) );
		return this;
	}

	public TranslatableMessage translatableString( final String value ) {
		messageParameters.add( new StringTranslatableParameter( value, services ) );
		return this;
	}

	/*public TranslatableMessage translatableList( final List<String> value ) {
		messageParameters.add( new StringTranslatableParameter( value, services ) );
		return this;
	}*/

	public TranslatableMessage addIntegerParameter( final int value ) {
		messageParameters.add( new IntegerParameter( value, services ) );
		return this;
	}

	public TranslatableMessage addFloatParameter( final float value ) {
		messageParameters.add( new FloatParameter( value, services ) );
		return this;
	}

	public TranslatableMessage addUserCardLinkParameter( final User user ) {
		messageParameters.add( new UserCardLinkParameter( user, services ) );
		return this;
	}

	public TranslatableMessage addPhotosByGenreLinkParameter( final Genre genre ) {
		messageParameters.add( new PhotosByGenreLinkParameter( genre, services ) );
		return this;
	}

	public TranslatableMessage addPhotosByGenreLinkParameter( final int genreId ) {
		messageParameters.add( new PhotosByGenreLinkParameter( getGenre( genreId ), services ) );
		return this;
	}

	public TranslatableMessage addUserCardLinkParameter( final int userId ) {
		messageParameters.add( new UserCardLinkParameter( getUser( userId ), services ) );
		return this;
	}

	public TranslatableMessage addPhotosByUserByGenreLinkParameter( final User user, final Genre genre ) {
		messageParameters.add( new PhotosByUserByGenreLinkParameter( user, genre, services ) );
		return this;
	}

	public TranslatableMessage addPhotosByUserByGenreLinkParameter( final int userId, final int genreId ) {
		messageParameters.add( new PhotosByUserByGenreLinkParameter( getUser( userId ), getGenre( genreId ), services ) );
		return this;
	}

	public TranslatableMessage addPhotoCardLinkParameter( final Photo photo ) {
		messageParameters.add( new PhotoCardLinkParameter( photo, services ) );
		return this;
	}

	public TranslatableMessage addPhotoCardLinkParameter( final int photoId ) {
		messageParameters.add( new PhotoCardLinkParameter( services.getPhotoService().load( photoId ), services ) );
		return this;
	}

	public TranslatableMessage dateTimeFormatted( final Date actionTime ) {
		messageParameters.add( new FormattedDateTimeParameter( actionTime, services ) );
		return this;
	}

	public TranslatableMessage addPhotoVotingCategoryParameterParameter( final PhotoVotingCategory photoVotingCategory ) {
		messageParameters.add( new PhotoVotingCategoryParameter( photoVotingCategory, services ) );
		return this;
	}

	public String build( final Language language ) {

		final StringBuilder builder = new StringBuilder( services.getTranslatorService().translate( nerd, language ) );

		if ( language == Language.NERD ) {
			return builder.toString();
		}

		int i = 1;
		for ( final AbstractTranslatableMessageParameter messageParameter : messageParameters ) {
			final String parameterPlaceholder = String.format( "$%d", i );

			final int start = builder.indexOf( parameterPlaceholder );
			if ( start >= 0 ) {
				builder.replace( start, start + parameterPlaceholder.length(), messageParameter.getValue( language ) );
			} else {
				builder.append( messageParameter.getValue( language ) );
			}

			i++;
		}

		return builder.toString();
	}

	private User getUser( final int userId ) {
		return services.getUserService().load( userId );
	}

	private Genre getGenre( final int genreId ) {
		return services.getGenreService().load( genreId );
	}
}
