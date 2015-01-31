package ui.controllers.users.avatar;

import core.general.img.Dimension;
import core.general.user.User;
import core.log.LogHelper;
import core.services.security.SecurityService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.ImageFileUtilsService;
import core.services.utils.TempFileUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import utils.NumberUtils;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping( "members/{userId}/avatar" )
public class UserAvatarController {

	private static final String VIEW = "users/avatar/UserAvatar";

	private static final String MODEL_NAME = "userAvatarModel";

	@Autowired
	private UserService userService;

	@Autowired
	private UserAvatarValidator userAvatarValidator;

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private TempFileUtilsService tempFileUtilsService;
	
	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	@Autowired
	private TranslatorService translatorService;

	private LogHelper log = new LogHelper( UserAvatarController.class );

	@InitBinder
	private void initBinder( final WebDataBinder binder ) {
		binder.setValidator( userAvatarValidator );
	}

	@ModelAttribute( MODEL_NAME )
	private UserAvatarModel prepareModel( final @PathVariable( "userId" ) String _userId ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final User user = userService.load( userId );

		securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), user );

		final UserAvatarModel model = new UserAvatarModel();

		model.setUser( user );
		model.getPageModel().setPageTitleData( breadcrumbsUserService.setUserAvatarBreadcrumbs( model.getUser() ) );
		model.setDimension( getAvatarDimension( model.getCurrentAvatarFile() ) );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showAvatar( final @ModelAttribute( MODEL_NAME ) UserAvatarModel model ) {


		final File userAvatarFile = userPhotoFilePathUtilsService.getUserAvatarFile( model.getUser().getId() );
		if ( userAvatarFile.exists() ) {
			model.setCurrentAvatarFile( userAvatarFile );
		}

		model.getPageModel().setPageTitleData( breadcrumbsUserService.setUserAvatarBreadcrumbs( model.getUser() ) );

		model.setDimension( getAvatarDimension( model.getCurrentAvatarFile() ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String uploadAvatar( @Valid final @ModelAttribute( MODEL_NAME ) UserAvatarModel model, final BindingResult result ) {

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final int userId = model.getUser().getId();

		final MultipartFile multipartFile = model.getAvatarFile();

		try {
			final File tmpAvatarFile = tempFileUtilsService.getTempFile();

			multipartFile.transferTo( tmpAvatarFile );

			userService.saveAvatar( userId, tmpAvatarFile );
		} catch ( final IOException e ) {
			final Language language = EnvironmentContext.getLanguage();
			result.reject( translatorService.translate( "Saving data error", language ), translatorService.translate( "Can not copy file", language ) );
			log.error( e );
		}

		return String.format( "redirect:%s", urlUtilsService.getEditUserAvatarLink( userId ) );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/delete/" )
	public String deleteAvatar( final @PathVariable( "userId" ) int userId, final @ModelAttribute( MODEL_NAME ) UserAvatarModel model ) {

		securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), model.getUser() );

		try {
			final boolean isDeleted = userService.deleteAvatar( userId );
			if ( isDeleted ) {
				model.setAvatarFile( null );
				model.setCurrentAvatarFile( null );
			}
		} catch ( final IOException e ) {
			log.error( String.format( "Error deleting user avatar: %d", userId ), e );
		}

		return VIEW;
	}

	private Dimension getAvatarDimension( final File avatarFile ) {
		if ( avatarFile != null ) {
			try {
				return imageFileUtilsService.getImageDimension( avatarFile );
			} catch ( IOException e ) {
				log.error( String.format( "Error reading avatar dimension: '%s'", avatarFile ) );
			}
		}
		return null; //new Dimension( 1, 1 );
	}
}
