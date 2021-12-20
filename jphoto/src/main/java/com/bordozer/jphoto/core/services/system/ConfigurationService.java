package com.bordozer.jphoto.core.services.system;

import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.general.configuration.Configuration;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;

import java.util.List;
import java.util.Map;

public interface ConfigurationService {

    String BEAN_NAME = "configurationService";

    List<SystemConfiguration> getAllSystemConfigurations();

    Map<String, Configuration> getSystemConfigurationParametersMap(final SystemConfiguration systemConfiguration);

    void reloadSystemConfiguration(final int systemConfigurationId);

    Configuration getConfiguration(final ConfigurationKey key);

    String getString(final ConfigurationKey key);

    int getInt(final ConfigurationKey key);

    long getLong(final ConfigurationKey key);

    float getFloat(final ConfigurationKey key);

    boolean getBoolean(final ConfigurationKey key);

    List<String> getListString(final ConfigurationKey key);

    List<Integer> getListInt(final ConfigurationKey key);

    List<PhotoActionAllowance> getAccessiblePhotoCommentAllowance();

    List<PhotoActionAllowance> getAccessiblePhotoVotingAllowance();
}
