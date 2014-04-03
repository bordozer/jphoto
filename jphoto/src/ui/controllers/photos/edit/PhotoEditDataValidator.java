package ui.controllers.photos.edit;

import core.context.EnvironmentContext;
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
		final String photoName = model.getName();

		validateUser( errors );

		if ( model.getCurrentStep() == PhotoEditWizardStep.PHOTO_FILE_UPLOAD ) {
			validateFile( errors, model );
		}

		validateName( errors, photoName );

		validateGenre( errors, model.getGenreId() );
	}

	private void validateUser( final Errors errors ) {
		if ( ! UserUtils.isCurrentUserLoggedUser() ) {
			errors.reject( translatorService.translate( "Please, login", EnvironmentContext.getLanguage() ), translatorService.translate( "You are not logged in", EnvironmentContext.getLanguage() ) );
		}
	}

	private void validateFile( final Errors errors, final PhotoEditDataModel model ) {
		if ( model.getPhotoId() > 0 ) {
			return;
		}

		final User currentUser = EnvironmentContext.getCurrentUser();

		final MultipartFile multipartFile = model.getFile();
		final ConfigurationKey fileMaxSizeKey = currentUser.getUserStatus() == UserStatus.CANDIDATE ? ConfigurationKey.CANDIDATES_FILE_MAX_SIZE_KB : ConfigurationKey.MEMBERS_FILE_MAX_SIZE_KB;
		final long maxFileSizeKb = configurationService.getLong( fileMaxSizeKey );

		final int maxFileWidth = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MAX_WIDTH );
		final int maxFileHeight = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MAX_HEIGHT );
		final Dimension maxDimension = new Dimension( maxFileWidth, maxFileHeight );

		imageFileUtilsService.validateUploadedFile( errors, multipartFile, maxFileSizeKb, maxDimension, PhotoEditDataModel.PHOTO_EDIT_DATA_FILE_FORM_CONTROL, EnvironmentContext.getLanguage() );
	}

	private void validateName( final Errors errors, final String name ) {

		if ( StringUtils.isEmpty( name ) ) {
			errors.rejectValue( PhotoEditDataModel.PHOTO_EDIT_DATA_NAME_FORM_CONTROL, translatorService.translate( "%s should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ) ) );
			return;
		}

		final int photoNameMaxLength = configurationService.getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH );
		if ( name.length() > photoNameMaxLength ) {
			final String mess = translatorService.translate( "$1 ($2) should be less then %d symbols ( entered $2 symbols)", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ), String.valueOf( photoNameMaxLength ), name, String.valueOf( name.length() ) );
			errors.rejectValue( PhotoEditDataModel.PHOTO_EDIT_DATA_NAME_FORM_CONTROL, mess );
		}
	}

	private void validateGenre( final Errors errors, final int genreId ) {
		if ( genreId == 0 ) {
			errors.rejectValue( PhotoEditDataModel.PHOTO_EDIT_DATA_GENRE_ID_FORM_CONTROL, translatorService.translate( "Select $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Genre" ) ) );
		}
	}
}
