package controllers.photos.list;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.context.EnvironmentContext;
import core.general.data.PhotoListCriterias;
import core.general.data.photoList.AbstractPhotoListData;
import core.general.data.photoList.BestPhotoListData;
import core.general.data.photoList.PhotoListData;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.entry.GenreService;
import core.services.entry.GroupOperationService;
import core.services.entry.VotingCategoryService;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import core.services.utils.UtilsService;
import core.services.utils.sql.PhotoCriteriasSqlService;
import elements.PhotoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import utils.*;
import core.services.pageTitle.PageTitlePhotoUtilsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( UrlUtilsServiceImpl.PHOTOS_URL )
public class PhotoListController {

	public static final String VIEW = "photos/list/PhotoList";

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Autowired
	private PhotoListCriteriasService photoListCriteriasService;

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UrlUtilsService urlUtilsService;
	
	@Autowired
	private PageTitlePhotoUtilsService pageTitlePhotoUtilsService;
	
	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private GroupOperationService groupOperationService;
	
	@Autowired
	private PhotoCriteriasSqlService photoCriteriasSqlService;

	@ModelAttribute( "photoListModel" )
	public PhotoListModel prepareModel() {
		final PhotoListModel model = new PhotoListModel();

		model.setDeviceType( EnvironmentContext.getDeviceType() );

		return model;
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel();
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( utilsService.getPhotosOnPage( EnvironmentContext.getCurrentUser() ) );

		return pagingModel;
	}

	// all -->
	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showAllPhotos( final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final List<AbstractPhotoListData> photoListDatas = newArrayList();

		if ( pagingModel.getCurrentPage() == 1 ) {
			final PhotoListCriterias topBestCriterias = photoListCriteriasService.getForAllPhotosTopBest( EnvironmentContext.getCurrentUser() );
			final AbstractPhotoListData topBestData = new BestPhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( topBestCriterias, pagingModel ) );
			topBestData.setPhotoListCriterias( topBestCriterias );
			topBestData.setLinkToFullList( urlUtilsService.getPhotosBestInPeriodUrl( topBestCriterias.getVotingTimeFrom(), topBestCriterias.getVotingTimeTo() ) );
			topBestData.setTitleData( pageTitlePhotoUtilsService.getPhotosAllData() );

			photoListDatas.add( topBestData );
		}

		final PhotoListCriterias criterias = photoListCriteriasService.getForAllPhotos( EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosAllData() );

