package com.bordozer.jphoto.admin.controllers.configuration.tabs;

import com.bordozer.jphoto.core.general.configuration.Configuration;
import com.bordozer.jphoto.core.general.configuration.ConfigurationTab;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.ui.elements.PageModel;

import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class ConfigurationTabsModel {

    private SystemConfiguration systemConfiguration;
    private Map<String, Configuration> configurationMap = newLinkedHashMap();
    private ConfigurationTab configurationTab;

    private Map<Integer, Integer> rankInGenrePointsMap;

    private PageModel pageModel = new PageModel();

    public SystemConfiguration getSystemConfiguration() {
        return systemConfiguration;
    }

    public void setSystemConfiguration(final SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public Map<String, Configuration> getConfigurationMap() {
        return configurationMap;
    }

    public void setConfigurationMap(final Map<String, Configuration> configurationMap) {
        this.configurationMap = configurationMap;
    }

    public ConfigurationTab getConfigurationTab() {
        return configurationTab;
    }

    public void setConfigurationTab(final ConfigurationTab configurationTab) {
        this.configurationTab = configurationTab;
    }

    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(final PageModel pageModel) {
        this.pageModel = pageModel;
    }

    public Map<Integer, Integer> getRankInGenrePointsMap() {
        return rankInGenrePointsMap;
    }

    public void setRankInGenrePointsMap(final Map<Integer, Integer> rankInGenrePointsMap) {
        this.rankInGenrePointsMap = rankInGenrePointsMap;
    }
}
