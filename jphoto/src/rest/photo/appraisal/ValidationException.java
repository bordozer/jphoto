package rest.photo.appraisal;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
public class ValidationException extends RuntimeException {

	private final BindingResult bindingResult;
	private final Object bean;

	public ValidationException( final Object bean, final BindingResult bindingResult ) {

		super( String.format( "Validation failed for %s", bean ) );

		this.bindingResult = bindingResult;
		this.bean = bean;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public Object getBean() {
		return bean;
	}
}
