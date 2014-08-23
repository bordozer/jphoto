package ui.controllers.photos.list;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.data.TimeRange;
import core.general.data.photoList.PhotoListData;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.dao.*;
import core.services.entry.GenreService;
import core.services.entry.GroupOperationService;
import core.services.entry.VotingCategoryService;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoService;
import core.services.photo.list.PhotoListFactoryService;
import core.services.photo.list.factory.AbstractPhotoListFactory;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoQueryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import ui.context.EnvironmentContext;
import ui.controllers.photos.edit.GenreWrapper;
import ui.elements.PhotoList;
import ui.services.UtilsService;
import ui.services.breadcrumbs.BreadcrumbsPhotoGalleryService;
import utils.NumberUtils;
import utils.PagingUtils;
import utils.UserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( UrlUtilsServiceImpl.PHOTOS_URL )
public class PhotoListController {

	public static final String VIEW = "photos/list/PhotoList";

	private static final String PHOTO_FILTER_MODEL = "photoFilterModel";
	private static final String PHOTO_FILTER_DATA_SESSION_KEY = "PhotoFilterDataKey";

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
	private BreadcrumbsPhotoGalleryService breadcrumbsPhotoGalleryService;
	
	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private GroupOperationService groupOperationService;
	
	@Autowired
	private PhotoQueryService photoQueryService;

	@Autowired
	private Services services;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private PhotoFilterValidator photoFilterValidator;

	@Autowired
	private PhotoListFactoryService photoListFactoryService;

	@ModelAttribute( "photoListModel" )
	public PhotoListModel prepareModel() {
		final PhotoListModel model = new PhotoListModel();

		model.setDeviceType( EnvironmentContext.getDeviceType() );

		return model;
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( utilsService.getPhotosOnPage( getCurrentUser() ) );

		return pagingModel;
	}

