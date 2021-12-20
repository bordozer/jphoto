package com.bordozer.jphoto.ui.controllers.users.password.change;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.controllers.users.edit.UserEditDataModel;
import com.bordozer.jphoto.ui.services.validation.DataRequirementService;

public class ChangeUserPasswordModel extends AbstractGeneralModel {

    public static final String FORM_CONTROL_OLD_PASSWORD = "oldPassword";
    public static final String FORM_CONTROL_NEW_PASSWORD = UserEditDataModel.USER_PASSWORD_FORM_CONTROL;
    public static final String FORM_CONTROL_NEW_PASSWORD_CONFIRMATION = UserEditDataModel.USER_CONFIRM_PASSWORD_FORM_CONTROL;

    private final User user;

    private String oldPassword;
    private String password;
    private String confirmPassword;

    private DataRequirementService dataRequirementService;

    public ChangeUserPasswordModel(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public DataRequirementService getDataRequirementService() {
        return dataRequirementService;
    }

    public void setDataRequirementService(final DataRequirementService dataRequirementService) {
        this.dataRequirementService = dataRequirementService;
    }
}
