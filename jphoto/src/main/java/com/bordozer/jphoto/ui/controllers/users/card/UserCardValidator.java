package com.bordozer.jphoto.ui.controllers.users.card;

import com.bordozer.jphoto.core.general.base.PagingModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserCardValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCardModel.class.equals(clazz) || PagingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
}
