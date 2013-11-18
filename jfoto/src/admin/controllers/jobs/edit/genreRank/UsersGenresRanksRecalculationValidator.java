package admin.controllers.jobs.edit.genreRank;

import admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UsersGenresRanksRecalculationValidator extends SavedJobValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return UsersGenresRanksRecalculationModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final UsersGenresRanksRecalculationModel model = ( UsersGenresRanksRecalculationModel ) target;

		validateJobName( model, errors );
	}
}
