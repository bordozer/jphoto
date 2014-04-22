package ui.controllers.users.photoAlbums.photos;

import core.general.base.PagingModel;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.entry.GroupOperationService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import core.services.utils.sql.PhotoSqlHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.context.EnvironmentContext;
import ui.elements.PhotoList;
import ui.services.PhotoUIService;
import ui.services.UtilsService;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import utils.NumberUtils;
import utils.PagingUtils;
import utils.StringUtilities;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping( "members/{userId}/albums/{albumId}" )
public class UserPhotoAlbumPhotosController {

	private static final String MODEL_NAME = "userPhotoAlbumPhotosModel";
	private static final String VIEW = "users/photoAlbums/photos/PhotoAlbumPhotos";

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoUIService photoUIService;

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private PhotoSqlHelperService photoSqlHelperService;

	@Autowired
	private GroupOperationService groupOperationService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserService userService;

	@Autowired
	private Services services;

	@ModelAttribute( MODEL_NAME )
	public UserPhotoAlbumPhotosModel prepareModel() {
		return new UserPhotoAlbumPhotosModel();
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( utilsService.getPhotosOnPage( EnvironmentContext.getCurrentUser() ) );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String albumPhotos( final @PathVariable( "userId" ) String _userId, final @PathVariable( "albumId" ) int albumId, final @ModelAttribute( MODEL_NAME ) UserPhotoAlbumPhotosModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );
		final User user = userService.load( userId );

		final UserPhotoAlbum photoAlbum = userPhotoAlbumService.load( albumId );

		model.setPhotoAlbum( photoAlbum );

		final String title = String.format( "Photo album %s", StringUtilities.escapeHtml( photoAlbum.getName() ) );

		final SqlIdsSelectQuery selectIdsQuery = photoSqlHelperService.getUserPhotoAlbumLastPhotosQuery( photoAlbum.getUser().getId(), photoAlbum.getId(), pagingModel );
		final SqlSelectIdsResult selectIdsResult = photoService.load( selectIdsQuery );

		final PhotoList photoList = new PhotoList( selectIdsResult.getIds(), title );
		photoList.setPhotosInLine( utilsService.getPhotosInLine( EnvironmentContext.getCurrentUser() ) );
		photoList.setBottomText( photoAlbum.getDescription() );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getUserCardCustomPhotoListPhotoGroupOperationMenuContainer( user, EnvironmentContext.getCurrentUser() ) );

		model.setPhotoList( photoList );

		pagingModel.setTotalItems( selectIdsResult.getRecordQty() );

		model.setPageTitleData( breadcrumbsUserService.getUserPhotoAlbumPhotosBreadcrumbs( photoAlbum ) );

		return VIEW;
	}
}
