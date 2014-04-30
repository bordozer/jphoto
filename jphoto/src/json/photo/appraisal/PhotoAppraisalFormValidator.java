package json.photo.appraisal;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PhotoAppraisalFormValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return clazz.isAssignableFrom( PhotoAppraisalDTO.class );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoAppraisalDTO dto = ( PhotoAppraisalDTO ) target;
	}
}
