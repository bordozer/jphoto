package com.bordozer.jphoto.admin.controllers.genres.edit;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class GenreEditDataValidator implements Validator {

    @Autowired
    private GenreService genreService;
    @Autowired
    private TranslatorService translatorService;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return GenreEditDataModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final GenreEditDataModel model = (GenreEditDataModel) target;

        validateName(errors, model.getGenreId(), model.getGenreName());

        validateAllowedVotingCategories(errors, model.getAllowedVotingCategoryIDs());
    }

    private void validateName(final Errors errors, final int genreId, final String genreName) {

        if (StringUtils.isEmpty(genreName)) {
            errors.rejectValue(GenreEditDataModel.GENRE_EDIT_DATA_NAME_FORM_CONTROL, translatorService.translate("$1 should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Name")));
        }

        final Genre checkGenre = genreService.loadByName(genreName);
        if (checkGenre != null && checkGenre.getId() > 0 && checkGenre.getId() != genreId) {
            errors.rejectValue(GenreEditDataModel.GENRE_EDIT_DATA_NAME_FORM_CONTROL, translatorService.translate("$1 ($2) already exists!", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Name"), genreName), genreName);
        }
    }

    private void validateAllowedVotingCategories(final Errors errors, final List<String> allowedVotingCategoryIDs) {

        final int categoriesCount = configurationService.getInt(ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT);

        if (allowedVotingCategoryIDs == null || allowedVotingCategoryIDs.size() < categoriesCount) {
            errors.rejectValue(GenreEditDataModel.GENRE_EDIT_DATA_ALLOWED_VOTING_CATEGORIES_FORM_CONTROL, translatorService.translate("Check at least $1 $2 or decrease the value in System configuration ( key $3 )"
                    , EnvironmentContext.getLanguage()
                    , String.valueOf(categoriesCount)
                    , FormatUtils.getFormattedFieldName("allowed appraisal categories")
                    , String.valueOf(ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT.getId())
            ));
        }
    }
}
