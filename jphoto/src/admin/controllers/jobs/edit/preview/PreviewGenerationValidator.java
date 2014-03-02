package admin.controllers.jobs.edit.preview;

import admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.NumberUtils;
import utils.TranslatorUtils;

public class PreviewGenerationValidator extends SavedJobValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return PreviewGenerationModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PreviewGenerationModel model = ( PreviewGenerationModel ) target;

		final int previewSize = NumberUtils.convertToInt( model.getPreviewSize() );
		validatePictureFolder( previewSize, errors );

		validateJobName( model, errors );
	}

	private void validatePictureFolder( final int previewSize, final Errors errors ) {
		if ( previewSize <= 0 ) {
			errors.rejectValue( PreviewGenerationModel.PREVIEW_SIZE_FORM_CONTROL,
								TranslatorUtils.translate( String.format( "Enter %s.", FormatUtils.getFormattedFieldName( "Preview size" ) ) ) );
		}
	}
}
