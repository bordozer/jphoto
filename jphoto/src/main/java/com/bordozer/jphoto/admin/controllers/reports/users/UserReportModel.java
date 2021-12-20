package com.bordozer.jphoto.admin.controllers.reports.users;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;

import java.util.Date;
import java.util.Map;

public class UserReportModel extends AbstractGeneralPageModel {

    private Map<Date, UserRegistrationData> registrationsMap;

    public void setRegistrationsMap(final Map<Date, UserRegistrationData> registrationsMap) {
        this.registrationsMap = registrationsMap;
    }

    public Map<Date, UserRegistrationData> getRegistrationsMap() {
        return registrationsMap;
    }
}
