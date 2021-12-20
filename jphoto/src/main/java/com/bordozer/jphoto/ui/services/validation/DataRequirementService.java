package com.bordozer.jphoto.ui.services.validation;

public interface DataRequirementService {

    String HINT_LINE_BREAK = "<br />";

    UserRequirement getUserRequirement();

    PhotoRequirement getPhotoRequirement();

    String getFieldIsMandatoryText();

    String getFieldIsOptionalText();
}
