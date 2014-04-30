package json.photo.appraisal;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

public class ValidationHelper {

	public static <T> void validate( T bean, Validator validator ) {

		final BindingResult bindingResult = new BeanPropertyBindingResult( bean, "validated" );
		validator.validate( bean, bindingResult );

		if ( bindingResult.hasErrors() ) {
			throw new ValidationException( bean, bindingResult );
		}
	}
}
