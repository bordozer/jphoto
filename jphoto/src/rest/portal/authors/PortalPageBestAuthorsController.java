package rest.portal.authors;

import core.general.configuration.ConfigurationKey;
import core.general.data.UserRating;
import core.general.user.User;
import core.services.photo.PhotoVotingService;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "portal-page/authors" )
public class PortalPageBestAuthorsController {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping( method = RequestMethod.GET, value = "/best/{dateFrom}/{dateTo}/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public BestAuthorsModel theBestAuthors( final @PathVariable ( "dateFrom" ) String _dateFrom, final @PathVariable ( "dateTo" ) String _dateTo ) {

		final Date dateFrom = dateUtilsService.parseDate( _dateFrom );
		final Date dateTo = dateUtilsService.parseDate( _dateTo );

		final BestAuthorsModel model = new BestAuthorsModel();

		model.setTitle( translatorService.translate( "Portal page: Beat authors $1 - $2", EnvironmentContext.getLanguage(), _dateFrom, _dateTo ) );

		final List<UserRating> users = photoVotingService.getUserRatingForPeriod( dateFrom, dateTo, configurationService.getInt( ConfigurationKey.PORTAL_PAGE_BEST_AUTHORS_RATING_LENGTH ) );

		final List<AuthorDTO> authors = newArrayList();
		for ( final UserRating userRating : users ) {
			final AuthorDTO dto = new AuthorDTO();

			final User user = userRating.getUser();

			dto.setUserId( user.getId() );
			dto.setUserCardLink( entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ) );

			dto.setRating( userRating.getRating() );

			authors.add( dto );
		}

		model.setAuthorDTOs( authors );

		return model;
	}
}
