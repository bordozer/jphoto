package json.photo;

import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.menu.EntryMenuService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;
import ui.userRankIcons.AbstractUserRankIcon;
import ui.userRankIcons.UserRankIconContainer;

import java.util.Date;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoEntryController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private EntryMenuService entryMenuService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private SecurityService securityService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public PhotoEntryDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {

		final Photo photo = photoService.load( photoId );

		final PhotoEntryDTO photoEntry = new PhotoEntryDTO( photoId );

		photoEntry.setGroupOperationCheckbox( getGroupOperationCheckbox( photo ) );
		photoEntry.setPhotoUploadDate( dateUtilsService.formatDateTimeShort( photo.getUploadTime() ) );
		photoEntry.setPhotoCategory( getPhotoCategory( photo.getGenreId() ) );
		photoEntry.setPhotoImage( getPhotoPreview( photo ) );

		setPhotoContextMenu( photo, photoEntry );

		photoEntry.setPhotoName( entityLinkUtilsService.getPhotoCardLink( photo, getLanguage() ) );
		photoEntry.setPhotoAuthorLink( getPhotoAuthorLink( photo ) );

		setPhotoStatistics( photo, photoEntry );

		setUserRank( photo, photoEntry );

		setPhotoAnonymousPeriodExpiration( photo, photoEntry );

		return photoEntry;
	}

	private void setPhotoStatistics( final Photo photo, final PhotoEntryDTO photoEntry ) {
		final boolean showPhotoStatistics = configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_STATISTIC );
		photoEntry.setShowStatistics( showPhotoStatistics );

		if ( !showPhotoStatistics ) {
			return;
		}

		photoEntry.setTodayMarks( getTodayMarks( photo ) );
		photoEntry.setTodayMarksTitle( translatorService.translate( "The photo's today's marks", getLanguage() ) );

		photoEntry.setPeriodMarks( getPeriodMarks( photo ) );
		final int period = configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
		photoEntry.setPeriodMarksTitle( translatorService.translate( "The photo's marks for period from $1 to $2"
			, getLanguage()
			, dateUtilsService.formatDate( dateUtilsService.getDatesOffsetFromCurrentDate( -period ) )
			, dateUtilsService.formatDate( dateUtilsService.getCurrentTime() )
		));

		photoEntry.setTotalMarks( getTotalMarks( photo ) );
		photoEntry.setTotalMarksUrl( urlUtilsService.getPhotoMarksListLink( photo.getId() ) );
		photoEntry.setTotalMarksTitle( translatorService.translate( "The photo's total marks", getLanguage() ) );

	}

	private int getTodayMarks( final Photo photo ) {
		return photoVotingService.getPhotoMarksForPeriod( photo.getId(), dateUtilsService.getFirstSecondOfToday(), dateUtilsService.getLastSecondOfToday() );
	}

	private int getPeriodMarks( final Photo photo ) {
		final int days = configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
		final Date dateFrom = dateUtilsService.getFirstSecondOfTheDayNDaysAgo( days );
		return photoVotingService.getPhotoMarksForPeriod( photo.getId(), dateFrom, dateUtilsService.getLastSecondOfToday() );
	}

	private int getTotalMarks( final Photo photo ) {
		return photoVotingService.getSummaryPhotoMark( photo );
	}

	private String getGroupOperationCheckbox( final Photo photo ) {
		return String.format( "<input type='checkbox' value='%s' />", photo.getId() );
	}

	private String getPhotoCategory( final int genreId ) {
		final Genre genre = genreService.load( genreId );
		return entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() );
	}

	private String getPhotoPreview( final Photo photo ) {
		return String.format( "<a href='%s' title='%s'><img src='%s' class='photo-preview-image'/></a>"
			, urlUtilsService.getPhotoCardLink( photo.getId() )
			, photo.getNameEscaped()
			, userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo )
		);
	}

	private void setPhotoContextMenu( final Photo photo, final PhotoEntryDTO photoEntry ) {
		final boolean showPhotoContextMenu = configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_PHOTO_MENU );
		photoEntry.setShowPhotoContextMenu( showPhotoContextMenu );
		if ( !showPhotoContextMenu ) { // TODO: no photo menu in photo card if it is switched off for photo list!!!!
			return;
		}

//		final EntryMenu photoMenu = entryMenuService.getPhotoMenu( photo, getCurrentUser() ); // TODO

		photoEntry.setPhotoContextMenu( "menu" );
	}

	private String getPhotoAuthorLink( final Photo photo ) {
		final boolean hideAuthorName = securityService.isPhotoAuthorNameMustBeHidden( photo, getCurrentUser() );

		if ( hideAuthorName ) {
			return configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME );
		}

		final User user = userService.load( photo.getUserId() );
		return entityLinkUtilsService.getUserCardLink( user, getLanguage() );
	}

	private void setUserRank( final Photo photo, final PhotoEntryDTO photoEntry ) {
		final boolean showUserRank = configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_USER_RANK_IN_GENRE );
		photoEntry.setShowUserRank( showUserRank );

		if ( !showUserRank ) {
			return;
		}

		final User user = userService.load( photo.getUserId() );
		final UserRankIconContainer iconContainer = userRankService.getUserRankIconContainer( user, photo );
		final StringBuilder builder = new StringBuilder();

		for ( final AbstractUserRankIcon rankIcon : iconContainer.getRankIcons() ) {
			builder.append( String.format( "<img src='%s/%s' height='8px' title='%s'>"
				, urlUtilsService.getSiteImagesPath()
				, rankIcon.getIcon()
				, rankIcon.getTitle()
			));
		}

		photoEntry.setPhotoAuthorRank( builder.toString() );
	}

	private void setPhotoAnonymousPeriodExpiration( final Photo photo, final PhotoEntryDTO photoEntry ) {
		final boolean showAnonymousPeriodExpirationInfo = securityService.isPhotoAuthorNameMustBeHidden( photo, getCurrentUser() );
		photoEntry.setShowAnonymousPeriodExpirationInfo( showAnonymousPeriodExpirationInfo );
		if ( showAnonymousPeriodExpirationInfo ) {
			photoEntry.setShowUserRank( false );
			photoEntry.setPhotoAnonymousPeriodExpirationInfo( translatorService.translate( "Anonymous till $1", getLanguage(), dateUtilsService.formatDateTimeShort( photoService.getPhotoAnonymousPeriodExpirationTime( photo ) ) ) );
		}
	}

	private User getCurrentUser() {
		return EnvironmentContext.getCurrentUser();
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
