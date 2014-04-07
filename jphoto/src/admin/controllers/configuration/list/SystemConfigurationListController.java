package admin.controllers.configuration.list;

import ui.services.breadcrumbs.BreadcrumbsAdminService;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping ( value = "configuration" )
public class SystemConfigurationListController {

	public static final String MODEL_NAME = "systemConfigurationListModel";

	public static final String REDIRECT_CONFIGURATION_LINK = "redirect:/admin/configuration/";

	private static final String VIEW = "admin/configuration/list/ConfigurationList";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String configurationInfo( final @ModelAttribute( MODEL_NAME ) SystemConfigurationListModel model ) {

		model.setSystemConfigurations( configurationService.getAllSystemConfigurations() );

		model.getPageModel().setPageTitleData( breadcrumbsAdminService.getAdminSystemConfigurationListBreadcrumbs() );

		return VIEW;
	}
}
