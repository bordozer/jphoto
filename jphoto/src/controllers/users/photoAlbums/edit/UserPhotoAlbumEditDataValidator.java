package controllers.users.photoAlbums.edit;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.TranslatorUtils;

public class UserPhotoAlbumEditDataValidator implements Validator {

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
				, TranslatorUtils.translate( String.format( "%s should not be empty.", FormatUtils.getFormattedFieldName( "Album name" ) ) ) );
		}
	}
}
