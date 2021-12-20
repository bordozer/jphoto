package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.configuration.Configuration;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;

import java.util.List;

public interface ConfigurationDao extends BaseEntityDao<SystemConfiguration> {

    List<SystemConfiguration> getAllSystemConfigurations();

    List<Configuration> loadConfigurations(final int systemConfigurationId);

    void deleteSystemConfigurationKeys(final int systemConfigurationId);

    Configuration loadConfiguration(final int systemConfigurationId, final ConfigurationKey configurationKey);

    boolean save(final int systemConfigurationId, final Configuration configuration);

    int getDefaultConfigurationId();

    int getActiveConfigurationId();

    void activateSystemConfiguration(final int systemConfigurationId, final boolean isActive);
}
