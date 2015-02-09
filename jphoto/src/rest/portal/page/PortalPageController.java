package rest.portal.page;

import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "portal-page" )
public class PortalPageController {

	@Autowired
	private DateUtilsService dateUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PortalPageDTO portalPage() {

		final PortalPageDTO dto = new PortalPageDTO();

		dto.setWeekBegin( dateUtilsService.formatDate( dateUtilsService.getFirstSecondOfLastMonday() ) );
		dto.setWeekEnd( dateUtilsService.formatDate( dateUtilsService.getLastSecondOfNextSunday() ) );

		dto.setMonthBegin( dateUtilsService.formatDate( dateUtilsService.getFirstSecondOfMonth() ) );
		dto.setMonthEnd( dateUtilsService.formatDate( dateUtilsService.getLastSecondOfMonth() ) );

		return dto;
	}
}
