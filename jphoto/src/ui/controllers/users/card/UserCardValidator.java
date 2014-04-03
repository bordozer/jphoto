package ui.controllers.users.card;

import core.general.base.PagingModel;
import org.springframework.validation.Errors;

public class UserCardValidator implements org.springframework.validation.Validator {

	@Override
	public boolean supports( Class<?> clazz ) {
		return UserCardModel.class.equals( clazz ) || PagingModel.class.equals( clazz );
	}

	@Override
	public void validate( Object target, Errors errors ) {
	}
}
