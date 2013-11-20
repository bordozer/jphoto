package admin.controllers.jobs.edit.photoRating;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PhotoRatingJobValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return clazz.equals( PhotoRatingJobModel.class );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoRatingJobModel model = ( PhotoRatingJobModel ) target;
	}
}
