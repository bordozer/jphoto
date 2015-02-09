package ui.controllers.portalpage;

import core.services.translator.TranslatorService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ui.context.EnvironmentContext;
import ui.elements.PageTitleData;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

@Controller
@RequestMapping( "/" )
public class PortalPageController {

	public static final String VIEW = "portalpage/PortalPage";
	public static final String MODEL_NAME = "portalPageModel";

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private SystemVarsService systemVarsService;

	@ModelAttribute( MODEL_NAME )
	public PortalPageModel prepareModel() {

		final PortalPageModel model = new PortalPageModel();
		model.setTranslatorService( translatorService );

		return model;
	}

	@RequestMapping( "/" )
	public String portalPage( @ModelAttribute( MODEL_NAME ) PortalPageModel model ) {

		/*model.setLastUploadedPhotos( getPortalPagePhotos( getLastUploadedPhotos() ) );

		final List<Integer> theBestPhotosIds = getTheBestPhotosIds();
		model.setBestPhotos( getPortalPagePhotos( theBestPhotosIds ) );

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
			portalGenre.setTotal( photoService.getPhotosCountByGenre( genre.getId() ) );
			portalGenre.setToday( photoService.getPhotosCountByGenreForPeriod( genre, todayFirstSecond, todayLastSecond ) );

			portalPageGenres.add( portalGenre );
		}

		model.setPortalPageGenres( portalPageGenres );
		model.setRandomBestPhotoArrayIndex( randomUtilsService.getRandomInt( 0, theBestPhotosIds.size() - 1 ) );

		model.setLastActivities( activityStreamService.getLastActivities( configurationService.getInt( ConfigurationKey.SYSTEM_ACTIVITY_PORTAL_PAGE_STREAM_LENGTH ) ) );*/

		final String title = translatorService.translate( BreadcrumbsBuilder.BREADCRUMBS_PORTAL_PAGE, EnvironmentContext.getLanguage() );
		model.getPageModel().setPageTitleData( new PageTitleData( systemVarsService.getProjectName(), title, title ) );

		return VIEW;
	}

	/*private List<PortalPagePhoto> getPortalPagePhotos( final List<Integer> photosIds ) {
		final User accessor = EnvironmentContext.getCurrentUser();
		Collections.shuffle( photosIds );
		final List<PortalPagePhoto> lastUploadedPhotos = newArrayList();
		for ( final Integer photoId : photosIds ) {
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
		final SqlIdsSelectQuery query = new PhotoListQueryBuilder( dateUtilsService )
			.forPage( 1, 12 ) // TODO: move 12 to configuration!
			.sortByUploadTimeDesc()
			.getQuery();

		return photoService.load( query ).getIds();
	}

	private List<Integer> getTheBestPhotosIds() {

		final SqlIdsSelectQuery query = new PhotoListQueryBuilder( dateUtilsService )
			.filterByMinimalMarks( configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_PHOTO_OF_THE_DAY ) )
			.filterByVotingTime( photoVotingService.getPortalPageBestDateRange() )
			.forPage( 1, 8 ) // TODO: move 8 to configuration!
			.sortBySumMarksDesc()
			.getQuery();

		final SqlSelectIdsResult sqlSelectIdsResult = photoService.load( query );

		final Date currentTime = dateUtilsService.getCurrentTime();
		final List<Integer> ids = sqlSelectIdsResult.getIds();
		CollectionUtils.filter( ids, new Predicate<Integer>() {
			@Override
			public boolean evaluate( final Integer photoId ) {
				return ! restrictionService.isPhotoOfTheDayRestrictedOn( photoId, currentTime );
			}
		} );

		return ids;
	}*/
}
