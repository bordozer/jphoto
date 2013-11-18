package admin.controllers.jobs.edit.membership;

import admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MembershipJobValidator extends SavedJobValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return MembershipJobModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final MembershipJobModel model = ( MembershipJobModel ) target;

		validateJobName( model, errors );
	}
}
