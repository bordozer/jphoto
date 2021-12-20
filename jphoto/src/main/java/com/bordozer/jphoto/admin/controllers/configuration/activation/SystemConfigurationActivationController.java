package com.bordozer.jphoto.admin.controllers.configuration.activation;

import com.bordozer.jphoto.admin.controllers.configuration.list.SystemConfigurationListController;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.SystemConfigurationLoadService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/configuration/activation")
public class SystemConfigurationActivationController {

    public static final String MODEL_NAME = "systemConfigurationActivationModel";

    private static final String EDIT_VIEW = "admin/configuration/activation/ConfigurationActivation";

    @Autowired
    private SystemConfigurationLoadService systemConfigurationLoadService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;

    @ModelAttribute(MODEL_NAME)
    public com.bordozer.jphoto.admin.controllers.configuration.activation.SystemConfigurationActivationModel prepareModel(final HttpServletRequest request) {
        final com.bordozer.jphoto.admin.controllers.configuration.activation.SystemConfigurationActivationModel model = new com.bordozer.jphoto.admin.controllers.configuration.activation.SystemConfigurationActivationModel();

        final String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            final int systemConfigurationId = Integer.parseInt(id);
            model.setSystemConfigurationId(systemConfigurationId);
        }

        final List<SystemConfiguration> systemConfigurations = systemConfigurationLoadService.getAllSystemConfigurations();
        model.setSystemConfigurations(systemConfigurations);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String edit(final @ModelAttribute(MODEL_NAME) com.bordozer.jphoto.admin.controllers.configuration.activation.SystemConfigurationActivationModel model) {
        model.getPageModel().setPageTitleData(breadcrumbsAdminService.setActiveConfigurationBreadcrumbs());

        return EDIT_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String save(final @ModelAttribute(MODEL_NAME) com.bordozer.jphoto.admin.controllers.configuration.activation.SystemConfigurationActivationModel model) {

        final int systemConfigurationId = model.getSystemConfigurationId();

        systemConfigurationLoadService.deactivateSystemConfiguration(systemConfigurationLoadService.getActiveSystemConfigurationId());

        systemConfigurationLoadService.activateSystemConfiguration(systemConfigurationId);

        configurationService.reloadSystemConfiguration(systemConfigurationId);

        return SystemConfigurationListController.REDIRECT_CONFIGURATION_LINK;
    }
}
