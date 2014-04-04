package ui.controllers.users.bookmarks;

import ui.controllers.photos.list.PhotoListController;
import ui.controllers.photos.list.PhotoListModel;
import core.context.EnvironmentContext;
import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GroupOperationService;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import core.services.utils.UtilsService;
import core.services.utils.sql.PhotoSqlHelperService;
import elements.PhotoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import utils.NumberUtils;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( UrlUtilsServiceImpl.USERS_URL )
public class UserBookmarksController {

	private static final String VIEW = PhotoListController.VIEW;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private PhotoSqlHelperService photoSqlHelperService;

	@Autowired
	private GroupOperationService groupOperationService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private Services services;

	@ModelAttribute( "photoListModel" )
	public PhotoListModel prepareModel() {
		final PhotoListModel model = new PhotoListModel();
		model.setShowPhotoSearchForm( false );

		model.setDeviceType( EnvironmentContext.getDeviceType() );

		return model;
	}

	@ModelAttribute
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( utilsService.getPhotosOnPage( EnvironmentContext.getCurrentUser() ) );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{userId}/favorites/photos/" )
	public String showFavoritePhotos( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final List<FavoriteEntryType> showIconsForFavoriteEntryTypes = newArrayList( FavoriteEntryType.FAVORITE_PHOTOS );

		initFavorites( userId, model, pagingModel, FavoriteEntryType.FAVORITE_PHOTOS, showIconsForFavoriteEntryTypes );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{userId}/favorites/members/photos/" )
	public String showPhotosOfFavoriteMembers( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final List<FavoriteEntryType> showIconsForFavoriteEntryTypes = newArrayList();

		final SqlIdsSelectQuery selectQuery = photoSqlHelperService.getPhotosOfUserFavoritesMembersSQL( pagingModel, userId );
		initFavorites( selectQuery, userId, model, pagingModel, FavoriteEntryType.FAVORITE_PHOTOS, showIconsForFavoriteEntryTypes );

		final User user = userService.load( userId );
		model.setPageTitleData( breadcrumbsUserService.getPhotosOfUserFavoriteMembers( user ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{userId}/bookmark/" )
	public String showBookmarkedPhotos( @PathVariable( "userId" ) String _userId, @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final List<FavoriteEntryType> showIconsForFavoriteEntryTypes = newArrayList();

		initFavorites( userId, model, pagingModel, FavoriteEntryType.BOOKMARKED_PHOTOS, showIconsForFavoriteEntryTypes );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{userId}/notification/comments/" )
	public String showPhotosWithNewCommentNotification( @PathVariable( "userId" ) String _userId, @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final List<FavoriteEntryType> showIconsForFavoriteEntryTypes = newArrayList( FavoriteEntryType.NEW_COMMENTS_NOTIFICATION );

		initFavorites( userId, model, pagingModel, FavoriteEntryType.NEW_COMMENTS_NOTIFICATION, showIconsForFavoriteEntryTypes );

		return VIEW;
	}

	private void initFavorites( final int userId, final PhotoListModel model, final PagingModel pagingModel, final FavoriteEntryType entryType, final List<FavoriteEntryType> showIconsForFavoriteEntryTypes ) {
		final SqlIdsSelectQuery selectQuery = photoSqlHelperService.getFavoritesPhotosSQL( pagingModel, userId, entryType );
		initFavorites( selectQuery,  userId, model, pagingModel, entryType, showIconsForFavoriteEntryTypes );
	}

	private void initFavorites( final SqlIdsSelectQuery selectQuery, final int userId, final PhotoListModel model, final PagingModel pagingModel, final FavoriteEntryType entryType, final List<FavoriteEntryType> showIconsForFavoriteEntryTypes ) {
		model.clear();

		final List<Photo> photos = selectDataFromDB( pagingModel, selectQuery );

		final User user = userService.load( userId );

		final String listTitle = String.format( "%s: %s", entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), translatorService.translate( entryType.getName(), EnvironmentContext.getLanguage() ) );

		final PhotoList photoList = new PhotoList( photoService.getPhotoInfos( photos, showIconsForFavoriteEntryTypes, EnvironmentContext.getCurrentUser() ), listTitle );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( null, false, EnvironmentContext.getCurrentUser() ) );
		model.addPhotoList( photoList );

		model.setPageTitleData( breadcrumbsUserService.getUserFavoriteEntryListBreadcrumbs( user, entryType ) );
		photoList.setPhotosInLine( utilsService.getPhotosInLine( EnvironmentContext.getCurrentUser() ) );
	}

	private List<Photo> selectDataFromDB( final PagingModel pagingModel, final SqlIdsSelectQuery selectIdsQuery ) {
		final SqlSelectIdsResult selectResult = photoService.load( selectIdsQuery );
		pagingModel.setTotalItems( selectResult.getRecordQty() );

		return photoService.load( selectResult.getIds() );
	}
}
