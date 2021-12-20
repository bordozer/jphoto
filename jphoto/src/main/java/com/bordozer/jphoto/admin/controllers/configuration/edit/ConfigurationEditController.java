package com.bordozer.jphoto.admin.controllers.configuration.edit;

import com.bordozer.jphoto.admin.controllers.configuration.list.SystemConfigurationListController;
import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.configuration.Configuration;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.configuration.ConfigurationTab;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.system.CacheServiceImpl;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.SystemConfigurationLoadService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes(ConfigurationEditController.MODEL_NAME)
@Controller
@RequestMapping("/admin/configuration")
public class ConfigurationEditController {

    public static final String MODEL_NAME = "configurationEditModel";

    private static final String EDIT_DATA_VIEW = "admin/configuration/edit/ConfigurationDataEdit";
    private static final String EDIT_TAB_VIEW = "admin/configuration/edit/ConfigurationEdit";

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ConfigurationValidator configurationValidator;

    @Autowired
    private SystemConfigurationLoadService systemConfigurationLoadService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private CacheServiceImpl cacheService;

    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;

    private final LogHelper log = new LogHelper();

    @InitBinder
    protected void initBinder(final ServletRequestDataBinder binder) {
        binder.setValidator(configurationValidator);
    }

    @ModelAttribute(MODEL_NAME)
    public ConfigurationEditModel prepareModel() {
        return new ConfigurationEditModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new/")
    public String configurationNew(final @ModelAttribute(MODEL_NAME) ConfigurationEditModel model) {

        final SystemConfiguration defaultSystemConfiguration = systemConfigurationLoadService.getDefaultSystemConfiguration();

        final SystemConfiguration systemConfiguration = new SystemConfiguration();
        systemConfiguration.setName(String.format("%s copy", defaultSystemConfiguration.getName()));
        //		systemConfiguration.setDescription( String.format( "%s copy", defaultSystemConfiguration.getCriteriaDescription() ) );
        systemConfiguration.setDefaultConfiguration(false);

        model.setSystemConfiguration(systemConfiguration);
        model.setConfigurationTab(null);

        model.setConfigurationMap(configurationService.getSystemConfigurationParametersMap(systemConfiguration));

        model.getPageModel().setPageTitleData(breadcrumbsAdminService.getAdminConfigurationNewBreadcrumbs());

        model.setRankInGenrePointsMap(getUserGenreRankPointsMap(model));

        return EDIT_DATA_VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{systemConfigurationId}/edit/")
    public String configurationTabEdit(final @PathVariable("systemConfigurationId") int systemConfigurationId, final @ModelAttribute(MODEL_NAME) ConfigurationEditModel model, final HttpServletRequest request) {

        model.setBindingResult(null);

        final String tab = request.getParameter("tab");
        final ConfigurationTab configurationTab = tab == null ? ConfigurationTab.getDefaultConfigurationTab() : ConfigurationTab.getByKey(tab);

        final SystemConfiguration systemConfiguration = systemConfigurationLoadService.load(systemConfigurationId);
        if (systemConfiguration == null) {
            return SystemConfigurationListController.REDIRECT_CONFIGURATION_LINK;
        }

        model.setSystemConfiguration(systemConfiguration);

        model.setConfigurationMap(configurationService.getSystemConfigurationParametersMap(systemConfiguration));

        model.setConfigurationTab(configurationTab);

        model.setRankInGenrePointsMap(getUserGenreRankPointsMap(model));

        model.getPageModel().setPageTitleData(getPageData(model, configurationTab));

        return getView(configurationTab);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{systemConfigurationId}/edit/")
    public String configurationTabEditPost(@Valid final @ModelAttribute(MODEL_NAME) ConfigurationEditModel model, final BindingResult bindingResult) {
        model.setBindingResult(bindingResult);

        cacheService.expire(CacheKey.RANK_IN_GENRE_POINTS_CACHE_ENTRY);

        final ConfigurationTab configurationTab = ConfigurationTab.getByKey(model.getConfigurationTabKey());

        final SystemConfiguration defaultSystemConfiguration = systemConfigurationLoadService.getDefaultSystemConfiguration();

        final String resetConfigurationKey = model.getResetConfigurationKey();
        if (StringUtils.isNotEmpty(resetConfigurationKey)) {
            resetConfigurationKeyValue(model, defaultSystemConfiguration);
            model.setBindingResult(null); // to prevent extra validation of reset key
        }

        for (final String configurationKeyVal : model.getConfigurationMap().keySet()) {
            final ConfigurationKey configurationKey = ConfigurationKey.getById(Integer.parseInt(configurationKeyVal));
            final Configuration configuration = model.getConfigurationMap().get(configurationKeyVal);
            final Configuration defaultConfiguration = defaultSystemConfiguration.getConfiguration(configurationKey);
            if (!configuration.equals(defaultConfiguration)) {
                configuration.setDefaultSystemConfiguration(defaultConfiguration);
                configuration.setGotFromDefaultSystemConfiguration(false);
            }
        }

        if (bindingResult.hasErrors()) {
            return getView(configurationTab);
        }

        model.setRankInGenrePointsMap(getUserGenreRankPointsMap(model));

        model.setConfigurationTab(configurationTab);
        model.getPageModel().setPageTitleData(getPageData(model, configurationTab));

        return getView(configurationTab);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{systemConfigurationId}/save/")
    public String configurationSave(@Valid final @ModelAttribute(MODEL_NAME) ConfigurationEditModel model, final BindingResult bindingResult) {
        model.setBindingResult(bindingResult);

        if (bindingResult.hasErrors()) {
            return EDIT_TAB_VIEW;
        }

        save(model.getSystemConfiguration(), model.getConfigurationMap());

        return SystemConfigurationListController.REDIRECT_CONFIGURATION_LINK;//String.format( "redirect:%s", urlUtilsService.getAdminConfigurationTabsLink( model.getSystemConfiguration().getId() ) );
    }

    private Map<Integer, Integer> getUserGenreRankPointsMap(final ConfigurationEditModel model) {
        final Configuration configuration1 = model.getConfiguration(ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP);
        final Configuration configuration2 = model.getConfiguration(ConfigurationKey.RANK_VOTING_POINTS_COEFFICIENT);

        return userRankService.getUserGenreRankPointsMap(configuration1.getValueInt(), configuration2.getValueFloat());
    }

    private void resetConfigurationKeyValue(final ConfigurationEditModel model, final SystemConfiguration defaultSystemConfiguration) {
        final String resetConfigurationKey = model.getResetConfigurationKey();
        final Configuration configuration = model.getConfigurationMap().get(resetConfigurationKey);
        final ConfigurationKey configurationKey = ConfigurationKey.getById(Integer.parseInt(resetConfigurationKey));
        configuration.setValue(defaultSystemConfiguration.getConfiguration(configurationKey).getValue());
        configuration.setGotFromDefaultSystemConfiguration(true);
        configuration.setDefaultSystemConfiguration(null);
    }

    private PageTitleData getPageData(final ConfigurationEditModel model, final ConfigurationTab configurationTab) {
        final PageTitleData pageTitleData;
        if (configurationTab != null) {
            pageTitleData = breadcrumbsAdminService.getAdminConfigurationEditTabBreadcrumbs(model.getSystemConfiguration().getId(), model.getSystemConfiguration(), configurationTab);
        } else {
            pageTitleData = breadcrumbsAdminService.getAdminConfigurationEditDataBreadcrumbs(model.getSystemConfiguration());
        }
        return pageTitleData;
    }

    private void save(final SystemConfiguration systemConfiguration, final Map<String, Configuration> configurationMap) {

        if (!systemConfiguration.isDefaultConfiguration()) {

            final int defaultConfigurationId = systemConfigurationLoadService.getDefaultSystemConfigurationId();
            final SystemConfiguration defaultSystemConfiguration = systemConfigurationLoadService.load(defaultConfigurationId);
            if (defaultSystemConfiguration == null) {
                throw new BaseRuntimeException(String.format("Default System Configuration id=%d does not exists", defaultConfigurationId));
            }

            final List<Configuration> changedConfigurations = newArrayList();

            for (final Configuration configuration : configurationMap.values()) {
                final Configuration defaultConfiguration = defaultSystemConfiguration.getConfiguration(configuration.getConfigurationKey());
                if (!configuration.isGotFromDefaultSystemConfiguration() || !defaultConfiguration.getValue().equals(configuration.getValue())) {
                    changedConfigurations.add(configuration);
                }
            }

            systemConfiguration.setConfigurations(changedConfigurations);
        } else {
            final List<Configuration> configurations = newArrayList();
            for (final Configuration configuration : configurationMap.values()) {
                configurations.add(configuration);
            }
            systemConfiguration.setConfigurations(configurations);
        }

        systemConfigurationLoadService.save(systemConfiguration);

        if (systemConfiguration.isDefaultConfiguration() || systemConfiguration.isActiveConfiguration()) {
            final int activeSystemConfigurationId = systemConfigurationLoadService.getActiveSystemConfigurationId();
            configurationService.reloadSystemConfiguration(activeSystemConfigurationId);

            log.debug(String.format("Configuration #%d has been reloaded", activeSystemConfigurationId));
        }
    }

    private String getView(final ConfigurationTab configurationTab) {
        if (configurationTab == null) {
            return EDIT_DATA_VIEW;
        }

        return EDIT_TAB_VIEW;
    }
}
