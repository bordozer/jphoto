package admin.controllers.configuration.activation;

import admin.controllers.configuration.list.SystemConfigurationListController;
import core.general.configuration.SystemConfiguration;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.system.ConfigurationService;
import core.services.system.SystemConfigurationLoadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping( "configuration/activation" )
public class SystemConfigurationActivationController {

	public static final String MODEL_NAME = "systemConfigurationActivationModel";

	private static final String EDIT_VIEW = "admin/configuration/activation/ConfigurationActivation";

	@Autowired
	private SystemConfigurationLoadService systemConfigurationLoadService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@ModelAttribute( MODEL_NAME )
	public SystemConfigurationActivationModel prepareModel( final HttpServletRequest request ) {
		final SystemConfigurationActivationModel model = new SystemConfigurationActivationModel();

		final String id = request.getParameter( "id" );
		if ( StringUtils.isNotEmpty( id ) ) {
			final int systemConfigurationId = Integer.parseInt( id );
			model.setSystemConfigurationId( systemConfigurationId );
		}

		final List<SystemConfiguration> systemConfigurations = systemConfigurationLoadService.getAllSystemConfigurations();
		model.setSystemConfigurations( systemConfigurations );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String edit( final @ModelAttribute( MODEL_NAME ) SystemConfigurationActivationModel model ) {
		model.getPageModel().setPageTitleData( pageTitleAdminUtilsService.setActiveConfigurationData() );

		return EDIT_VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String save( final @ModelAttribute( MODEL_NAME ) SystemConfigurationActivationModel model ) {

		final int systemConfigurationId = model.getSystemConfigurationId();

		systemConfigurationLoadService.deactivateSystemConfiguration( systemConfigurationLoadService.getActiveSystemConfigurationId() );

		systemConfigurationLoadService.activateSystemConfiguration( systemConfigurationId );

		configurationService.reloadSystemConfiguration( systemConfigurationId );

		return SystemConfigurationListController.REDIRECT_CONFIGURATION_LINK;
	}
}
