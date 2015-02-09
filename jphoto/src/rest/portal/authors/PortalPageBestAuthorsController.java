package rest.portal.authors;

import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "portal-page/authors" )
public class PortalPageBestAuthorsController {

	@Autowired
	private UserService userService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/best/{dateFrom}/{dateTo}/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public BestAuthorsModel theBestAuthors( final @PathVariable ( "dateFrom" ) String _dateFrom, final @PathVariable ( "dateFrom" ) String _dateTo ) {

		final Date dateFrom = dateUtilsService.parseDate( _dateFrom );
		final Date dateTo = dateUtilsService.parseDate( _dateTo );

		final BestAuthorsModel model = new BestAuthorsModel();

		model.setTitle( translatorService.translate( "Portal page: Beat authors $1 - $2", EnvironmentContext.getLanguage(), _dateFrom, _dateTo ) );

		return model;
	}
}