		photoListDatas.add( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/best/" )
	public String showAbsoluteBestPhotos( final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final PhotoListCriterias criterias = photoListCriteriasService.getForAbsolutelyBest( EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosAllDataBest() );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}
	// all <--

	// by genre -->
	@RequestMapping( method = RequestMethod.GET, value = "genres/{genreId}/" )
	public String showPhotosByGenre( final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int genreId = assertGenreExists( _genreId );
		final Genre genre = genreService.load( genreId );

		final List<AbstractPhotoListData> photoListDatas = newArrayList();

		if ( pagingModel.getCurrentPage() == 1 ) {
			final PhotoListCriterias topBestCriterias = photoListCriteriasService.getForGenreTopBest( genre, EnvironmentContext.getCurrentUser() );
			final AbstractPhotoListData topBestData = new BestPhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( topBestCriterias, pagingModel ) );
			topBestData.setPhotoListCriterias( topBestCriterias );
			topBestData.setTitleData( pageTitlePhotoUtilsService.getPhotosByGenreData( genre ) );
			topBestData.setLinkToFullList( urlUtilsService.getPhotosByGenreLinkBest( genreId ) );

			photoListDatas.add( topBestData );
		}

		final PhotoListCriterias criterias = photoListCriteriasService.getForGenre( genre, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByGenreData( genre ) );
		data.setPhotoListBottomText( genre.getDescription() );

		photoListDatas.add( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "genres/{genreId}/best/" )
	public String showPhotosByGenreBest( final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int genreId = assertGenreExists( _genreId );
		final Genre genre = genreService.load( genreId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForGenreBestForPeriod( genre, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByGenreDataBest( genre ) );
		data.setPhotoListBottomText( genre.getDescription() );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}
	// by genre <--

	// by user -->
	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/" )
	public String showPhotosByUser( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );

		final User user = userService.load( userId );

		final List<AbstractPhotoListData> photoListDatas = newArrayList();

		if ( pagingModel.getCurrentPage() == 1 ) {
			final PhotoListCriterias topBestCriterias = photoListCriteriasService.getForUserTopBest( user, EnvironmentContext.getCurrentUser() );
			final AbstractPhotoListData topBestData = new BestPhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( topBestCriterias, pagingModel ) );
			topBestData.setPhotoListCriterias( topBestCriterias );
			topBestData.setTitleData( pageTitlePhotoUtilsService.getPhotosByUser( user ) );
			topBestData.setLinkToFullList( urlUtilsService.getPhotosByUserLinkBest( userId ) );
			topBestData.setPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos( true );

			photoListDatas.add( topBestData );
		}

		final PhotoListCriterias criterias = photoListCriteriasService.getForUser( user, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByUser( user ) );
		data.setPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos( true );
		setUserOwnPhotosGroupOperationMenuContainer( user, data );

		photoListDatas.add( data );

		initUserGenres( model, user );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/best/" )
	public String showPhotosByUserBest( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );

		final User user = userService.load( userId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAbsolutelyBest( user, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByUserBest( user ) );
		data.setPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos( true );
		setUserOwnPhotosGroupOperationMenuContainer( user, data );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initUserGenres( model, user );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}
	// by user <--

	// by user and genre -->
	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/genre/{genreId}/" )
	public String showPhotosByUserByGenre( final @PathVariable( "userId" ) String _userId, final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );
		final int genreId = assertGenreExists( _genreId );

		final Genre genre = genreService.load( genreId );
		final User user = userService.load( userId );

		final List<AbstractPhotoListData> photoListDatas = newArrayList();

		if ( pagingModel.getCurrentPage() == 1 ) {
			final PhotoListCriterias topBestCriterias = photoListCriteriasService.getForUserAndGenreTopBest( user, genre, EnvironmentContext.getCurrentUser() );
			final AbstractPhotoListData topBestData = new BestPhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( topBestCriterias, pagingModel ) );
			topBestData.setPhotoListCriterias( topBestCriterias );
			topBestData.setTitleData( pageTitlePhotoUtilsService.getPhotosByUserAndGenre( user, genre ) );
			topBestData.setLinkToFullList( urlUtilsService.getPhotosByUserByGenreLinkBest( userId, genreId ) );
			topBestData.setPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos( true );

			photoListDatas.add( topBestData );
		}


		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenre( user, genre, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByUserAndGenre( user, genre ) );
		data.setPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos( true );
		setUserOwnPhotosGroupOperationMenuContainer( user, data );

		photoListDatas.add( data );

		initUserGenres( model, user );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/genre/{genreId}/best/" )
	public String showPhotosByUserByGenreBest( final @PathVariable( "userId" ) String _userId, final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );
		final int genreId = assertGenreExists( _genreId );

