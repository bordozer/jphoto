package admin.controllers.jobs.edit.favorites;

import admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.TranslatorUtils;

public class FavoritesJobValidator extends SavedJobValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return FavoritesJobModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final FavoritesJobModel model = ( FavoritesJobModel ) target;

		validateJobName( model, errors );

		validateActionQty( model, errors );

		validateFavoriteEntries( model, errors );
	}

	private void validateActionQty( final FavoritesJobModel model, final Errors errors ) {
		validatePositiveNumber( model.getActionsQty(), errors, FavoritesJobModel.ACTIONS_QTY_FORM_CONTROL, "Actions" );
	}

	private void validateFavoriteEntries( final FavoritesJobModel model, final Errors errors ) {
		if ( model.getFavoriteEntriesIds() == null || model.getFavoriteEntriesIds().size() == 0 ) {
			errors.rejectValue( FavoritesJobModel.FAVORITE_ENTRIES_IDS_FORM_CONTROL, TranslatorUtils.translate( String.format( "Select at least one checkbox of %s", FormatUtils.getFormattedFieldName( "Favorite entries" ) ) ) );
		}
	}
}
