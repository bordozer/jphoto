package rest.admin.restriction;

import core.general.restriction.EntryRestriction;
import core.services.security.RestrictionService;
import core.services.translator.Language;
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

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "admin/members/{userId}/restriction/history" )
@Controller
public class UserLockHistoryController {

	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserRestrictionHistoryEntryDTO> userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {

		final List<EntryRestriction> userRestrictions = restrictionService.loadUserRestrictions( userId );

		final List<UserRestrictionHistoryEntryDTO> result = newArrayList();

		for ( final EntryRestriction userRestriction : userRestrictions ) {
			final UserRestrictionHistoryEntryDTO dto = new UserRestrictionHistoryEntryDTO();

			dto.setId( userRestriction.getId() );
			dto.setRestrictionName( translatorService.translate( userRestriction.getRestrictionType().getName(), getLanguage() ) );

			dto.setDateFrom( dateUtilsService.formatDate( userRestriction.getRestrictionTimeFrom() ) );
			dto.setTimeFrom( dateUtilsService.formatTimeShort( userRestriction.getRestrictionTimeFrom() ) );
			dto.setDateTo( dateUtilsService.formatDate( userRestriction.getRestrictionTimeTo() ) );
			dto.setTimeTo( dateUtilsService.formatTimeShort( userRestriction.getRestrictionTimeTo() ) );

			dto.setActive( userRestriction.isActive() );
			dto.setCreatorLink( entityLinkUtilsService.getUserCardLink( userRestriction.getCreator(), getLanguage() ) );
			dto.setCreationDate( dateUtilsService.formatDate( userRestriction.getCreatingTime() ) );
			dto.setCreationTime( dateUtilsService.formatTimeShort( userRestriction.getCreatingTime() ) );

			if ( userRestriction.getCanceller() != null ) {
				dto.setCancellerLink( entityLinkUtilsService.getUserCardLink( userRestriction.getCanceller(), getLanguage() ) );
				dto.setCancellingTime( dateUtilsService.formatDateTime( userRestriction.getCancellingTime() ) );
			}

			result.add( dto );
		}

		return result;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
