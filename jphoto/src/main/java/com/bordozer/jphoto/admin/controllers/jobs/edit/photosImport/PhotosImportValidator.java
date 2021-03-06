package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import com.bordozer.jphoto.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.IntegerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.List;

@Component
public class PhotosImportValidator extends SavedJobValidator implements Validator {

    @Autowired
    private VotingCategoryService votingCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return PhotosImportModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PhotosImportModel model = (PhotosImportModel) target;

        validateJobName(model, errors);

        validatePhotoCategoriesExist(errors);

        validateThatAtLeastOneUserExists(errors);

        final PhotosImportSource importSource = model.getImportSource();

        switch (importSource) {
            case FILE_SYSTEM:
                validatePictureFolder(model.getPictureDir(), errors);

                validateDates(model.getDateFrom(), model.getDateTo(), errors);

                validateImageQty(model.getPhotoQtyLimit(), errors);

                break;
            case PHOTOSIGHT:
            case PHOTO35:
            case NATURELIGHT:
                validatePhotosightUserIds(model.getImportSource(), model.getRemotePhotoSiteUserIds(), errors);

                validatePhotoSightCategories(model, errors);

                validateGender(model, errors);

                validateMembershipType(model, errors);

                validateDelay(model.getDelayBetweenRequest(), errors);
                break;
            default:
                throw new IllegalArgumentException(String.format("Illegal PhotosImportSource: %s", importSource));
        }
    }

    private void validatePhotoSightCategories(final PhotosImportModel model, final Errors errors) {
        final List<String> photosightCategories = model.getRemotePhotoSiteCategories();
        if (photosightCategories == null || photosightCategories.isEmpty()) {
            errors.rejectValue("remotePhotoSiteCategories", translatorService.translate("Select at least one $1", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName("Photosight category")));
        }
    }

    private void validatePhotoCategoriesExist(final Errors errors) {
        final List<PhotoVotingCategory> categories = votingCategoryService.loadAll();
        if (categories.size() == 0) {
            errors.reject(translatorService.translate("The system is not configured properly", EnvironmentContext.getLanguage())
                    , translatorService.translate("There are at least three voting categories have to be configured in the system", EnvironmentContext.getLanguage()));
        }
    }

    private void validateThatAtLeastOneUserExists(final Errors errors) {
        final int userQty = userService.getUserCount();
        if (userQty == 0) {
            errors.reject(translatorService.translate("The system is not configured properly", EnvironmentContext.getLanguage())
                    , translatorService.translate("There are no members in the system. Create members manually or run User Generation Job", EnvironmentContext.getLanguage()));
        }
    }

    private void validatePictureFolder(final String previewDir, final Errors errors) {
        if (StringUtils.isEmpty(previewDir)) {
            errors.rejectValue(PhotosImportModel.PICTURE_DIR_FORM_CONTROL, translatorService.translate("Enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Folder with test pictures")));
        }
    }

    private void validateDates(final String dateFrom, final String dateTo, final Errors errors) {
        if (StringUtils.isEmpty(dateFrom)) {
            errors.rejectValue(PhotosImportModel.DATE_FROM_FORM_CONTROL, translatorService.translate("Enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Dater from")));
            return;
        }

        if (StringUtils.isEmpty(dateTo)) {
            errors.rejectValue(PhotosImportModel.DATE_TO_FORM_CONTROL, translatorService.translate("Enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Dater to")));
            return;
        }

        final Date fromDate = dateUtilsService.parseDate(dateFrom);
        final Date toDate = dateUtilsService.parseDate(dateTo);

        if (toDate.getTime() < fromDate.getTime()) {
            errors.rejectValue(PhotosImportModel.DATE_TO_FORM_CONTROL, translatorService.translate("$1 should be more then %s", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Dater to"), FormatUtils.getFormattedFieldName("Dater from")));
        }
    }

    private void validateImageQty(final String photoQtyLimit, final Errors errors) {

        final boolean isEmptyValue = StringUtils.isEmpty(photoQtyLimit) || photoQtyLimit.equals("0");
        if (!isEmptyValue && !IntegerValidator.getInstance().isValid(photoQtyLimit)) {
            errors.rejectValue(PhotosImportModel.PHOTO_QTY_LIMIT_FORM_CONTROL, translatorService.translate("$1 must be a number, an empty string ot 0", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Photo qty")));
        }
    }


    private void validatePhotosightUserIds(final PhotosImportSource importSource, final String photosightUserIds, final Errors errors) {
        final String[] ids = photosightUserIds.split(",");

        switch (importSource) {
            case PHOTOSIGHT:
                for (final String idTxt : ids) {
                    validateNonZeroPositiveNumber(idTxt.trim(), errors, "Photosight user id", PhotosImportModel.FORM_CONTROL_PHOTOSIGHT_USER_ID);
                }
                break;
        }
    }

    private void validateGender(final PhotosImportModel model, final Errors errors) {
        final String _genderId = model.getUserGenderId();
        validateNonZeroPositiveNumber(_genderId, errors, "gender", PhotosImportModel.USER_GENDER_ID_FORM_CONTROL);

        if (errors.hasErrors()) {
            return;
        }

        final int genderId = NumberUtils.convertToInt(_genderId);
        final UserGender userGender = UserGender.getById(genderId);
        if (userGender == null) {
            errors.rejectValue(PhotosImportModel.USER_GENDER_ID_FORM_CONTROL, translatorService.translate("Select $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("gender")));
        }
    }

    private void validateMembershipType(final PhotosImportModel model, final Errors errors) {
        final String _membershipId = model.getUserGenderId();
        validateNonZeroPositiveNumber(_membershipId, errors, "membership type", PhotosImportModel.USER_MEMBERSHIP_ID_FORM_CONTROL);

        if (errors.hasErrors()) {
            return;
        }

        final int membershipId = NumberUtils.convertToInt(_membershipId);
        final UserMembershipType membershipType = UserMembershipType.getById(membershipId);
        if (membershipType == null) {
            errors.rejectValue(PhotosImportModel.USER_MEMBERSHIP_ID_FORM_CONTROL, translatorService.translate("Select $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("membership type")));
        }
    }

    private void validateDelay(final String delayBetweenRequest, final Errors errors) {
        validateZeroOrPositiveNumber(delayBetweenRequest, errors, PhotosImportModel.DELAY_BETWEEN_REQUEST_FORM_CONTROL, "Delay between requests");
    }
}
