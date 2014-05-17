package admin.controllers.configuration.list;

import core.general.configuration.SystemConfiguration;
import core.services.system.ConfigurationService;
import core.services.system.SystemConfigurationLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.services.breadcrumbs.BreadcrumbsAdminService;

@Controller
@RequestMapping ( value = "configuration" )
public class SystemConfigurationListController {

	public static final String MODEL_NAME = "systemConfigurationListModel";

	public static final String REDIRECT_CONFIGURATION_LINK = "redirect:/admin/configuration/";

	public static final String VIEW = "admin/configuration/list/ConfigurationList";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@Autowired
	private SystemConfigurationLoadService systemConfigurationLoadService;

	@ModelAttribute( MODEL_NAME )
	public SystemConfigurationListModel prepareModel() {
		final SystemConfigurationListModel model = new SystemConfigurationListModel();

		model.setSystemConfigurations( systemConfigurationLoadService.getAllSystemConfigurations() );

		model.getPageModel().setPageTitleData( breadcrumbsAdminService.getAdminSystemConfigurationListBreadcrumbs() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String configurationList( final @ModelAttribute( MODEL_NAME ) SystemConfigurationListModel model ) {
		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{systemConfigurationId}/delete/" )
	public String deleteConfiguration( final @PathVariable( "systemConfigurationId" ) int systemConfigurationId, final @ModelAttribute( MODEL_NAME ) SystemConfigurationListModel model ) {

		final SystemConfiguration systemConfiguration = systemConfigurationLoadService.load( systemConfigurationId );

		final boolean defaultConfiguration = systemConfiguration.isDefaultConfiguration();
		final boolean activeConfiguration = systemConfiguration.isActiveConfiguration();

		// TODO: show error message
		/*if ( defaultConfiguration ) {
			final String error = translatorService.translate( "This is the default system configuration. It can not be deleted.", EnvironmentContext.getLanguage() );
			EnvironmentContext.getEnvironment().setHiMessage( error );
		}

		if ( activeConfiguration ) {
			final String error = translatorService.translate( "This is the active system configuration. It can not be deleted. Activate another configuration first.", EnvironmentContext.getLanguage() );
			EnvironmentContext.getEnvironment().setHiMessage( error );
		}*/

		if ( ! activeConfiguration && ! defaultConfiguration ) {
			systemConfigurationLoadService.delete( systemConfigurationId );
			return REDIRECT_CONFIGURATION_LINK;
		}

		return VIEW;
	}
}
