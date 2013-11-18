package controllers.users.bookmarks;

import core.general.base.PagingModel;
import core.context.EnvironmentContext;
import core.enums.FavoriteEntryType;
import core.general.photo.Photo;
import core.general.user.User;
import controllers.photos.list.PhotoListModel;
import core.services.entry.GroupOperationService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import core.services.utils.UtilsService;
import elements.PhotoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import utils.TranslatorUtils;
import utils.*;
import core.services.utils.sql.PhotoSqlHelperService;
import core.services.pageTitle.PageTitleUserUtilsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping( UrlUtilsServiceImpl.USERS_URL )
public class UserBookmarksController {

	private static final String PHOTO_LIST_VIEW = "photos/list/PhotoList";

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PageTitleUserUtilsService pageTitleUserUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private PhotoSqlHelperService photoSqlHelperService;

	@Autowired
	private GroupOperationService groupOperationService;

	@ModelAttribute( "photoListModel" )
	public PhotoListModel prepareModel() {
		final PhotoListModel model = new PhotoListModel();

		model.setDeviceType( EnvironmentContext.getDeviceType() );

		return model;
	}

	@ModelAttribute
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel();
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( utilsService.getPhotosOnPage( EnvironmentContext.getCurrentUser() ) );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{userId}/favorites/photos/" )
	public String showFavoritePhotos( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "photoListModel" ) PhotoListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		initFavorites( userId, model, pagingModel, FavoriteEntryType.PHOTO );
		return PHOTO_LIST_VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{userId}/bookmark/" )
	public String showBookmarkedPhotos( @PathVariable( "userId" ) String _userId, @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		initFavorites( userId, model, pagingModel, FavoriteEntryType.BOOKMARK );
		return PHOTO_LIST_VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{userId}/notification/comments/" )
	public String showPhotosWithNewCommentNotification( @PathVariable( "userId" ) String _userId, @ModelAttribute( "photoListModel" ) PhotoListModel model, @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		initFavorites( userId, model, pagingModel, FavoriteEntryType.NEW_COMMENTS_NOTIFICATION );
		return PHOTO_LIST_VIEW;
	}

	private void initFavorites( final int userId, final PhotoListModel model, final PagingModel pagingModel, final FavoriteEntryType entryType ) {
		model.clear();

		final SqlIdsSelectQuery selectQuery = photoSqlHelperService.getFavoritesPhotosSQL( pagingModel, userId, entryType );
		final List<Photo> photos = selectDataFromDB( pagingModel, selectQuery );

		final User user = userService.load( userId );

		final String listTitle = TranslatorUtils.translate( "$1: $2", entityLinkUtilsService.getUserCardLink( user ), entryType.getName() );
		final PhotoList photoList = new PhotoList( photoService.getPhotoInfos( photos, EnvironmentContext.getCurrentUser() ), listTitle );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( null, false, EnvironmentContext.getCurrentUser() ) );
		model.addPhotoList( photoList );

		model.setPageTitleData( pageTitleUserUtilsService.getFavoriteEntry( user, entryType ) );
		photoList.setPhotosInLine( utilsService.getPhotosInLine( EnvironmentContext.getCurrentUser() ) );
	}

	private List<Photo> selectDataFromDB( final PagingModel pagingModel, final SqlIdsSelectQuery selectIdsQuery ) {
		final SqlSelectIdsResult selectResult = photoService.load( selectIdsQuery );
		pagingModel.setTotalItems( selectResult.getRecordQty() );

		return photoService.load( selectResult.getIds() );
	}
}
