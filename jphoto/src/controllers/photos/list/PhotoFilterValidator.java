package controllers.photos.list;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PhotoFilterValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return PhotoFilterModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoFilterModel model = ( PhotoFilterModel ) target;
	}
}
