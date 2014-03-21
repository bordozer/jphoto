package controllers.photos.rating;

import core.context.EnvironmentContext;
import core.general.base.PagingModel;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.entry.GroupOperationService;
import core.services.pageTitle.PageTitlePhotoUtilsService;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoService;
import core.services.security.Services;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.UtilsService;
import core.services.utils.sql.PhotoCriteriasSqlService;
import elements.PhotoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlSelectQuery;
import utils.NumberUtils;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "photos/rating" )
public class PhotoRatingController {

	public static final String VIEW = "photos/rating/PhotoRating";
	public static final String MODEL_NAME = "photoRatingModel";

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private Services services;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PageTitlePhotoUtilsService pageTitlePhotoUtilsService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private PhotoListCriteriasService photoListCriteriasService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoCriteriasSqlService photoCriteriasSqlService;

	@Autowired
	private GroupOperationService groupOperationService;

	@ModelAttribute( MODEL_NAME )
	public PhotoRatingModel prepareModel() {

		final PhotoRatingModel model = new PhotoRatingModel();

		final int maxSystemGenreRank = configurationService.getInt( ConfigurationKey.RANK_VOTING_MAX_GENRE_RANK );

		final List<String> photoRatingRanks = newArrayList();

		for ( int i = 0; i < maxSystemGenreRank + 1; i++ ) {
			photoRatingRanks.add( String.valueOf( i ) );
		}

		model.setPhotoRatingRanks( photoRatingRanks );
		model.setSelectedPhotoRatingRank( "1" );

		return model;
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( utilsService.getPhotosOnPage( EnvironmentContext.getCurrentUser() ) );

		return pagingModel;
	}

	@RequestMapping( value = "/" )
	public String showAllPhotos( final @ModelAttribute( MODEL_NAME ) PhotoRatingModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final User currentUser = EnvironmentContext.getCurrentUser();

		final PhotoListCriterias criterias = photoListCriteriasService.getForPeriodBest( dateUtilsService.getFirstSecondOfTheDayNDaysAgo( configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS ) ), dateUtilsService.getLastSecondOfToday(), currentUser );
		final SqlIdsSelectQuery sqlIdsSelectQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );

		final SqlSelectIdsResult idsResult = photoService.load( sqlIdsSelectQuery );
		final List<Photo> photos = photoService.load( idsResult.getIds() );
		final List<PhotoInfo> photoInfos = photoService.getPhotoInfos( photos, currentUser );

		final PhotoList photoList = getPhotoList( photoInfos );
		model.setPhotoList( photoList );

		model.setPageTitleData( pageTitlePhotoUtilsService.getPhotoRatingTitleData( NumberUtils.convertToInt( model.getSelectedPhotoRatingRank() ) ) );

		return VIEW;
	}

	private PhotoList getPhotoList( final List<PhotoInfo> photoInfos ) {
		final PhotoList photoList = new PhotoList( photoInfos, translatorService.translate( "Photo rating by user rank" ) );
		photoList.setPhotosInLine( 4 ); // TODO
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getNoPhotoGroupOperationMenuContainer() );

		return photoList;
	}
}
