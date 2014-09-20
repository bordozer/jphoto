package ui.controllers.users.photoAlbums.list;

import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.SecurityService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import utils.NumberUtils;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Controller
@RequestMapping( "members/{userId}/albums/" )
public class UserPhotoAlbumListController {

	private static final String MODEL_NAME = "userPhotoAlbumListModel";
	private static final String VIEW = "users/photoAlbums/list/PhotoAlbumList";

	@Autowired
	private UserService userService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@ModelAttribute( MODEL_NAME )
	public UserPhotoAlbumListModel prepareModel( final @PathVariable( "userId" ) String _userId ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );
		final User user = userService.load( userId );

		final UserPhotoAlbumListModel model = new UserPhotoAlbumListModel();
		model.setUser( user );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String albumList( final @ModelAttribute( MODEL_NAME ) UserPhotoAlbumListModel model ) {

		final User user = model.getUser();

		securityService.assertUserEqualsToCurrentUser( user );

		final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadAllForEntry( user.getId() );
		model.setUserPhotoAlbums( userPhotoAlbums );

		final Map<Integer, Integer> userPhotoAlbumsQtyMap = newLinkedHashMap();
		for ( final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums ) {
			userPhotoAlbumsQtyMap.put( userPhotoAlbum.getId(), userPhotoAlbumService.getUserPhotoAlbumPhotosQty( userPhotoAlbum.getId() ) );
		}
		model.setUserPhotoAlbumsQtyMap( userPhotoAlbumsQtyMap );

		model.setPageTitleData( breadcrumbsUserService.getUserPhotoAlbumListBreadcrumbs( user ) );

		return VIEW;
	}
}
