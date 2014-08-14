package rest.admin.restriction;

import admin.controllers.restriction.entry.RestrictionEntryType;
import core.enums.RestrictionStatus;
import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.general.user.UserAvatar;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
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

@RequestMapping( "admin/restrictions" )
@Controller
public class RestrictionController {

	public static final int DAYS_IN_MONTH = 30;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

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

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/members/{userId}/history/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<RestrictionHistoryEntryDTO> showUserRestriction( final @PathVariable( "userId" ) int userId ) {
		return getRestrictionHistoryEntryDTOs( restrictionService.loadUserRestrictions( userId ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/photos/{photoId}/history/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<RestrictionHistoryEntryDTO> showPhotoRestriction( final @PathVariable( "photoId" ) int photoId ) {
		return getRestrictionHistoryEntryDTOs( restrictionService.loadPhotoRestrictions( photoId ) );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/*/{entryId}/history/{restrictionHistoryEntryId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public RestrictionHistoryEntryDTO inactivateRestriction( @RequestBody final RestrictionHistoryEntryDTO restrictionDTO ) {
		restrictionService.deactivate( restrictionDTO.getId(), EnvironmentContext.getCurrentUser(), dateUtilsService.getCurrentTime() );
		return getRestrictionHistoryEntryDTO( restrictionService.load( restrictionDTO.getId() ) );
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/*/{entryId}/history/{restrictionHistoryEntryId}" )
	@ResponseBody
	public boolean deleteRestriction( final @PathVariable( "restrictionHistoryEntryId" ) int restrictionHistoryEntryId ) {
		return restrictionService.delete( restrictionHistoryEntryId );
	}



	@RequestMapping( method = RequestMethod.GET, value = "/search/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<RestrictionHistoryEntryDTO> showEmptySearchForm() {
//		final List<RestrictionType> defaultTypes = RestrictionType.FOR_USERS;
		return getRestrictionHistoryEntryDTOs( restrictionService.loadAll() );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/search/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public RestrictionHistoryEntryDTO doSearch( @RequestBody final RestrictionHistoryEntryDTO restrictionDTO ) {
		return inactivateRestriction( restrictionDTO );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/search/{restrictionHistoryEntryId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public RestrictionHistoryEntryDTO inactivateRestrictionFromSearch( @RequestBody final RestrictionHistoryEntryDTO restrictionDTO ) {
		return inactivateRestriction( restrictionDTO );
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/search/{restrictionHistoryEntryId}" )
	@ResponseBody
	public boolean deleteRestrictionFromSearch( final @PathVariable( "restrictionHistoryEntryId" ) int restrictionHistoryEntryId ) {
		return restrictionService.delete( restrictionHistoryEntryId );
	}

	/*@RequestMapping( method = RequestMethod.GET, value = "/search/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public RestrictionFilterFormDTO showEmptySearchForm() {
		final RestrictionFilterFormDTO dto = new RestrictionFilterFormDTO();

		final List<RestrictionType> defaultTypes = RestrictionType.FOR_USERS;

		dto.setSearchResultEntryDTOs( getRestrictionHistoryEntryDTOs( restrictionService.load( defaultTypes ) ) );
		dto.setSelectedRestrictionTypeIds( Lists.transform( defaultTypes, new Function<RestrictionType, String>() {
			@Override
			public String apply( final RestrictionType restrictionType ) {
				return String.valueOf( restrictionType.getId() );
			}
		} ) );

		return dto;
	}*/



	private RestrictionHistoryEntryDTO getRestrictionHistoryEntryDTO( final EntryRestriction restriction ) {
		final RestrictionHistoryEntryDTO dto = new RestrictionHistoryEntryDTO();

		dto.setId( restriction.getId() );
		dto.setEntryLink( getEntryLink( restriction.getEntry().getId(), restriction.getRestrictionType() ) );
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

		initRestrictionEntryImage( restriction, dto );

		initCssClassAndStatus( restriction, dto );

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

	private void initRestrictionEntryImage( final EntryRestriction restriction, final RestrictionHistoryEntryDTO dto ) {
		switch ( restriction.getRestrictionEntryType() ) {
			case USER:
				final User user = ( User ) restriction.getEntry();
				final UserAvatar userAvatar = userService.getUserAvatar( user.getId() );
				dto.setEntryImage( userAvatar.getUserAvatarFileUrl() );
				dto.setRestrictionEntryTypeName( translatorService.translate( RestrictionEntryType.USER.getName(), getLanguage() ) );

				break;
			case PHOTO:
				final Photo photo = ( Photo ) restriction.getEntry();
				dto.setEntryImage( userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
				dto.setRestrictionEntryTypeName( translatorService.translate( RestrictionEntryType.PHOTO.getName(), getLanguage() ) );

				break;
		}
	}

	private String getEntryLink( final int entryId, final RestrictionType restrictionType ) {

		if ( RestrictionType.FOR_USERS.contains( restrictionType ) ) {
			return entityLinkUtilsService.getUserCardLink( userService.load( entryId ), getLanguage() );
		}

		return entityLinkUtilsService.getPhotoCardLink( photoService.load( entryId ), getLanguage() );
	}

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

	private long offset() {
		return dateUtilsService.getTimeZoneOffset();
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

	private void initCssClassAndStatus( final EntryRestriction restriction, final RestrictionHistoryEntryDTO dto ) {

		if ( ! restriction.isActive() ) {
			dto.setStatus( translatorService.translate( RestrictionStatus.CANCELLED.getName(), getLanguage() ) );
			dto.setCssClass( "block-background-inactive-restriction" );
			return;
		}

		if ( restriction.getRestrictionTimeFrom().getTime() > dateUtilsService.getCurrentTime().getTime() ) {
			dto.setStatus( translatorService.translate( RestrictionStatus.POSTPONED.getName(), getLanguage() ) );
			dto.setCssClass( "block-background-postponed-restriction" );
			return;
		}

		if ( ! isFinished( restriction ) ) {
			dto.setStatus( translatorService.translate( RestrictionStatus.PROGRESS.getName(), getLanguage() ) );
			dto.setCssClass( "block-background" );
			return;
		}

		dto.setStatus( translatorService.translate( RestrictionStatus.PASSED.getName(), getLanguage() ) );
	}

	private boolean isFinished( final EntryRestriction restriction ) {
		return dateUtilsService.getCurrentTime().getTime() > restriction.getRestrictionTimeTo().getTime();
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	private List<RestrictionHistoryEntryDTO> getRestrictionHistoryEntryDTOs( final List<EntryRestriction> restrictions ) {

		Collections.sort( restrictions, new Comparator<EntryRestriction>() {
			@Override
			public int compare( final EntryRestriction o1, final EntryRestriction o2 ) {
				return o2.getId() - o1.getId();
			}
		} );

		final List<RestrictionHistoryEntryDTO> result = newArrayList();

		for ( final EntryRestriction restriction : restrictions ) {
			result.add( getRestrictionHistoryEntryDTO( restriction ) );
		}

		return result;
	}
}
