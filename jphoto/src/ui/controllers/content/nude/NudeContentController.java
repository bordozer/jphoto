package ui.controllers.content.nude;

import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.Environment;
import ui.context.EnvironmentContext;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping( "/nudeContent" )
public class NudeContentController {

	private static final String MODEL_NAME = "nudeContentModel";

	@Autowired
	private UrlUtilsService urlUtilsService;

	@ModelAttribute( MODEL_NAME )
	public NudeContentModel prepareModel( final HttpServletRequest request ) {
		final NudeContentModel model = new NudeContentModel();

		model.setRedirectToIfAcceptUrl( request.getParameter( "redirectToIfAcceptUrl" ) );
		model.setRedirectToIfDeclineUrl( request.getParameter( "redirectToIfDeclineUrl" ) );
		model.setViewingNudeContent( request.getParameter( "IConfirmShowingNudeContent" ) != null );

		return model;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String processNudeContentShowingChoice( final @ModelAttribute( MODEL_NAME ) NudeContentModel model, final HttpServletRequest request  ) {
		if ( ! model.isViewingNudeContent() ) {
			//return String.format( "redirect:%s", model.getRedirectToIfDeclineUrl() );
			return String.format( "redirect:%s", urlUtilsService.getAllPhotosLink() );
		}

		final Environment env = EnvironmentContext.getEnvironment();
		env.setShowNudeContent( true );
		EnvironmentContext.setEnv( env );

		return String.format( "redirect:%s%s", urlUtilsService.getBaseURL(), model.getRedirectToIfAcceptUrl() );
	}
}
