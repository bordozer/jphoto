package rest.admin.restriction;

import core.general.restriction.EntryRestriction;
import core.services.security.RestrictionService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "admin/members/{userId}/restriction/history" )
@Controller
public class UserLockHistoryController {

	public static final int DAYS_IN_MONTH = 30;
	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserRestrictionHistoryEntryDTO> userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {

		final List<EntryRestriction> userRestrictions = restrictionService.loadUserRestrictions( userId );

		final List<UserRestrictionHistoryEntryDTO> result = newArrayList();

		for ( final EntryRestriction userRestriction : userRestrictions ) {
			final UserRestrictionHistoryEntryDTO dto = getUserRestrictionHistoryEntryDTO( userRestriction );

			result.add( dto );
		}

		return result;
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{restrictionHistoryEnreyId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserRestrictionHistoryEntryDTO saveUserTeamMember( @RequestBody final UserRestrictionHistoryEntryDTO restrictionDTO ) {

		restrictionService.deactivate( restrictionDTO.getId(), EnvironmentContext.getCurrentUser(), dateUtilsService.getCurrentTime() );

		return getUserRestrictionHistoryEntryDTO( restrictionService.load( restrictionDTO.getId() ) );
	}

	private UserRestrictionHistoryEntryDTO getUserRestrictionHistoryEntryDTO( final EntryRestriction userRestriction ) {
		final UserRestrictionHistoryEntryDTO dto = new UserRestrictionHistoryEntryDTO();

		dto.setId( userRestriction.getId() );
		dto.setRestrictionName( translatorService.translate( userRestriction.getRestrictionType().getName(), getLanguage() ) );
		dto.setRestrictionIcon( String.format( "%s/%s", urlUtilsService.getSiteImagesPath(), userRestriction.getRestrictionType().getIcon() ) );

		dto.setDateFrom( dateUtilsService.formatDate( userRestriction.getRestrictionTimeFrom() ) );
		dto.setTimeFrom( dateUtilsService.formatTimeShort( userRestriction.getRestrictionTimeFrom() ) );
		dto.setDateTo( dateUtilsService.formatDate( userRestriction.getRestrictionTimeTo() ) );
		dto.setTimeTo( dateUtilsService.formatTimeShort( userRestriction.getRestrictionTimeTo() ) );

		dto.setRestrictionDuration( getFormattedTimeDifference( userRestriction.getRestrictionTimeFrom(), userRestriction.getRestrictionTimeTo() ) );
		dto.setExpiresAfter( getFormattedTimeDifference( dateUtilsService.getCurrentTime(), userRestriction.getRestrictionTimeTo() ) );

		dto.setActive( userRestriction.isActive() );
		dto.setCreatorLink( entityLinkUtilsService.getUserCardLink( userRestriction.getCreator(), getLanguage() ) );
		dto.setCreationDate( dateUtilsService.formatDate( userRestriction.getCreatingTime() ) );
		dto.setCreationTime( dateUtilsService.formatTimeShort( userRestriction.getCreatingTime() ) );

		dto.setCssClass( getCssClass( userRestriction ) );

		dto.setActive( userRestriction.isActive() );
		if ( ! userRestriction.isActive() ) {
			dto.setActive( false );
			dto.setCancellerLink( entityLinkUtilsService.getUserCardLink( userRestriction.getCanceller(), getLanguage() ) );
			dto.setCancellingTime( dateUtilsService.formatDateTime( userRestriction.getCancellingTime() ) );
		}
		return dto;
	}

	/*@RequestMapping( method = RequestMethod.DELETE, value = "/{restrictionHistoryEntryId}" )
	@ResponseBody
	public boolean deleteUserTeamMember( final @PathVariable( "restrictionHistoryEntryId" ) int restrictionHistoryEntryId ) {
		return restrictionService.deactivate( restrictionHistoryEntryId, EnvironmentContext.getCurrentUser(), dateUtilsService.getCurrentTime() );
	}*/

	private String getFormattedTimeDifference( final Date timeFrom, final Date timeTo ) {

		final Date timeBetween = dateUtilsService.getTimeBetween( timeFrom, timeTo );

		if ( lessThenOneDay( timeBetween ) ) {
			return dateUtilsService.formatTime( timeBetween );
		}

		final int fuckingOffset = 3 * 60 * 60 * 1000; // TODO: critical: make this in the proper way

		final long diffInMilliseconds = timeBetween.getTime() + fuckingOffset;
		if ( lessThenOneMonth( timeBetween ) ) {
			final long diffSeconds = diffInMilliseconds / 1000 % 60;
			final long diffMinutes = diffInMilliseconds / ( 60 * 1000 ) % 60;
			final long diffHours = diffInMilliseconds / ( 60 * 60 * 1000 ) % 24;
			final long diffDays = diffInMilliseconds / ( 24 * 60 * 60 * 1000 );

			if ( diffSeconds == 0 && diffMinutes == 0 && diffHours == 0 ) {
				return String.format( "%d days", diffDays );
			}

			return String.format( "%d days %02d:%02d:%02d", diffDays, diffHours, diffMinutes, diffSeconds );
		}

		if ( lessThenOneYear( timeBetween ) ) {
			final long diffMonth = diffInMilliseconds / DAYS_IN_MONTH / 24 / 60 / 60 / 1000;

			return String.format( "~ %d month", diffMonth );
		}

		return String.format( "~ %d years", diffInMilliseconds / 12 / DAYS_IN_MONTH / 24 / 60 / 60 / 1000 );
	}

	private boolean lessThenOneDay( final Date timeBetween ) {
		return timeBetween.getTime() < 24 * 60 * 60 * 1000;
	}

	private boolean lessThenOneMonth( final Date timeBetween ) {
		return timeBetween.getTime() / ( 24 * 60 * 60 * 1000 ) < DAYS_IN_MONTH;
	}

	private boolean lessThenOneYear( final Date timeBetween ) {
		return timeBetween.getTime() / ( 24 * 60 * 60 * 1000 ) < 365;
	}

	private String getCssClass( final EntryRestriction userRestriction ) {

		if ( ! userRestriction.isActive() ) {
			return "block-background-inactive-restriction";
		}

		if ( dateUtilsService.getCurrentTime().getTime() < userRestriction.getRestrictionTimeTo().getTime() ) {
			return "block-background";
		}

		return "";
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
