package controllers.photos.groupoperations;

import core.general.photo.group.PhotoGroupOperationType;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserTeamService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.TranslatorUtils;

import java.util.List;

public class PhotoGroupOperationValidator implements Validator {

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private UserTeamService userTeamService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return PhotoGroupOperationModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoGroupOperationModel model = ( PhotoGroupOperationModel ) target;

		validateGroupOperationSelected( model, errors );

		validateAtLeastOnePhotoSelected( model, errors );

		validateMoveToGenreSelected( model, errors );
	}

	void validateGroupOperationSelected( final PhotoGroupOperationModel model, final Errors errors ) {
		final String groupOperationId = model.getPhotoGroupOperationId();

		if ( StringUtils.isEmpty( groupOperationId ) ) {
			errors.reject( TranslatorUtils.translate( "Validation error" ), TranslatorUtils.translate( "Please, select group operation" ) );
			return;
		}

		final PhotoGroupOperationType groupOperationType = PhotoGroupOperationType.getById( Integer.parseInt( groupOperationId ) );
		if ( groupOperationType == null ) {
			errors.reject( TranslatorUtils.translate( "Validation error" ), TranslatorUtils.translate( "Group operation is unknown" ) );
		}
	}

	void validateAtLeastOnePhotoSelected( final PhotoGroupOperationModel model, final Errors errors ) {
		final List<String> selectedPhotoIds = model.getSelectedPhotoIds();
		if ( selectedPhotoIds == null || selectedPhotoIds.size() == 0 ) {
			errors.reject( TranslatorUtils.translate( "Validation error" ), TranslatorUtils.translate( "Please, select at least one photo" ) );
		}
	}

	private void validateMoveToGenreSelected( final PhotoGroupOperationModel model, final Errors errors ) {
		final PhotoGroupOperationType groupOperationType = model.getPhotoGroupOperationType();

		if ( groupOperationType == null ) {
			return;
		}

		if ( groupOperationType != PhotoGroupOperationType.MOVE_TO_GENRE ) {
			return;
		}

		if ( model.getMoveToGenreId() == 0 ) {
			errors.rejectValue( PhotoGroupOperationModel.FORM_CONTROL_MOVE_TO_GENRE_ID, TranslatorUtils.translate( String.format( "Select %s to move to.", FormatUtils.getFormattedFieldName( "genre" ) ) ) );
		}
	}
}
