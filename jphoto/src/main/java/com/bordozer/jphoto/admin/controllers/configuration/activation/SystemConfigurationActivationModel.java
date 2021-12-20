package com.bordozer.jphoto.admin.controllers.configuration.activation;

import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.ui.elements.PageModel;
import org.springframework.validation.BindingResult;

import java.util.List;

public class SystemConfigurationActivationModel {

    public static final String SYSTEM_CONFIGURATION_ID_FORM_CONTROL = "systemConfigurationId";

    private int systemConfigurationId;
    private List<SystemConfiguration> systemConfigurations;

    private PageModel pageModel = new PageModel();

    private BindingResult bindingResult;

    public int getSystemConfigurationId() {
        return systemConfigurationId;
    }

    public void setSystemConfigurationId(final int systemConfigurationId) {
        this.systemConfigurationId = systemConfigurationId;
    }

    public List<SystemConfiguration> getSystemConfigurations() {
        return systemConfigurations;
    }

    public void setSystemConfigurations(final List<SystemConfiguration> systemConfigurations) {
        this.systemConfigurations = systemConfigurations;
    }

    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(final PageModel pageModel) {
        this.pageModel = pageModel;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(final BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
