package ui.controllers.photos.edit;

import core.general.configuration.ConfigurationKey;
import core.general.img.Dimension;
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

public class PhotoEditFileValidator implements Validator {

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

		validateFile( errors, model );
	}

	private void validateFile( final Errors errors, final PhotoEditDataModel model ) {

		final MultipartFile multipartFile = model.getPhotoFile();

		if ( multipartFile == null || StringUtils.isEmpty( multipartFile.getOriginalFilename() ) ) {
			errors.rejectValue( "photoFile", translatorService.translate( "Please, select a $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "photo file" ) ) );
			return;
		}

		final ConfigurationKey fileMaxSizeKey = model.getPhotoAuthor().getUserStatus() == UserStatus.CANDIDATE ? ConfigurationKey.CANDIDATES_FILE_MAX_SIZE_KB : ConfigurationKey.MEMBERS_FILE_MAX_SIZE_KB;
		final long maxFileSizeKb = configurationService.getLong( fileMaxSizeKey );

		final int minFileWidth = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MUN_WIDTH );
		final int minFileHeight = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MIN_HEIGHT );
		final Dimension minDimension = new Dimension( minFileWidth, minFileHeight );

		final int maxFileWidth = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MAX_WIDTH );
		final int maxFileHeight = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_MAX_HEIGHT );
		final Dimension maxDimension = new Dimension( maxFileWidth, maxFileHeight );

		imageFileUtilsService.validateUploadedFile( errors, multipartFile, maxFileSizeKb, maxDimension, minDimension, "photoFile", EnvironmentContext.getLanguage() );
	}
}
