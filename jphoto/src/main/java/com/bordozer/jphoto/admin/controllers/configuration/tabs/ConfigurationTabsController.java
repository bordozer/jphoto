package com.bordozer.jphoto.admin.controllers.configuration.tabs;

import com.bordozer.jphoto.core.general.configuration.Configuration;
import com.bordozer.jphoto.core.general.configuration.ConfigurationTab;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.SystemConfigurationLoadService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Controller
@RequestMapping("/admin/configuration/{systemConfigurationId}")
public class ConfigurationTabsController {

    public static final String MODEL_NAME = "configurationTabsModel";

    private static final String VIEW = "admin/configuration/tabs/ConfigurationTabs";

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private SystemConfigurationLoadService systemConfigurationLoadService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;

    @Autowired
    private TranslatorService translatorService;

    @ModelAttribute(MODEL_NAME)
    public ConfigurationTabsModel prepareModel(final @PathVariable("systemConfigurationId") int systemConfigurationId) {
        final ConfigurationTabsModel model = new ConfigurationTabsModel();

        final SystemConfiguration systemConfiguration = systemConfigurationLoadService.load(systemConfigurationId);
        model.setSystemConfiguration(systemConfiguration);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tabs/changes/info/")
    public String changesInfo(final @ModelAttribute(MODEL_NAME) ConfigurationTabsModel model) {

        final SystemConfiguration systemConfiguration = model.getSystemConfiguration();

        final Map<String, Configuration> parametersMap = configurationService.getSystemConfigurationParametersMap(systemConfiguration);
        final Map<String, Configuration> changedParametersMap = newLinkedHashMap();
        for (final String key : parametersMap.keySet()) {
            final Configuration configuration = parametersMap.get(key);
            if (!configuration.isGotFromDefaultSystemConfiguration()) {
                changedParametersMap.put(key, configuration);
            }
        }
        model.setConfigurationMap(changedParametersMap);
        model.setConfigurationTab(ConfigurationTab.CHANGES_ONLY);

        model.setRankInGenrePointsMap(userRankService.getUserGenreRankPointsMap(model.getSystemConfiguration()));

        final PageTitleData pageTitleData = breadcrumbsAdminService.getAdminConfigurationInfoBreadcrumbs(systemConfiguration);
        model.getPageModel().setPageTitleData(pageTitleData);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tabs/{tab}/info/")
    public String configurationTabInfo(final @PathVariable("tab") String tab, final @ModelAttribute(MODEL_NAME) ConfigurationTabsModel model) {
        final SystemConfiguration systemConfiguration = model.getSystemConfiguration();

        final ConfigurationTab configurationTab = ConfigurationTab.getByKey(tab);

        final Map<String, Configuration> parametersMap = configurationService.getSystemConfigurationParametersMap(systemConfiguration);
        final Map<String, Configuration> tabParametersMap = newLinkedHashMap();
        for (final String key : parametersMap.keySet()) {
            final Configuration configuration = parametersMap.get(key);
            if (configurationTab == ConfigurationTab.ALL || configuration.getConfigurationKey().getTab() == configurationTab) {
                tabParametersMap.put(key, configuration);
            }
        }
        model.setConfigurationMap(tabParametersMap);

        model.setConfigurationTab(configurationTab);

        model.setRankInGenrePointsMap(userRankService.getUserGenreRankPointsMap(model.getSystemConfiguration()));

        final PageTitleData pageTitleData = breadcrumbsAdminService.getAdminConfigurationInfoTbBreadcrumbs(systemConfiguration, translatorService.translate(configurationTab.getName(), EnvironmentContext.getLanguage()));
        model.getPageModel().setPageTitleData(pageTitleData);

        return VIEW;
    }
}
