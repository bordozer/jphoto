package ui.controllers.photos.edit;

import core.general.configuration.ConfigurationKey;
import core.general.img.Dimension;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.utils.ImageFileUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import ui.context.EnvironmentContext;
import utils.FormatUtils;
import utils.UserUtils;

public class PhotoEditDataValidator implements Validator {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

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

		if ( ! model.isNew() ) {
			validateFile( errors, model );
		}

		validateName( errors, photoName );

		validateGenre( errors, model.getSelectedGenreId() );
	}

	private void validateUserIsLogged( final Errors errors ) {
		if ( ! UserUtils.isCurrentUserLoggedUser() ) {
			errors.reject( translatorService.translate( "Please, login", EnvironmentContext.getLanguage() ), translatorService.translate( "You are not logged in", EnvironmentContext.getLanguage() ) );
		}
	}

	private void validateFile( final Errors errors, final PhotoEditDataModel model ) {

		final User currentUser = EnvironmentContext.getCurrentUser();

		final MultipartFile multipartFile = model.getPhotoFile();
		final ConfigurationKey fileMaxSizeKey = currentUser.getUserStatus() == UserStatus.CANDIDATE ? ConfigurationKey.CANDIDATES_FILE_MAX_SIZE_KB : ConfigurationKey.MEMBERS_FILE_MAX_SIZE_KB;
		final long maxFileSizeKb = configurationService.getLong( fileMaxSizeKey );

		final int maxFileWidth = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MAX_WIDTH );
		final int maxFileHeight = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MAX_HEIGHT );
		final Dimension maxDimension = new Dimension( maxFileWidth, maxFileHeight );

		imageFileUtilsService.validateUploadedFile( errors, multipartFile, maxFileSizeKb, maxDimension, "photoFile", EnvironmentContext.getLanguage() );
	}

	private void validateName( final Errors errors, final String name ) {

		if ( StringUtils.isEmpty( name ) ) {
			errors.rejectValue( "photoName", translatorService.translate( "%s should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ) ) );
			return;
		}

		final int photoNameMaxLength = configurationService.getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH );
		if ( name.length() > photoNameMaxLength ) {
			final String mess = translatorService.translate( "$1 ($2) should be less then %d symbols ( entered $2 symbols)", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ), String.valueOf( photoNameMaxLength ), name, String.valueOf( name.length() ) );
			errors.rejectValue( "photoName", mess );
		}
	}

	private void validateGenre( final Errors errors, final int genreId ) {
		if ( genreId == 0 ) {
			errors.rejectValue( "selectedGenreId", translatorService.translate( "Select $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Genre" ) ) );
		}
	}
}
