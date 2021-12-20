package com.bordozer.jphoto.core.services.system;

import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;

import java.util.List;

public interface SystemConfigurationLoadService extends BaseEntityService<SystemConfiguration> {

    List<SystemConfiguration> getAllSystemConfigurations();

    int getDefaultSystemConfigurationId();

    SystemConfiguration getDefaultSystemConfiguration();

    List<SystemConfiguration> getCustomSystemConfigurations();

    int getActiveSystemConfigurationId();

    void activateSystemConfiguration(final int systemConfigurationId);

    void deactivateSystemConfiguration(final int systemConfigurationId);
}
