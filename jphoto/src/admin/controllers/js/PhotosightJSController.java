package admin.controllers.js;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "/js" )
public class PhotosightJSController {

	@RequestMapping( method = RequestMethod.GET, value = "/photosight.js" )
	public String photosight() {
		return "admin/jobs/edit/photosImport/photosight-user-info.js";
	}
}
