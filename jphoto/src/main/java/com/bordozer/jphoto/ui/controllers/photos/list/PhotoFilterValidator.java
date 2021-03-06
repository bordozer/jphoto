package com.bordozer.jphoto.ui.controllers.photos.list;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PhotoFilterValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return PhotoFilterModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PhotoFilterModel model = (PhotoFilterModel) target;
    }
}
