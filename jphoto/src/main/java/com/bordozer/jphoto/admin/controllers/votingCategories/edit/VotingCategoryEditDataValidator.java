package com.bordozer.jphoto.admin.controllers.votingCategories.edit;

import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VotingCategoryEditDataValidator implements Validator {

    @Autowired
    private VotingCategoryService votingCategoryService;
    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return VotingCategoryEditDataModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final VotingCategoryEditDataModel model = (VotingCategoryEditDataModel) target;

        final int votingCategoryId = model.getVotingCategoryId();
        final String votingCategoryName = model.getVotingCategoryName();

        validateName(errors, votingCategoryId, votingCategoryName);
    }

    private void validateName(final Errors errors, final int votingCategoryId, final String votingCategoryName) {

        if (StringUtils.isEmpty(votingCategoryName)) {
            final String errorCode = translatorService.translate("$1 should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Name"));
            errors.rejectValue(VotingCategoryEditDataModel.VOTING_CATEGORIES_NAME_FORM_CONTROL, errorCode);
        }

        final PhotoVotingCategory checkPhotoVotingCategories = votingCategoryService.loadByName(votingCategoryName);
        if (checkPhotoVotingCategories != null && checkPhotoVotingCategories.getId() > 0 && checkPhotoVotingCategories.getId() != votingCategoryId) {
            errors.rejectValue(VotingCategoryEditDataModel.VOTING_CATEGORIES_NAME_FORM_CONTROL
                    , translatorService.translate("$1 ($2) already exists!", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Name"), votingCategoryName), votingCategoryName);
        }
    }
}
