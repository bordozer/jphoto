package controllers.users.photoAlbums.edit;

import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;

public class UserPhotoAlbumEditDataValidator implements Validator {

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return UserPhotoAlbumEditDataModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final UserPhotoAlbumEditDataModel model = ( UserPhotoAlbumEditDataModel ) target;

		validateName( model, errors );
	}

	private void validateName( final UserPhotoAlbumEditDataModel model, final Errors errors ) {
		if ( StringUtils.isEmpty( model.getAlbumName() ) ) {
			errors.rejectValue( UserPhotoAlbumEditDataModel.FORM_CONTROL_PHOTO_ALBUM_NAME
				, translatorService.translate( String.format( "%s should not be empty.", FormatUtils.getFormattedFieldName( "Album name" ) ) ) );
		}
	}
}
