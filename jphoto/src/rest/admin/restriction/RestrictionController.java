package rest.admin.restriction;

import admin.controllers.restriction.entry.RestrictionEntryType;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import core.enums.RestrictionStatus;
import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.general.user.UserAvatar;
import core.services.security.RestrictionService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;
import ui.translatable.GenericTranslatableEntry;
import ui.translatable.GenericTranslatableList;

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

	@RequestMapping( method = RequestMethod.GET, value = "/history/members/{userId}/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<RestrictionHistoryEntryDTO> showUserRestriction( final @PathVariable( "userId" ) int userId ) {
		return getRestrictionHistoryEntryDTOs( restrictionService.loadUserRestrictions( userId ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/history/photos/{photoId}/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<RestrictionHistoryEntryDTO> showPhotoRestriction( final @PathVariable( "photoId" ) int photoId ) {
		return getRestrictionHistoryEntryDTOs( restrictionService.loadPhotoRestrictions( photoId ) );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/history/{restrictionHistoryEntryId}/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public RestrictionHistoryEntryDTO inactivateRestriction( @RequestBody final RestrictionHistoryEntryDTO restrictionDTO ) {
		restrictionService.deactivate( restrictionDTO.getId(), EnvironmentContext.getCurrentUser(), dateUtilsService.getCurrentTime() );
		return getRestrictionHistoryEntryDTO( restrictionService.load( restrictionDTO.getId() ) );
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/history/{restrictionHistoryEntryId}/" )
	@ResponseBody
	public boolean deleteRestriction( final @PathVariable( "restrictionHistoryEntryId" ) int restrictionHistoryEntryId ) {
		return restrictionService.delete( restrictionHistoryEntryId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/search/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public RestrictionFilterFormDTO showEmptySearchForm( final FilterDTO filterFormDTO ) {

		final List<String> selectedRestrictionTypeIds = filterFormDTO.getSelectedRestrictionTypeIds();
		final List<String> restrictionStatusIds = filterFormDTO.getRestrictionStatusIds();

		final RestrictionFilterFormDTO dto = new RestrictionFilterFormDTO();

		final List<RestrictionType> restrictionTypesToShow = Lists.transform( selectedRestrictionTypeIds, new Function<String, RestrictionType>() {
			@Override
			public RestrictionType apply( final String restrictionTypeId ) {
				return RestrictionType.getById( restrictionTypeId );
			}
		} );

		final List<RestrictionStatus> restrictionStatusesToShow = Lists.transform( restrictionStatusIds, new Function<String, RestrictionStatus>() {
			@Override
			public RestrictionStatus apply( final String restrictionStatusId ) {
				return RestrictionStatus.getById( restrictionStatusId );
			}
		} );

		final int userId = filterFormDTO.getUserId();
		final Date time = dateUtilsService.getCurrentTime();

		final List<EntryRestriction> restrictions = restrictionService.load( restrictionTypesToShow );
		CollectionUtils.filter( restrictions, new Predicate<EntryRestriction>() {
			@Override
			public boolean evaluate( final EntryRestriction restriction ) {
				return restrictionStatusesToShow.contains( restrictionService.getRestrictionStatus( restriction, time ) )
					&& ( userId == 0 || restriction.getEntry().getId() == userId )
					;
			}
		} );

		dto.setSearchResultEntryDTOs( getRestrictionHistoryEntryDTOs( restrictions ) );

		final List<GenericTranslatableEntry> userEntries = GenericTranslatableList.restrictionUserTranslatableList( EnvironmentContext.getLanguage(), translatorService ).getEntries();
		final List<GenericTranslatableEntry> photoEntries = GenericTranslatableList.restrictionPhotosTranslatableList( EnvironmentContext.getLanguage(), translatorService ).getEntries();
		final List<GenericTranslatableEntry> statusEntries = GenericTranslatableList.restrictionStatusList( EnvironmentContext.getLanguage(), translatorService ).getEntries();

		dto.setRestrictionTypesUser( Lists.transform( userEntries, function( selectedRestrictionTypeIds ) ) );
		dto.setRestrictionTypesPhoto( Lists.transform( photoEntries, function( selectedRestrictionTypeIds ) ) );
		dto.setRestrictionStatuses( Lists.transform( statusEntries, function( restrictionStatusIds ) ) );

		return dto;
	}

	private static Function<GenericTranslatableEntry, CheckboxDTO> function( final List<String> selectedRestrictionTypeIds ) {
		return new Function<GenericTranslatableEntry, CheckboxDTO>() {
			@Override
			public CheckboxDTO apply( final GenericTranslatableEntry translatableEntry ) {
				final CheckboxDTO checkboxDTO = new CheckboxDTO();

				checkboxDTO.setValue( String.valueOf( translatableEntry.getId() ) );
				checkboxDTO.setLabel( translatableEntry.getName() );
				checkboxDTO.setChecked( isChecked( translatableEntry, selectedRestrictionTypeIds ) );

				return checkboxDTO;
			}

			private boolean isChecked( final GenericTranslatableEntry translatableEntry, final List<String> selectedRestrictionTypeIds ) {
				for ( final String selectedRestrictionTypeId : selectedRestrictionTypeIds ) {
					if ( selectedRestrictionTypeId.equals( String.valueOf( translatableEntry.getId() ) ) ) {
						return true;
					}
				}
				return false;
			}
		};
	}

	private RestrictionHistoryEntryDTO getRestrictionHistoryEntryDTO( final EntryRestriction restriction ) {
		final RestrictionHistoryEntryDTO dto = new RestrictionHistoryEntryDTO();

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

		initEntrySpecificData( restriction, dto );

		final RestrictionStatus restrictionStatus = restrictionService.getRestrictionStatus( restriction, dateUtilsService.getCurrentTime() );
		dto.setStatus( translatorService.translate( restrictionStatus.getName(), getLanguage() ) );
		dto.setCssClass( getCssClass( restrictionStatus ) );

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

	private void initEntrySpecificData( final EntryRestriction restriction, final RestrictionHistoryEntryDTO dto ) {
		switch ( restriction.getRestrictionEntryType() ) {
			case USER:
				final User user = ( User ) restriction.getEntry();

				dto.setEntryLink( entityLinkUtilsService.getUserCardLink( user, getLanguage() ) );
				dto.setEntryLinkUrl( urlUtilsService.getUserCardLink( user.getId() ) );

				final UserAvatar userAvatar = userService.getUserAvatar( user.getId() );
				dto.setEntryImage( userAvatar.getUserAvatarFileUrl() );
				dto.setRestrictionEntryTypeName( translatorService.translate( RestrictionEntryType.USER.getName(), getLanguage() ) );

				break;
			case PHOTO:
				final Photo photo = ( Photo ) restriction.getEntry();

				dto.setEntryLink( entityLinkUtilsService.getPhotoCardLink( photo, getLanguage() ) );
				dto.setEntryLinkUrl( urlUtilsService.getPhotoCardLink( photo.getId() ) );

				dto.setEntryImage( userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
				dto.setRestrictionEntryTypeName( translatorService.translate( RestrictionEntryType.PHOTO.getName(), getLanguage() ) );

				break;
		}
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

	private String getCssClass( final RestrictionStatus restrictionStatus ) {

		switch ( restrictionStatus ) {
			case CANCELLED:
				return "block-background-inactive-restriction";
			case POSTPONED:
				return "block-background-postponed-restriction";
			case PROGRESS:
				return "block-background";
			case PASSED:
				return StringUtils.EMPTY;
		}

		throw new IllegalArgumentException( String.format( "Illegal RestrictionStatus: %s", restrictionStatus ) );
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
