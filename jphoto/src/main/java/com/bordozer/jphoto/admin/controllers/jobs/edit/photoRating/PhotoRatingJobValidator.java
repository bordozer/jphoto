package com.bordozer.jphoto.admin.controllers.jobs.edit.photoRating;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PhotoRatingJobValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return clazz.equals(PhotoRatingJobModel.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PhotoRatingJobModel model = (PhotoRatingJobModel) target;
    }
}
