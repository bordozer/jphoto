package json.photo;

import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.menu.EntryMenuService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

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

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public PhotoEntryDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {

		final Photo photo = photoService.load( photoId );

		final PhotoEntryDTO photoEntry = new PhotoEntryDTO( photoId );

		photoEntry.setGroupOperationCheckbox( getGroupOperationCheckbox( photo ) );
		photoEntry.setPhotoUploadDate( dateUtilsService.formatDateTimeShort( photo.getUploadTime() ) );
		photoEntry.setPhotoCategory( getPhotoCategory( photo.getGenreId() ) );
		photoEntry.setPhotoImage( getPhotoPreview( photo ) );
		photoEntry.setPhotoContextMenu( getPhotoContextMenu( photo ) );
		photoEntry.setPhotoName( entityLinkUtilsService.getPhotoCardLink( photo, getLanguage() ) );
		photoEntry.setPhotoAuthorLink( getPhotoAuthorLink( photo.getUserId() ) );

		setPhotoStatistics( photo, photoEntry );

		setUserRank( photoEntry );

		return photoEntry;
	}

	private void setUserRank( final PhotoEntryDTO photoEntry ) {
		final boolean showUserRank = configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_USER_RANK_IN_GENRE );
		photoEntry.setShowUserRank( showUserRank );
		if ( showUserRank ) {
			photoEntry.setPhotoAuthorRank( getPhotoAuthorRank() );
		}
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

	private String getPhotoContextMenu( final Photo photo ) {
		if ( ! configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_PHOTO_MENU ) ) { // TODO: no photo menu in photo card if it is switched off for photo list!!!!
			return "";
		}

//		final EntryMenu photoMenu = entryMenuService.getPhotoMenu( photo, getCurrentUser() ); // TODO

		return "menu :(";
	}

	private String getPhotoAuthorLink( final int userId ) {
		final User user = userService.load( userId );
		return entityLinkUtilsService.getUserCardLink( user, getLanguage() );
	}

	private String getPhotoAuthorRank() {
		final boolean showUserRank = configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_USER_RANK_IN_GENRE );
		return "rank";
	}

	private User getCurrentUser() {
		return EnvironmentContext.getCurrentUser();
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
