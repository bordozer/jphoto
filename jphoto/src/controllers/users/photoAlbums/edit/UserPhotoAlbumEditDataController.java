package controllers.users.photoAlbums.edit;

import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.SecurityService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import utils.NumberUtils;
import core.services.utils.UrlUtilsService;
import core.services.pageTitle.PageTitleUserUtilsService;

import javax.validation.Valid;

@SessionAttributes( UserPhotoAlbumEditDataController.MODEL_NAME )
@Controller
@RequestMapping( "members/{userId}/albums" )
public class UserPhotoAlbumEditDataController {

	public static final String MODEL_NAME = "userPhotoAlbumEditDataModel";
	private static final String VIEW = "users/photoAlbums/edit/PhotoAlbumEditData";

	@Autowired
	private UserPhotoAlbumEditDataValidator userPhotoAlbumEditDataValidator;

	@Autowired
	private UserService userService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PageTitleUserUtilsService pageTitleUserUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@InitBinder
	private void initBinder( final WebDataBinder binder ) {
		binder.setValidator( userPhotoAlbumEditDataValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public UserPhotoAlbumEditDataModel prepareModel() {
		return new UserPhotoAlbumEditDataModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String albumNew( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( MODEL_NAME ) UserPhotoAlbumEditDataModel model ) {

		assertSecurityPassed( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		model.clear();

		final User user = userService.load( userId );
		model.setUser( user );

		model.setPageTitleData( pageTitleUserUtilsService.getUserPhotoAlbumsNew( user ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{albumId}/edit/" )
	public String albumEdit( final @PathVariable( "userId" ) String _userId, final @PathVariable( "albumId" ) int albumId, final @ModelAttribute( MODEL_NAME ) UserPhotoAlbumEditDataModel model ) {

		assertSecurityPassed( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		model.clear();

		final User user = userService.load( userId );
		model.setUser( user );

		final UserPhotoAlbum photoAlbum = userPhotoAlbumService.load( albumId );
		model.setAlbumId( photoAlbum.getId() );
		model.setAlbumName( photoAlbum.getName() );
		model.setAlbumDescription( photoAlbum.getDescription() );

		model.setPageTitleData( pageTitleUserUtilsService.getUserPhotoAlbumsEdit( photoAlbum ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String albumSave( @Valid final @ModelAttribute( MODEL_NAME ) UserPhotoAlbumEditDataModel model, final BindingResult result ) {
		final User user = model.getUser();

		assertSecurityPassed( String.valueOf( user.getId() ) );

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final UserPhotoAlbum userTeamMember = initUserPhotoAlbumFromModel( model );

		if ( ! userPhotoAlbumService.save( userTeamMember ) ) {
			return VIEW;
		}

		return String.format( "redirect:%s", urlUtilsService.getUserPhotoAlbumListLink( user.getId() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{albumId}/delete/" )
	public String albumDelete( final @PathVariable( "userId" ) String _userId, final @PathVariable( "albumId" ) int albumId, final @ModelAttribute( MODEL_NAME ) UserPhotoAlbumEditDataModel model ) {

		assertSecurityPassed( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		userPhotoAlbumService.delete( albumId );

		return String.format( "redirect:%s", urlUtilsService.getUserPhotoAlbumListLink( userId ) );
	}

	private UserPhotoAlbum initUserPhotoAlbumFromModel( final UserPhotoAlbumEditDataModel model ) {
		final UserPhotoAlbum result = new UserPhotoAlbum();

		result.setId( model.getAlbumId() );
		result.setUser( model.getUser() );
		result.setName( model.getAlbumName() );
		result.setDescription( model.getAlbumDescription() );

		return result;
	}

	private void assertSecurityPassed( final String _userId ) {
		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );
		securityService.assertUserEqualsToCurrentUser( userId );
	}
}
