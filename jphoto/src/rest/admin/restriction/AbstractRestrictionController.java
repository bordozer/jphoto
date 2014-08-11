package rest.admin.restriction;

import core.general.restriction.EntryRestriction;
import core.services.security.RestrictionService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;

import java.util.Date;

public abstract class AbstractRestrictionController {

	public static final int DAYS_IN_MONTH = 30;

	@Autowired
	protected RestrictionService restrictionService;

	@Autowired
	protected DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	protected String getFormattedTimeDifference( final Date timeFrom, final Date timeTo ) {

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

	protected RestrictionHistoryEntryDTO getRestrictionHistoryEntryDTO( final EntryRestriction restriction ) {
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

	protected long offset() {
		return dateUtilsService.getTimeZoneOffset();
	}

	protected boolean lessThenOneDay( final Date timeBetween ) {
		return timeBetween.getTime() + offset() < 24 * 60 * 60 * 1000;
	}

	protected boolean lessThenOneMonth( final Date timeBetween ) {
		return timeBetween.getTime() / ( 24 * 60 * 60 * 1000 ) < DAYS_IN_MONTH;
	}

	protected boolean lessThenOneYear( final Date timeBetween ) {
		return timeBetween.getTime() / ( 24 * 60 * 60 * 1000 ) < 365;
	}

	protected void initCssClassAndStatus( final EntryRestriction restriction, final RestrictionHistoryEntryDTO dto ) {

		if ( ! restriction.isActive() ) {
			dto.setStatus( translatorService.translate( "Restriction history entry status: cancelled", getLanguage() ) );
			dto.setCssClass( "block-background-inactive-restriction" );
			return;
		}

		if ( restriction.getRestrictionTimeFrom().getTime() > dateUtilsService.getCurrentTime().getTime() ) {
			dto.setStatus( translatorService.translate( "Restriction history entry status: postponed", getLanguage() ) );
			dto.setCssClass( "block-background-postponed-restriction" );
			return;
		}

		if ( ! isFinished( restriction ) ) {
			dto.setStatus( translatorService.translate( "Restriction history entry status: in progress", getLanguage() ) );
			dto.setCssClass( "block-background" );
			return;
		}

		dto.setStatus( translatorService.translate( "Restriction history entry status: passed", getLanguage() ) );
		dto.setStatus( translatorService.translate( "", getLanguage() ) );
	}

	protected boolean isFinished( final EntryRestriction restriction ) {
		return dateUtilsService.getCurrentTime().getTime() > restriction.getRestrictionTimeTo().getTime();
	}

	protected Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