		final Genre genre = genreService.load( genreId );
		final User user = userService.load( userId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenreAbsolutelyBest( user, genre, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByUserAndGenreBest( user, genre ) );
		data.setPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos( true );
		setUserOwnPhotosGroupOperationMenuContainer( user, data );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initUserGenres( model, user );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	// by user and genre <--
	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/category/" )
	public String showPhotosVotedByUser( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );

		final User user = userService.load( userId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForVotedPhotos( user, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosVotedByUser( user ) );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/category/{votingCategoryId}/" )
	public String showPhotosByUserByVotingCategory( final @PathVariable( "userId" ) String _userId, final @PathVariable( "votingCategoryId" ) int votingCategoryId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );

		final User user = userService.load( userId );

		final PhotoVotingCategory votingCategory = votingCategoryService.load( votingCategoryId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForVotedPhotos( votingCategory, user, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByUserByVotingCategory( user, votingCategory ) );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	// by date -->

	@RequestMapping( method = RequestMethod.GET, value = "date/{date}/uploaded/" )
	public String showPhotosByDate( final @PathVariable( "date" ) String date, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return processPhotosOnPeriod( date, date, model, pagingModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "from/{datefrom}/to/{dateto}/uploaded/" )
	public String showPhotosByPeriod( final @PathVariable( "datefrom" ) String dateFrom, final @PathVariable( "dateto" ) String dateTo, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return processPhotosOnPeriod( dateFrom, dateTo, model, pagingModel );
	}

	private String processPhotosOnPeriod( final String dateFrom, final String dateTo, final PhotoListModel model, final PagingModel pagingModel ) {
		final Date fDateFrom = dateUtilsService.parseDate( dateFrom );
		final Date fDateTo = dateUtilsService.parseDate( dateTo );

		final List<AbstractPhotoListData> photoListDatas = newArrayList();

		final PhotoListCriterias criterias = photoListCriteriasService.getForPeriod( fDateFrom, fDateTo, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByPeriodData( fDateFrom, fDateTo ) );

		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeFullDays( fDateFrom, fDateTo );
		data.setPhotoRatingTimeFrom( timeRangeToday.getTimeFrom() );
		data.setPhotoRatingTimeTo( timeRangeToday.getTimeTo() );

		photoListDatas.add( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "date/{date}/best/" )
	public String showBestPhotosByDate( final @PathVariable( "date" ) String date, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return processBestPhotosOnPeriod( date, date, model, pagingModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "from/{datefrom}/to/{dateto}/best/" )
	public String showBestPhotosByPeriod( final @PathVariable( "datefrom" ) String dateFrom, final @PathVariable( "dateto" ) String dateTo, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return processBestPhotosOnPeriod( dateFrom, dateTo, model, pagingModel );
	}

	private String processBestPhotosOnPeriod( final String dateFrom, final String dateTo, final PhotoListModel model, final PagingModel pagingModel ) {
		final Date fDateFrom = dateUtilsService.parseDate( dateFrom );
		final Date fDateTo = dateUtilsService.parseDate( dateTo );

		final PhotoListCriterias criterias = photoListCriteriasService.getForPeriodBest( fDateFrom, fDateTo, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByPeriodDataBest( fDateFrom, fDateTo ) );

		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeFullDays( fDateFrom, fDateTo );
		data.setPhotoRatingTimeFrom( timeRangeToday.getTimeFrom() );
		data.setPhotoRatingTimeTo( timeRangeToday.getTimeTo() );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}
	// by date <--

	// by User Membership Type -->
	@RequestMapping( method = RequestMethod.GET, value = "type/{typeId}/" )
	public String showPhotosByMembershipType( final @PathVariable( "typeId" ) int typeId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		final UserMembershipType membershipType = UserMembershipType.getById( typeId );

		final List<AbstractPhotoListData> photoListDatas = newArrayList();

		if ( pagingModel.getCurrentPage() == 1 ) {
			final PhotoListCriterias topBestCriterias = photoListCriteriasService.getForMembershipTypeTopBest( membershipType, EnvironmentContext.getCurrentUser() );
			final AbstractPhotoListData topBestData = new BestPhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( topBestCriterias, pagingModel ) );
			topBestData.setPhotoListCriterias( topBestCriterias );
			topBestData.setTitleData( pageTitlePhotoUtilsService.getPhotosByMembershipType( membershipType ) );
			topBestData.setLinkToFullList( urlUtilsService.getPhotosByMembershipBest( membershipType, UrlUtilsServiceImpl.PHOTOS_URL ) );

			photoListDatas.add( topBestData );
		}

		final PhotoListCriterias criterias = photoListCriteriasService.getForMembershipType( membershipType, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByMembershipType( membershipType ) );

		photoListDatas.add( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "type/{typeId}/best/" )
	public String showPhotosByMembershipTypeBest( final @PathVariable( "typeId" ) int typeId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		final UserMembershipType membershipType = UserMembershipType.getById( typeId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForMembershipTypeBestForPeriod( membershipType, EnvironmentContext.getCurrentUser() );
		final AbstractPhotoListData data = new PhotoListData( photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( pageTitlePhotoUtilsService.getPhotosByMembershipTypeBest( membershipType ) );

		final List<AbstractPhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas );

		return VIEW;
	}
	// by User Membership Type <--

	private void initPhotoListData( final PhotoListModel model, final PagingModel pagingModel, final List<AbstractPhotoListData> photoListDatas ) {

		for ( final AbstractPhotoListData listData : photoListDatas ) {
			final PhotoListCriterias criterias = listData.getPhotoListCriterias();

			final List<Photo> pagePhotos = getPhotos( pagingModel, listData );
			final PhotoList photoList = getPhotoList( pagePhotos, listData, criterias );

			photoList.setPhotoGroupOperationMenuContainer( pagePhotos.size() > 0 ? groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( listData.getPhotoGroupOperationMenuContainer(), listData instanceof BestPhotoListData, EnvironmentContext.getCurrentUser() ) : groupOperationService.getNoPhotoGroupOperationMenuContainer() );

			if ( listData.isPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos() ) {
				photoService.hidePhotoPreviewForAnonymouslyPostedPhotos( photoList.getPhotoInfos() );
			}

			model.addPhotoList( photoList );
			model.setPageTitleData( listData.getTitleData() );
		}
	}

	private List<Photo> getPhotos( final PagingModel pagingModel, final AbstractPhotoListData listData ) {
		final List<Photo> pagePhotos;
		final SqlIdsSelectQuery selectIdsQuery = listData.getPhotoListQuery();

		final SqlSelectIdsResult sqlSelectIdsResult = photoService.load( selectIdsQuery );
		pagePhotos = photoService.load( sqlSelectIdsResult.getIds() );
		pagingModel.setTotalItems( sqlSelectIdsResult.getRecordQty() );

		return pagePhotos;
	}

	private PhotoList getPhotoList( final List<Photo> pagePhotos, final AbstractPhotoListData listData, final PhotoListCriterias criterias ) {

		final List<PhotoInfo> photoInfos;
		if ( dateUtilsService.isEmptyTime( listData.getPhotoRatingTimeFrom() ) && dateUtilsService.isEmptyTime( listData.getPhotoRatingTimeTo() ) ) {
			photoInfos = photoService.getPhotoInfos( pagePhotos, listData.getPhotoRatingTimeFrom(), listData.getPhotoRatingTimeTo(), EnvironmentContext.getCurrentUser() );
		} else {
			photoInfos = photoService.getPhotoInfos( pagePhotos, EnvironmentContext.getCurrentUser() );
		}

		final boolean showPaging = !criterias.isTopBestPhotoList();
		final PhotoList photoList = new PhotoList( photoInfos, photoListCriteriasService.getPhotoListTitle( criterias ), showPaging );

		photoList.setLinkToFullListText( photoListCriteriasService.getLinkToFullListText( criterias ) );
		photoList.setLinkToFullList( listData.getLinkToFullList() );
		photoList.setPhotosCriteriasDescription( photoListCriteriasService.getPhotoListCriteriasDescription( criterias ) );
		photoList.setBottomText( listData.getPhotoListBottomText() );
		photoList.setPhotosInLine( utilsService.getPhotosInLine( EnvironmentContext.getCurrentUser() ) );

		return photoList;
	}

	private void initUserGenres( final PhotoListModel model, final User user ) {
		model.setUser( user );
		model.setUserPhotosByGenres( photoService.getUserPhotosByGenres( user.getId() ) );
	}

	private int assertGenreExists( final String _genreId ) {
		securityService.assertGenreExists( _genreId );
		return NumberUtils.convertToInt( _genreId );
	}

	private int assertUserExistsAndGetUserId( final String _userId ) {
		securityService.assertUserExists( _userId );
		return NumberUtils.convertToInt( _userId );
	}

	private void setUserOwnPhotosGroupOperationMenuContainer( final User user, final AbstractPhotoListData data ) {
		if ( UserUtils.isUsersEqual( EnvironmentContext.getCurrentUser(), user ) ) {
			data.setPhotoGroupOperationMenuContainer( new PhotoGroupOperationMenuContainer( groupOperationService.getUserOwnPhotosGroupOperationMenus() ) );
		}
	}
}
