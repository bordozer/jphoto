package admin.controllers.jobs.edit.rankVoting;

import admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RankVotingJobValidator extends SavedJobValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return RankVotingJobModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final RankVotingJobModel model = ( RankVotingJobModel ) target;

		validateJobName( model, errors );

		validateActionsQty( model.getActionsQty(), errors );
	}

	private void validateActionsQty( final String actionsQty, final Errors errors ) {
		validateNonZeroPositiveNumber( actionsQty, errors, "Actions qty", RankVotingJobModel.ACTIONS_QTY_FORM_CONTROL );
	}
}
