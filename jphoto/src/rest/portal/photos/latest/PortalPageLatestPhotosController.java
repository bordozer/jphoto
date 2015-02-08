package rest.portal.photos.latest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "portal-page" )
public class PortalPageLatestPhotosController {

	@RequestMapping( method = RequestMethod.GET, value = "/photos/latest/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public LatestPhotosDTO theLatestPhotos( final LatestPhotosDTO dto ) {
		return dto;
	}
}
