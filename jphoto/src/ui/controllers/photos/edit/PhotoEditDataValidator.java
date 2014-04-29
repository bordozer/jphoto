package ui.controllers.photos.edit;

import core.general.configuration.ConfigurationKey;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.utils.ImageFileUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.FormatUtils;
import utils.UserUtils;

public class PhotoEditDataValidator implements Validator {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return PhotoEditDataModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoEditDataModel model = ( PhotoEditDataModel ) target;
		final String photoName = model.getPhotoName();

		validateUserIsLogged( errors );

		validatePhotoName( errors, photoName );

		validateGenreIsSelected( errors, model.getSelectedGenreId() );
	}

	private void validateUserIsLogged( final Errors errors ) {
		if ( ! UserUtils.isCurrentUserLoggedUser() ) {
			errors.reject( translatorService.translate( "Please, login", EnvironmentContext.getLanguage() ), translatorService.translate( "You are not logged in", EnvironmentContext.getLanguage() ) );
		}
	}

	private void validatePhotoName( final Errors errors, final String name ) {

		if ( StringUtils.isEmpty( name ) ) {
			errors.rejectValue( "photoName", translatorService.translate( "$1 should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ) ) );
			return;
		}

		final int photoNameMaxLength = configurationService.getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH );
		if ( name.length() > photoNameMaxLength ) {
			final String mess = translatorService.translate( "$1 ($2) should be less then $3 symbols ( entered $2 symbols)", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ), String.valueOf( photoNameMaxLength ), name, String.valueOf( name.length() ) );
			errors.rejectValue( "photoName", mess );
		}
	}

	private void validateGenreIsSelected( final Errors errors, final int genreId ) {
		if ( genreId == 0 ) {
			errors.rejectValue( "selectedGenreId", translatorService.translate( "Select $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Genre" ) ) );
		}
	}
}
