package ui.controllers.portalpage;

import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.ActivityStreamService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.RandomUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import core.services.utils.sql.PhotoSqlHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.context.EnvironmentContext;
import ui.services.security.SecurityUIService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "/" )
public class PortalPageController {

	public static final String VIEW = "portalpage/PortalPage";
	public static final String MODEL_NAME = "portalPageModel";

	@Autowired
	private PhotoService photoService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoSqlHelperService photoSqlHelperService;

	@Autowired
	private RandomUtilsService randomUtilsService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private TranslatorService translatorService;
	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private SecurityUIService securityUIService;


	@ModelAttribute( MODEL_NAME )
	public PortalPageModel prepareModel() {
		final PortalPageModel model = new PortalPageModel();
		model.setTranslatorService( translatorService );

		return model;
	}

	@RequestMapping( "/" )
	public String portalPage( @ModelAttribute( MODEL_NAME ) PortalPageModel model ) {

		model.setLastUploadedPhotos( getPortalPagePhotos( getLastUploadedPhotos() ) );

		final List<Integer> theBestPhotos = getTheBestPhotos();
		model.setBestPhotos( getPortalPagePhotos( theBestPhotos ) );

		model.setBestPhotosMinMarks( configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_PHOTO_OF_THE_DAY ) );
		model.setBestPhotosPeriod( configurationService.getInt( ConfigurationKey.PHOTO_RATING_PORTAL_PAGE_BEST_PHOTOS_FROM_PHOTOS_THAT_GOT_ENOUGH_MARKS_FOR_N_LAST_DAYS ) );

		final Date currentTime = dateUtilsService.getCurrentTime();

		final Date firstSecondOfLastMonday = dateUtilsService.getFirstSecondOfLastMonday();
		model.setBestWeekUserRating( photoVotingService.getUserRatingForPeriod( firstSecondOfLastMonday, currentTime, PortalPageModel.TOP_BEST_USERS_QTY ) );

		final Date firstSecondOfMonth = dateUtilsService.getFirstSecondOfMonth();
		model.setBestMonthUserRating( photoVotingService.getUserRatingForPeriod( firstSecondOfMonth, currentTime, PortalPageModel.TOP_BEST_USERS_QTY ) );

		final List<PortalPageGenre> portalPageGenres = newArrayList();
		final List<Genre> genres = genreService.loadAll();

		final Date todayFirstSecond = dateUtilsService.getFirstSecondOfToday();
		final Date todayLastSecond = dateUtilsService.getLastSecondOfToday();

		for ( final Genre genre : genres ) {
			final PortalPageGenre portalGenre = new PortalPageGenre();
			portalGenre.setGenre( genre );
			portalGenre.setTotal( photoService.getPhotoQtyByGenre( genre.getId() ) );
			portalGenre.setToday( photoService.getPhotoQtyByGenreForPeriod( genre.getId(), todayFirstSecond, todayLastSecond ) );

			portalPageGenres.add( portalGenre );
		}

		model.setPortalPageGenres( portalPageGenres );
		model.setRandomBestPhotoArrayIndex( randomUtilsService.getRandomInt( 0, theBestPhotos.size() - 1 ) );

		model.setLastActivities( activityStreamService.getLastActivities( configurationService.getInt( ConfigurationKey.SYSTEM_ACTIVITY_PORTAL_PAGE_STREAM_LENGTH ) ) );

		return VIEW;
	}

	private List<PortalPagePhoto> getPortalPagePhotos( final List<Integer> lastUploadedPhotosIds ) {
		final User accessor = EnvironmentContext.getCurrentUser();
		Collections.shuffle( lastUploadedPhotosIds );
		final List<PortalPagePhoto> lastUploadedPhotos = newArrayList();
		for ( final Integer photoId : lastUploadedPhotosIds ) {
			final PortalPagePhoto portalPagePhoto = new PortalPagePhoto();
			final Photo photo = photoService.load( photoId );

			portalPagePhoto.setPhoto( photo );
			portalPagePhoto.setPhotoImgUrl( userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
			portalPagePhoto.setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( securityUIService.isPhotoHasToBeHiddenBecauseOfNudeContent( photo, accessor ) );

			lastUploadedPhotos.add( portalPagePhoto );
		}
		return lastUploadedPhotos;
	}

	private List<Integer> getLastUploadedPhotos() {
		final SqlIdsSelectQuery selectQuery = photoSqlHelperService.getPortalPageLastUploadedPhotosSQL();
		final SqlSelectIdsResult selectResult = photoService.load( selectQuery );

		return selectResult.getIds();
	}

	private List<Integer> getTheBestPhotos() {
		final int minMarksTobeInPhotosOfTheDay = configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_PHOTO_OF_THE_DAY );
		final int days = configurationService.getInt( ConfigurationKey.PHOTO_RATING_PORTAL_PAGE_BEST_PHOTOS_FROM_PHOTOS_THAT_GOT_ENOUGH_MARKS_FOR_N_LAST_DAYS );

		final Date timeFrom = dateUtilsService.getFirstSecondOfTheDayNDaysAgo( days );
		final SqlIdsSelectQuery selectQuery = photoSqlHelperService.getPortalPageBestPhotosIdsSQL( minMarksTobeInPhotosOfTheDay, timeFrom );
		final SqlSelectIdsResult sqlSelectIdsResult = photoService.load( selectQuery );

		return sqlSelectIdsResult.getIds();
	}
}
