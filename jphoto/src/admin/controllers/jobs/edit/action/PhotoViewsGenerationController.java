package admin.controllers.jobs.edit.action;

import admin.jobs.enums.SavedJobType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes( {PhotoActionGenerationController.JOB_MODEL_NAME} )
@Controller
@RequestMapping( "jobs/data/previews" )
public class PhotoViewsGenerationController extends PhotoActionGenerationController {

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( @ModelAttribute( JOB_MODEL_NAME ) PhotoActionGenerationModel model ) {
		return doShowForm( model, SavedJobType.ACTIONS_GENERATION_VIEWS );
	}
}
