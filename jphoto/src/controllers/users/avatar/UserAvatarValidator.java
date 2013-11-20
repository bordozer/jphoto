package controllers.users.avatar;

import core.general.configuration.ConfigurationKey;
import core.general.img.Dimension;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import core.services.utils.ImageFileUtilsService;

public class UserAvatarValidator implements Validator {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return UserAvatarModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final UserAvatarModel model = ( UserAvatarModel ) target;

		validateFile( model, errors );
	}

	private void validateFile( final UserAvatarModel model, final Errors errors ) {
		final MultipartFile multipartFile = model.getAvatarFile();
		final long maxFileSizeKb = configurationService.getLong( ConfigurationKey.PHOTO_UPLOAD_AVATAR_MAX_SIZE_KB );

		final int maxFileWidth = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_AVATAR_MAX_WIDTH );
		final int maxFileHeight = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_AVATAR_MAX_HEIGHT );
		final Dimension maxDimension = new Dimension( maxFileWidth, maxFileHeight );

		imageFileUtilsService.validateUploadedFile( errors, multipartFile, maxFileSizeKb, maxDimension, UserAvatarModel.AVATAR_FILE_FORM_CONTROL );
	}
}
