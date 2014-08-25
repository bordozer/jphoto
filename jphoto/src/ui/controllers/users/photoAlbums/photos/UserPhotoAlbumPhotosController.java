package ui.controllers.users.photoAlbums.photos;

import core.general.base.PagingModel;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.photo.list.PhotoListFactoryService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.elements.PhotoList;
import ui.services.UtilsService;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import utils.NumberUtils;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping( "members/{userId}/albums/{albumId}" )
public class UserPhotoAlbumPhotosController {

	private static final String MODEL_NAME = "userPhotoAlbumPhotosModel";
	private static final String VIEW = "users/photoAlbums/photos/PhotoAlbumPhotos";

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserService userService;

	@Autowired
	private Services services;

	@Autowired
	private PhotoListFactoryService photoListFactoryService;

	@Autowired
	private DateUtilsService dateUtilsService;

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

		final PhotoList photoList = photoListFactoryService.userAlbumPhotos( user, photoAlbum, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser() )
														   .getPhotoList( photoAlbum.getId(), pagingModel.getCurrentPage(), EnvironmentContext.getLanguage(), dateUtilsService.getCurrentTime() );

		model.setPhotoList( photoList );

		pagingModel.setTotalItems( photoList.getPhotosCount() );

		model.setPageTitleData( breadcrumbsUserService.getUserPhotoAlbumPhotosBreadcrumbs( photoAlbum ) );

		return VIEW;
	}
}
