package com.bordozer.jphoto.core.general.configuration;

import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SystemConfiguration extends AbstractBaseEntity {

    private String name;
    private String description;
    private boolean defaultConfiguration;
    private boolean activeConfiguration;

    private List<Configuration> configurations = newArrayList();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(final List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public boolean isDefaultConfiguration() {
        return defaultConfiguration;
    }

    public void setDefaultConfiguration(final boolean defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    public boolean isActiveConfiguration() {
        return activeConfiguration;
    }

    public void setActiveConfiguration(final boolean activeConfiguration) {
        this.activeConfiguration = activeConfiguration;
    }

    public Configuration findConfiguration(final ConfigurationKey key) {
        for (final Configuration configuration : configurations) {
            if (configuration.getConfigurationKey().equals(key)) {
                return configuration;
            }
        }
        return null;
    }

    private void clear() {
        configurations.clear();
    }

    public Configuration getConfiguration(final ConfigurationKey configurationKey) {
        for (final Configuration configuration : configurations) {
            if (configuration.getConfigurationKey() == configurationKey) {
                return configuration;
            }
        }
        return null;
    }
}
