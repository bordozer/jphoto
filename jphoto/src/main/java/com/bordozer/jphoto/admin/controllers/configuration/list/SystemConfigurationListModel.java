package com.bordozer.jphoto.admin.controllers.configuration.list;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;

import java.util.List;

public class SystemConfigurationListModel extends AbstractGeneralPageModel {

    private List<SystemConfiguration> systemConfigurations;

    public List<SystemConfiguration> getSystemConfigurations() {
        return systemConfigurations;
    }

    public void setSystemConfigurations(final List<SystemConfiguration> systemConfigurations) {
        this.systemConfigurations = systemConfigurations;
    }
}
