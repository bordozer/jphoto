package controllers.photos.groupoperations;

import core.context.EnvironmentContext;
import core.general.photo.group.PhotoGroupOperationType;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;

import java.util.List;

public class PhotoGroupOperationValidator implements Validator {

	public static final int NO_GENRE_SELECTED = -1;

	@Autowired
	private TranslatorService translatorService;

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
			errors.reject( translatorService.translate( "Validation error", getLanguage() ), translatorService.translate( "Please, select group operation" , getLanguage() ) );
			return;
		}

		final PhotoGroupOperationType groupOperationType = PhotoGroupOperationType.getById( Integer.parseInt( groupOperationId ) );
		if ( groupOperationType == null ) {
			errors.reject( translatorService.translate( "Validation error", getLanguage() ), translatorService.translate( "Group operation is unknown", getLanguage() ) );
		}
	}

	void validateAtLeastOnePhotoSelected( final PhotoGroupOperationModel model, final Errors errors ) {
		final List<String> selectedPhotoIds = model.getSelectedPhotoIds();
		if ( selectedPhotoIds == null || selectedPhotoIds.size() == 0 ) {
			errors.reject( translatorService.translate( "Validation error", getLanguage() ), translatorService.translate( "Please, select at least one photo", getLanguage() ) );
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

		if ( model.getMoveToGenreId() == NO_GENRE_SELECTED ) {
			errors.rejectValue( PhotoGroupOperationModel.FORM_CONTROL_MOVE_TO_GENRE_ID, translatorService.translate( "Select %s to move to.", getLanguage(), FormatUtils.getFormattedFieldName( "genre" ) ) );
		}
	}

	private static Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
