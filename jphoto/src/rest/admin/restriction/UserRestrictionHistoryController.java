package rest.admin.restriction;

import core.general.restriction.EntryRestriction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "admin/restriction/members/{userId}/history" )
@Controller
public class UserRestrictionHistoryController extends AbstractRestrictionController {

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<RestrictionHistoryEntryDTO> userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {

		final List<EntryRestriction> userRestrictions = restrictionService.loadUserRestrictions( userId );
		Collections.sort( userRestrictions, new Comparator<EntryRestriction>() {
			@Override
			public int compare( final EntryRestriction o1, final EntryRestriction o2 ) {
				return o2.getId() - o1.getId();
			}
		} );

		final List<RestrictionHistoryEntryDTO> result = newArrayList();

		for ( final EntryRestriction userRestriction : userRestrictions ) {
			result.add( getRestrictionHistoryEntryDTO( userRestriction ) );
		}

		return result;
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{restrictionHistoryEntryId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public RestrictionHistoryEntryDTO saveUserTeamMember( @RequestBody final RestrictionHistoryEntryDTO restrictionDTO ) {

		restrictionService.deactivate( restrictionDTO.getId(), EnvironmentContext.getCurrentUser(), dateUtilsService.getCurrentTime() );

		return getRestrictionHistoryEntryDTO( restrictionService.load( restrictionDTO.getId() ) );
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/{restrictionHistoryEntryId}" )
	@ResponseBody
	public boolean deleteUserTeamMember( final @PathVariable( "restrictionHistoryEntryId" ) int restrictionHistoryEntryId ) {
		return restrictionService.delete( restrictionHistoryEntryId );
	}
}
