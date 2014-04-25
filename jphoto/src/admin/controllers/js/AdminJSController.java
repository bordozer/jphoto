package admin.controllers.js;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "/js" )
public class AdminJSController {

	@RequestMapping( method = RequestMethod.GET, value = "/photosight.js" )
	public String photosightUserInfo() {
		return "admin/js/photosight-user-info.js";
	}

	@RequestMapping( method = RequestMethod.GET, value = "/job-execution-progress.js" )
	public String jobExecutionProgress() {
		return "admin/js/job-execution-progress.js";
	}
}
