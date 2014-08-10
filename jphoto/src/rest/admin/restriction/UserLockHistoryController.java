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

import java.util.Collections;
import java.util.Comparator;
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
		Collections.sort( userRestrictions, new Comparator<EntryRestriction>() {
			@Override
			public int compare( final EntryRestriction o1, final EntryRestriction o2 ) {
				return o2.getId() - o1.getId();
			}
		} );

		final List<UserRestrictionHistoryEntryDTO> result = newArrayList();

		for ( final EntryRestriction userRestriction : userRestrictions ) {
			result.add( getUserRestrictionHistoryEntryDTO( userRestriction ) );
		}

		return result;
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{restrictionHistoryEnreyId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserRestrictionHistoryEntryDTO saveUserTeamMember( @RequestBody final UserRestrictionHistoryEntryDTO restrictionDTO ) {

		restrictionService.deactivate( restrictionDTO.getId(), EnvironmentContext.getCurrentUser(), dateUtilsService.getCurrentTime() );

		return getUserRestrictionHistoryEntryDTO( restrictionService.load( restrictionDTO.getId() ) );
	}

	private UserRestrictionHistoryEntryDTO getUserRestrictionHistoryEntryDTO( final EntryRestriction restriction ) {
		final UserRestrictionHistoryEntryDTO dto = new UserRestrictionHistoryEntryDTO();

		dto.setId( restriction.getId() );
		dto.setRestrictionName( translatorService.translate( restriction.getRestrictionType().getName(), getLanguage() ) );
		dto.setRestrictionIcon( String.format( "%s/%s", urlUtilsService.getSiteImagesPath(), restriction.getRestrictionType().getIcon() ) );

		dto.setDateFrom( dateUtilsService.formatDate( restriction.getRestrictionTimeFrom() ) );
		dto.setTimeFrom( dateUtilsService.formatTimeShort( restriction.getRestrictionTimeFrom() ) );
		dto.setDateTo( dateUtilsService.formatDate( restriction.getRestrictionTimeTo() ) );
		dto.setTimeTo( dateUtilsService.formatTimeShort( restriction.getRestrictionTimeTo() ) );

		dto.setRestrictionDuration( getFormattedTimeDifference( restriction.getRestrictionTimeFrom(), restriction.getRestrictionTimeTo() ) );
		dto.setExpiresAfter( getFormattedTimeDifference( dateUtilsService.getCurrentTime(), restriction.getRestrictionTimeTo() ) );

		dto.setActive( restriction.isActive() );
		dto.setCreatorLink( entityLinkUtilsService.getUserCardLink( restriction.getCreator(), getLanguage() ) );
		dto.setCreationDate( dateUtilsService.formatDate( restriction.getCreatingTime() ) );
		dto.setCreationTime( dateUtilsService.formatTimeShort( restriction.getCreatingTime() ) );

		dto.setCssClass( getCssClass( restriction ) );

		dto.setActive( restriction.isActive() );
		dto.setFinished( isFinished( restriction ) );

		if ( ! dto.isActive() ) {
			dto.setActive( false );
			dto.setCancellerLink( entityLinkUtilsService.getUserCardLink( restriction.getCanceller(), getLanguage() ) );
			dto.setCancellingDate( dateUtilsService.formatDate( restriction.getCancellingTime() ) );
			dto.setCancellingTime( dateUtilsService.formatTimeShort( restriction.getCancellingTime() ) );
			dto.setWasRestricted( getFormattedTimeDifference( restriction.getRestrictionTimeFrom(), restriction.getCancellingTime() ) );

			dto.setExpiresAfter( "" );
		}

		if ( dto.isFinished() ) {
			dto.setExpiresAfter( "" );
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

		final long diffInMilliseconds = timeBetween.getTime() + offset();
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

	private int offset() {
		return 3 * 60 * 60 * 1000; // TODO: critical: make this in the proper way
	}

	private boolean lessThenOneDay( final Date timeBetween ) {
		return timeBetween.getTime() + offset() < 24 * 60 * 60 * 1000;
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

		if ( ! isFinished( userRestriction ) ) {
			return "block-background";
		}

		return "";
	}

	private boolean isFinished( final EntryRestriction userRestriction ) {
		return dateUtilsService.getCurrentTime().getTime() > userRestriction.getRestrictionTimeTo().getTime();
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