	@ModelAttribute( PHOTO_FILTER_MODEL )
	public PhotoFilterModel prepareUserFilterModel( final HttpServletRequest request ) {
		final PhotoFilterModel filterModel = new PhotoFilterModel();

		filterModel.setShowPhotosWithNudeContent( true );

		final HttpSession session = request.getSession();
		final PhotoFilterData filterData = ( PhotoFilterData ) session.getAttribute( PHOTO_FILTER_DATA_SESSION_KEY );

		if ( filterData == null ) {
			setDefaultOrdering( filterModel );
		} else {
			filterModel.setPhotosSortColumn( String.valueOf( filterData.getPhotosSortColumn().getId() ) );
			filterModel.setPhotosSortOrder( String.valueOf( filterData.getPhotosSortOrder().getId() ) );
		}

		filterModel.setGenreWrappers( getGenreWrappers() );

		filterModel.setPhotoAuthorMembershipTypeIds( Lists.transform( Arrays.asList( UserMembershipType.values() ), new Function<UserMembershipType, Integer>() {
			@Override
			public Integer apply( final UserMembershipType membershipType ) {
				return membershipType.getId();
			}
		} ) );

		return filterModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showPhotoGallery( final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel ) {

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryTopBest( pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), getCurrentUser() ), pagingModel ) );
		model.addPhotoList( getPhotoList( photoListFactoryService.gallery( pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), getCurrentUser() ), pagingModel ) );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotoGalleryBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/best/" )
	public String showAbsoluteBestPhotos( final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryBest( pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), getCurrentUser() ), pagingModel ) );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getAbsolutelyBestPhotosBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "genres/{genreId}/" )
	public String showPhotosByGenre( final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int genreId = assertGenreExists( _genreId );
		final Genre genre = genreService.load( genreId );

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForGenreTopBest( genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );
		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForGenre( genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotosByGenreBreadcrumbs( genre ) );

		filterModel.setFilterGenreId( _genreId );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "genres/{genreId}/best/" )
	public String showPhotosByGenreBest( final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int genreId = assertGenreExists( _genreId );
		final Genre genre = genreService.load( genreId );

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForGenreBest( genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotosByGenreBestBreadcrumbs( genre ) );

		filterModel.setFilterGenreId( _genreId );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/" )
	public String showPhotosByUser( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );
		final User user = userService.load( userId );

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForUserTopBest( user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );
		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForUser( user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );

		fillFilterModelWithUserData( filterModel, user );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotosByUserBreadcrumbs( user ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/best/" )
	public String showPhotosByUserBest( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );
		final User user = userService.load( userId );

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForUserBest( user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotosByUserBestBreadcrumbs( user ) );

		fillFilterModelWithUserData( filterModel, user );

		initUserGenres( model, user );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/genre/{genreId}/" )
	public String showPhotosByUserByGenre( final @PathVariable( "userId" ) String _userId, final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );
		final int genreId = assertGenreExists( _genreId );

		final Genre genre = genreService.load( genreId );
		final User user = userService.load( userId );

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForUserAndGenreTopBest( user, genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );
		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForUserAndGenre( user, genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );

		filterModel.setFilterGenreId( _genreId );
		fillFilterModelWithUserData( filterModel, user );

		initUserGenres( model, user );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotosByUserAndGenreBreadcrumbs( user, genre ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/genre/{genreId}/best/" )
	public String showPhotosByUserByGenreBest( final @PathVariable( "userId" ) String _userId, final @PathVariable( "genreId" ) String _genreId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );
		final int genreId = assertGenreExists( _genreId );

		final Genre genre = genreService.load( genreId );
		final User user = userService.load( userId );

		model.addPhotoList( getPhotoList( photoListFactoryService.galleryForUserAndGenreBest( user, genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotosByUserAndGenreBestBreadcrumbs( user, genre ) );

		initUserGenres( model, user );

		filterModel.setFilterGenreId( _genreId );

		fillFilterModelWithUserData( filterModel, user );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/category/" )
	public String showPhotosVotedByUser( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );
		final User user = userService.load( userId );

		model.addPhotoList( getPhotoList( photoListFactoryService.appraisedByUserPhotos( user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() ), pagingModel ) );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getPhotosAppraisedByUserBreadcrumbs( user ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "members/{userId}/category/{votingCategoryId}/" )
	public String showPhotosByUserByVotingCategory( final @PathVariable( "userId" ) String _userId, final @PathVariable( "votingCategoryId" ) int votingCategoryId, final @ModelAttribute( "photoListModel" ) PhotoListModel model
		, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {

		final int userId = assertUserExistsAndGetUserId( _userId );

		final User user = userService.load( userId );

		final PhotoVotingCategory votingCategory = votingCategoryService.load( votingCategoryId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForAppraisedByUserPhotos( votingCategory, user, getCurrentUser() );
		final PhotoListData data = new PhotoListData( photoQueryService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( breadcrumbsPhotoGalleryService.getPhotosByUserByVotingCategoryBreadcrumbs( user, votingCategory ) );

		final List<PhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas, filterModel );

		fillFilterModelWithUserData( filterModel, user );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "date/{date}/uploaded/" )
	public String showPhotosByDate( final @PathVariable( "date" ) String date, final @ModelAttribute( "photoListModel" ) PhotoListModel model
		, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {
		return processPhotosOnPeriod( date, date, model, pagingModel, filterModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "from/{datefrom}/to/{dateto}/uploaded/" )
	public String showPhotosByPeriod( final @PathVariable( "datefrom" ) String dateFrom, final @PathVariable( "dateto" ) String dateTo
		, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {
		return processPhotosOnPeriod( dateFrom, dateTo, model, pagingModel, filterModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "date/{date}/best/" )
	public String showBestPhotosByDate( final @PathVariable( "date" ) String date, final @ModelAttribute( "photoListModel" ) PhotoListModel model
		, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {
		return processBestPhotosOnPeriod( date, date, model, pagingModel, filterModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "from/{datefrom}/to/{dateto}/best/" )
	public String showBestPhotosByPeriod( final @PathVariable( "datefrom" ) String dateFrom, final @PathVariable( "dateto" ) String dateTo
		, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {
		return processBestPhotosOnPeriod( dateFrom, dateTo, model, pagingModel, filterModel );
	}

	private String processPhotosOnPeriod( final String dateFrom, final String dateTo, final PhotoListModel model, final PagingModel pagingModel, final PhotoFilterModel filterModel ) {
		final Date fDateFrom = dateUtilsService.parseDate( dateFrom );
		final Date fDateTo = dateUtilsService.parseDate( dateTo );

		final List<PhotoListData> photoListDatas = newArrayList();

		final PhotoListCriterias criterias = photoListCriteriasService.getForPeriod( fDateFrom, fDateTo, getCurrentUser() );
		final PhotoListData data = new PhotoListData( photoQueryService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( breadcrumbsPhotoGalleryService.getPhotosByPeriodBreadcrumbs( fDateFrom, fDateTo ) );

		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeFullDays( fDateFrom, fDateTo );
		data.setPhotoRatingTimeFrom( timeRangeToday.getTimeFrom() );
		data.setPhotoRatingTimeTo( timeRangeToday.getTimeTo() );

		photoListDatas.add( data );

		initPhotoListData( model, pagingModel, photoListDatas, filterModel );

		return VIEW;
	}

	private String processBestPhotosOnPeriod( final String dateFrom, final String dateTo, final PhotoListModel model, final PagingModel pagingModel, final PhotoFilterModel filterModel ) {
		final Date fDateFrom = dateUtilsService.parseDate( dateFrom );
		final Date fDateTo = dateUtilsService.parseDate( dateTo );

		final PhotoListCriterias criterias = photoListCriteriasService.getForPeriodBest( fDateFrom, fDateTo, getCurrentUser() );
		final PhotoListData data = new PhotoListData( photoQueryService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( breadcrumbsPhotoGalleryService.getPhotosByPeriodBestBreadcrumbs( fDateFrom, fDateTo ) );

		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeFullDays( fDateFrom, fDateTo );
		data.setPhotoRatingTimeFrom( timeRangeToday.getTimeFrom() );
		data.setPhotoRatingTimeTo( timeRangeToday.getTimeTo() );

		final List<PhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas, filterModel );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "type/{typeId}/" )
	public String showPhotosByMembershipType( final @PathVariable( "typeId" ) int typeId, final @ModelAttribute( "photoListModel" ) PhotoListModel model
		, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {
		final UserMembershipType membershipType = UserMembershipType.getById( typeId );

		final List<PhotoListData> photoListDatas = newArrayList();

		if ( pagingModel.getCurrentPage() == 1 ) {
			final PhotoListCriterias topBestCriterias = photoListCriteriasService.getForMembershipTypeTopBest( membershipType, getCurrentUser() );
			final PhotoListData topBestData = new PhotoListData( photoQueryService.getForCriteriasPagedIdsSQL( topBestCriterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() ) );
			topBestData.setPhotoListCriterias( topBestCriterias );
			topBestData.setLinkToFullList( urlUtilsService.getPhotosByMembershipBest( membershipType, UrlUtilsServiceImpl.PHOTOS_URL ) );

			photoListDatas.add( topBestData );
		}

		final PhotoListCriterias criterias = photoListCriteriasService.getForMembershipType( membershipType, getCurrentUser() );
		final PhotoListData data = new PhotoListData( photoQueryService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( breadcrumbsPhotoGalleryService.getPhotosByMembershipTypeBreadcrumbs( membershipType ) );

		photoListDatas.add( data );

		initPhotoListData( model, pagingModel, photoListDatas, filterModel );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "type/{typeId}/best/" )
	public String showPhotosByMembershipTypeBest( final @PathVariable( "typeId" ) int typeId, final @ModelAttribute( "photoListModel" ) PhotoListModel model
		, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel ) {
		final UserMembershipType membershipType = UserMembershipType.getById( typeId );

		final PhotoListCriterias criterias = photoListCriteriasService.getForMembershipTypeBestForPeriod( membershipType, getCurrentUser() );
		final PhotoListData data = new PhotoListData( photoQueryService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() ) );
		data.setPhotoListCriterias( criterias );
		data.setTitleData( breadcrumbsPhotoGalleryService.getPhotosByMembershipTypeBestBreadcrumbs( membershipType ) );

		final List<PhotoListData> photoListDatas = newArrayList( data );

		initPhotoListData( model, pagingModel, photoListDatas, filterModel );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/filter/" )
	public String searchGet( final PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel
		, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel filterModel, final HttpServletRequest request ) {

		final HttpSession session = request.getSession();
		final PhotoFilterData filterData = ( PhotoFilterData ) session.getAttribute( PHOTO_FILTER_DATA_SESSION_KEY );

		if ( filterData == null ) {
			return String.format( "redirect:%s", urlUtilsService.getAllPhotosLink() ); // TODO: the session is expired - haw to be?
		}

		final User currentUser = getCurrentUser();

		final SqlIdsSelectQuery selectQuery = filterData.getSelectQuery();
		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );

		final SqlSelectIdsResult selectResult = photoService.load( selectQuery );

		final PhotoList photoList = new PhotoList( selectResult.getIds(), translatorService.translate( "Photo list: Search result title", getLanguage() ) );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( currentUser ) );
		model.addPhotoList( photoList );

		pagingModel.setTotalItems( selectResult.getRecordQty() );

		filterModel.setFilterPhotoName( filterData.getFilterPhotoName() );
		filterModel.setFilterGenreId( filterData.getFilterGenre() != null ? String.valueOf( filterData.getFilterGenre().getId() ) : "-1" );
		filterModel.setShowPhotosWithNudeContent( filterData.isShowPhotosWithNudeContent() );
		filterModel.setFilterAuthorName( filterData.getFilterAuthorName() );
		filterModel.setPhotoAuthorMembershipTypeIds( filterData.getPhotoAuthorMembershipTypeIds() );

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getFilteredPhotoListBreadcrumbs() );

		return PhotoListController.VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/filter/" )
	public String searchPost( final PhotoListModel model, final @ModelAttribute( PHOTO_FILTER_MODEL ) PhotoFilterModel photoFilterModel
		, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel, final HttpServletRequest request  ) {

		model.setPageTitleData( breadcrumbsPhotoGalleryService.getFilteredPhotoListBreadcrumbs() );

		final BindingResult bindingResult = new BindException( photoFilterModel, "" );
		photoFilterValidator.validate( photoFilterModel, bindingResult );
		photoFilterModel.setBindingResult( bindingResult );

		if ( bindingResult.hasErrors() ) {
			return VIEW;
		}

		final String filterByPhotoName = photoFilterModel.getFilterPhotoName();
		final Genre filterByGenre = ! photoFilterModel.getFilterGenreId().equals( "-1" ) ? genreService.load( NumberUtils.convertToInt( photoFilterModel.getFilterGenreId() ) ) : null;
		final boolean showPhotosWithNudeContent = photoFilterModel.isShowPhotosWithNudeContent();
		final String filterByAuthorName = photoFilterModel.getFilterAuthorName();
		final List<Integer> filterByPhotoAuthorMembershipTypeIds = photoFilterModel.getPhotoAuthorMembershipTypeIds();
		final PhotoFilterSortColumn sortColumn = PhotoFilterSortColumn.getById( Integer.parseInt( photoFilterModel.getPhotosSortColumn() ) );
		final PhotoFilterSortOrder sortOrder = PhotoFilterSortOrder.getById( Integer.parseInt( photoFilterModel.getPhotosSortOrder() ) );

		final SqlTable tPhotos = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
		final SqlIdsSelectQuery selectIdsQuery = new SqlIdsSelectQuery( tPhotos );

		final SqlTable tUsers = new SqlTable( UserDaoImpl.TABLE_USERS );

		if ( StringUtils.isNotEmpty( filterByPhotoName ) ) {
			final SqlColumnSelectable tPhotoColName = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_NAME );
			final SqlLogicallyJoinable photoNameCondition = new SqlCondition( tPhotoColName, SqlCriteriaOperator.LIKE, filterByPhotoName, dateUtilsService );
			selectIdsQuery.addWhereAnd( photoNameCondition );
		}

		if ( filterByGenre != null ) {
			final SqlColumnSelectable tPhotoColGenreId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_GENRE_ID );
			final SqlLogicallyJoinable photoNameCondition = new SqlCondition( tPhotoColGenreId, SqlCriteriaOperator.EQUALS, filterByGenre.getId(), dateUtilsService );
			selectIdsQuery.addWhereAnd( photoNameCondition );
		}

		if ( ! showPhotosWithNudeContent ) {
			final SqlColumnSelectable tPhotoColHasNudeContent = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_CONTAINS_NUDE_CONTENT );
			final SqlLogicallyJoinable photoNameCondition = new SqlCondition( tPhotoColHasNudeContent, SqlCriteriaOperator.EQUALS, ! showPhotosWithNudeContent, dateUtilsService );
			selectIdsQuery.addWhereAnd( photoNameCondition );
		}

		final boolean isFilterByAuthorNameNeeded = StringUtils.isNotEmpty( filterByAuthorName );
		final boolean isFilterByAuthorMembershipNeeded = filterByPhotoAuthorMembershipTypeIds.size() < UserMembershipType.values().length;
		final boolean isFilterByUserDataNeeded = isFilterByAuthorNameNeeded || isFilterByAuthorMembershipNeeded;
		if ( isFilterByUserDataNeeded ) {
			final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
			final SqlColumnSelect tUsersColId = new SqlColumnSelect( tUsers, UserDaoImpl.ENTITY_ID );
			final SqlJoin join = SqlJoin.inner( tUsers, new SqlJoinCondition( tPhotosColUserId, tUsersColId ) );
			selectIdsQuery.joinTable( join );

			if ( isFilterByAuthorNameNeeded ) {
				final SqlColumnSelectable tUserColName = new SqlColumnSelect( tUsers, UserDaoImpl.TABLE_COLUMN_NAME );
				final SqlLogicallyJoinable photoAuthorNameCondition = new SqlCondition( tUserColName, SqlCriteriaOperator.LIKE, filterByAuthorName, dateUtilsService );
				selectIdsQuery.addWhereAnd( photoAuthorNameCondition );
			}

			if ( isFilterByAuthorMembershipNeeded ) {
				final SqlColumnSelectable tUsersColMembershipType = new SqlColumnSelect( tUsers, UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE );
				final SqlConditionList membershipConditions = new SqlLogicallyOr();
				final List<SqlLogicallyJoinable> membershipConditionsItems = newArrayList();

				for ( final UserMembershipType membershipType : UserMembershipType.getByIds( filterByPhotoAuthorMembershipTypeIds ) ) {
					membershipConditionsItems.add( new SqlCondition( tUsersColMembershipType, SqlCriteriaOperator.EQUALS, membershipType.getId(), dateUtilsService ) );
				}
				membershipConditions.setLogicallyItems( membershipConditionsItems );
				selectIdsQuery.addWhereAnd( membershipConditions );
			}
		}

		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( tPhotos, UserDaoImpl.ENTITY_ID );
		final SqlColumnSelectable tPhotosColUploadTime = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );

		final SqlColumnSelectable tableSortColumn;

		switch ( sortColumn ) {
			case POSTING_TIME:
				tableSortColumn = tPhotosColUploadTime;
				break;
			case COMMENTS_COUNT:
				final SqlTable tComments = new SqlTable( PhotoCommentDaoImpl.TABLE_COMMENTS );
				final SqlColumnSelect tCommentsColPhotoId = new SqlColumnSelect( tComments, PhotoCommentDaoImpl.TABLE_COLUMN_PHOTO_ID );


				final SqlJoin join1 = SqlJoin.inner( tComments, new SqlJoinCondition( tPhotoColId, tCommentsColPhotoId ) );
				selectIdsQuery.joinTable( join1 );

				final SqlColumnSelect tCommentsColId = new SqlColumnSelect( tComments, PhotoCommentDaoImpl.ENTITY_ID );
				tableSortColumn = new SqlColumnAggregate( tCommentsColId, SqlFunctions.COUNT, "commentsCount" );

				final List<SqlBuildable> groupBy1 = newArrayList();
				groupBy1.add( tPhotoColId );
				selectIdsQuery.setGroupColumns( groupBy1 );
				break;
			case VIEWS_COUNT:
				final SqlTable tPreviews = new SqlTable( PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW );
				final SqlColumnSelect tPreviewsColPhotoId = new SqlColumnSelect( tPreviews, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID );

				final SqlJoin join2 = SqlJoin.inner( tPreviews, new SqlJoinCondition( tPhotoColId, tPreviewsColPhotoId ) );
				selectIdsQuery.joinTable( join2 );

				final SqlColumnSelect tPreviewsColId = new SqlColumnSelect( tPreviews, PhotoCommentDaoImpl.ENTITY_ID );
				tableSortColumn = new SqlColumnAggregate( tPreviewsColId, SqlFunctions.COUNT, "viewsCount" );

				final List<SqlBuildable> groupBy2 = newArrayList();
				groupBy2.add( tPhotoColId );
				selectIdsQuery.setGroupColumns( groupBy2 );
				break;
			case RATING:
				final SqlTable tPhotoVotingSummary = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY );
				final SqlColumnSelect tPhotoVotingSummaryColPhotoId = new SqlColumnSelect( tPhotoVotingSummary, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID );

				final SqlJoin join3 = SqlJoin.inner( tPhotoVotingSummary, new SqlJoinCondition( tPhotoColId, tPhotoVotingSummaryColPhotoId ) );
				selectIdsQuery.joinTable( join3 );

				final SqlColumnSelect tPhotoVotingSummaryColPhotoSummaryMark = new SqlColumnSelect( tPhotoVotingSummary, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY_MARKS );
				tableSortColumn = new SqlColumnAggregate( tPhotoVotingSummaryColPhotoSummaryMark, SqlFunctions.SUM, "photoSummaryMark" );

				final List<SqlBuildable> groupBy3 = newArrayList();
				groupBy3.add( tPhotoColId );
				selectIdsQuery.setGroupColumns( groupBy3 );
				break;
			default:
				throw new IllegalArgumentException( String.format( "Illegal photo search form sort column: %s", sortColumn ) );
		}
		selectIdsQuery.addSorting( tableSortColumn, sortOrder.getSortOrder() );
		selectIdsQuery.addSorting( tPhotosColUploadTime, SqlSortOrder.DESC );

		baseSqlUtilsService.initLimitAndOffset( selectIdsQuery, pagingModel );

		final User currentUser = getCurrentUser();

		final SqlSelectIdsResult selectResult = photoService.load( selectIdsQuery );

		final PhotoList photoList = new PhotoList( selectResult.getIds(), translatorService.translate( "Search result", getLanguage() ) );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( currentUser ) );
		model.addPhotoList( photoList );

		pagingModel.setTotalItems( selectResult.getRecordQty() );

		final PhotoFilterData filterData = new PhotoFilterData();
		filterData.setFilterPhotoName( filterByPhotoName );
		filterData.setFilterGenre( filterByGenre );
		filterData.setShowPhotosWithNudeContent( showPhotosWithNudeContent );
		filterData.setFilterAuthorName( filterByAuthorName );
		filterData.setPhotoAuthorMembershipTypeIds( filterByPhotoAuthorMembershipTypeIds );
		filterData.setSelectQuery( selectIdsQuery );
		filterData.setPhotosSortColumn( sortColumn );
		filterData.setPhotosSortOrder( sortOrder );

		final HttpSession session = request.getSession();
		session.setAttribute( PHOTO_FILTER_DATA_SESSION_KEY, filterData );

		return VIEW;
	}

	private void initPhotoListData( final PhotoListModel model, final PagingModel pagingModel, final List<PhotoListData> photoListDatas, final PhotoFilterModel filterModel ) {

		int listCounter = 0;
		for ( final PhotoListData listData : photoListDatas ) {
			final PhotoListCriterias criterias = listData.getPhotoListCriterias();

			final List<Integer> photosIds = getPhotosIds( pagingModel, listData );

			final PhotoList photoList = getPhotoList( photosIds, listData, criterias, getLanguage() );
			photoList.setPhotoListId( listCounter++ );

			photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getNoPhotoGroupOperationMenuContainer() ); // TODO: zaglushka

			model.addPhotoList( photoList );
			model.setPageTitleData( listData.getTitleData() );
		}

		setDefaultOrdering( filterModel );
	}

	private List<Integer> getPhotosIds( final PagingModel pagingModel, final PhotoListData listData ) {
		final SqlIdsSelectQuery selectIdsQuery = listData.getPhotoListQuery();

		final SqlSelectIdsResult sqlSelectIdsResult = photoService.load( selectIdsQuery );
		pagingModel.setTotalItems( sqlSelectIdsResult.getRecordQty() );

		return sqlSelectIdsResult.getIds();
	}

	private PhotoList getPhotoList( final List<Integer> photosIds, final PhotoListData listData, final PhotoListCriterias criterias, final Language language ) {

		final boolean showPaging = !criterias.isTopBestPhotoList();

		final String title = String.format( "<font color='red'>BUILT BY OLD WAY</font>" );
		final PhotoList photoList = new PhotoList( photosIds, title, showPaging );

		photoList.setLinkToFullListText( photoListCriteriasService.getLinkToFullListText() );
		photoList.setLinkToFullList( listData.getLinkToFullList() );
		photoList.setPhotosCriteriasDescription( "<font color='red'>BUILT BY OLD WAY</font>" );
		photoList.setBottomText( listData.getPhotoListBottomText() );

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

	private void setUserOwnPhotosGroupOperationMenuContainer( final User user, final PhotoListData data ) {
		if ( UserUtils.isUsersEqual( getCurrentUser(), user ) ) {
			data.setPhotoGroupOperationMenuContainer( new PhotoGroupOperationMenuContainer( groupOperationService.getUserOwnPhotosGroupOperationMenus() ) );
		}
	}

	private void setDefaultOrdering( final PhotoFilterModel filterModel ) {
		filterModel.setPhotosSortColumn( String.valueOf( PhotoFilterSortColumn.POSTING_TIME.getId() ) );
		filterModel.setPhotosSortOrder( String.valueOf( PhotoFilterSortOrder.DESC.getId() ) );
	}

	private void fillFilterModelWithUserData( final PhotoFilterModel filterModel, final User user ) {
		filterModel.setFilterAuthorName( user.getName() );
		filterModel.setPhotoAuthorMembershipTypeIds( newArrayList( user.getMembershipType().getId() ) );
	}

	// TODO: duplecated in /home/blu/dev/src/jphoto/jphoto/src/ui/controllers/photos/edit/PhotoEditDataController.java
	private List<GenreWrapper> getGenreWrappers() {
		final Language language = getLanguage();

		final List<GenreWrapper> result = newArrayList();

		final List<Genre> genres = genreService.loadAllSortedByNameForLanguage( language );
		for ( final Genre genre : genres ) {
			final GenreWrapper wrapper = new GenreWrapper( genre );
			wrapper.setGenreNameTranslated( translatorService.translateGenre( genre, language ) );

			result.add( wrapper );
		}

		return result;
	}

	private PhotoList getPhotoList( final AbstractPhotoListFactory photoListFactory, final PagingModel pagingModel ) {
		final int photoListId = 0; // TODO: check if it still has influence on sth
		final PhotoList photoList = photoListFactory.getPhotoList( photoListId, pagingModel.getCurrentPage(), getLanguage(), dateUtilsService.getCurrentTime() );

		if ( photoList == null ) {
			return null;
		}

		pagingModel.setTotalItems( photoList.getPhotosCount() );

		return photoList;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	private User getCurrentUser() {
		return EnvironmentContext.getCurrentUser();
	}
}
