package ui.controllers.portalpage;

import core.services.translator.TranslatorService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ui.context.EnvironmentContext;
import ui.elements.PageTitleData;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

@Controller
@RequestMapping( "/" )
public class PortalPageController {

	public static final String VIEW = "portalpage/PortalPage";
	public static final String MODEL_NAME = "portalPageModel";

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private SystemVarsService systemVarsService;

	@ModelAttribute( MODEL_NAME )
	public PortalPageModel prepareModel() {

		final PortalPageModel model = new PortalPageModel();
		model.setTranslatorService( translatorService );

		return model;
	}

	@RequestMapping( "/" )
	public String portalPage( @ModelAttribute( MODEL_NAME ) PortalPageModel model ) {

		final String title = translatorService.translate( BreadcrumbsBuilder.BREADCRUMBS_PORTAL_PAGE, EnvironmentContext.getLanguage() );
		model.getPageModel().setPageTitleData( new PageTitleData( systemVarsService.getProjectName(), title, title ) );

		return VIEW;
	}
}
