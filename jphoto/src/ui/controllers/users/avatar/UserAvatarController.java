package ui.controllers.users.avatar;

import ui.context.EnvironmentContext;
import core.general.img.Dimension;
import core.general.user.User;
import core.log.LogHelper;
import ui.services.breadcrumbs.BreadcrumbsUserService;
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
	private UserAvatarModel prepareModel() {
		return new UserAvatarModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showAvatar( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( MODEL_NAME ) UserAvatarModel model ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final User user = userService.load( userId );

		securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), user );

		model.setUser( user );

		final File userAvatarFile = userPhotoFilePathUtilsService.getUserAvatarFile( userId );
		if ( userAvatarFile.exists() ) {
			model.setCurrentAvatarFile( userAvatarFile );
		}

		model.getPageModel().setPageTitleData( breadcrumbsUserService.setUserAvatarBreadcrumbs( model.getUser() ) );

		model.setDimension( getAvatarDimension( model.getCurrentAvatarFile() ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String uploadAvatar( final @PathVariable( "userId" ) String _userId, @Valid final @ModelAttribute( MODEL_NAME ) UserAvatarModel model, final BindingResult result ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final User user = userService.load( userId );
		securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), user );

		model.setUser( user );

		model.setBindingResult( result );

		model.getPageModel().setPageTitleData( breadcrumbsUserService.setUserAvatarBreadcrumbs( model.getUser() ) );

		model.setDimension( getAvatarDimension( model.getCurrentAvatarFile() ) );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final MultipartFile multipartFile = model.getAvatarFile();

		try {
			final File tmpAvatarFile = tempFileUtilsService.getTempFile();

			multipartFile.transferTo( tmpAvatarFile );

			userService.saveAvatar( userId, tmpAvatarFile );
		} catch ( IOException e ) {
			final Language language = EnvironmentContext.getLanguage();
			result.reject( translatorService.translate( "Saving data error", language ), translatorService.translate( "Can not copy file", language ) );
			log.error( e );
		}

		return String.format( "redirect:%s", urlUtilsService.getEditUserAvatarLink( userId ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/delete/" )
	public String deleteAvatar( final @PathVariable( "userId" ) int userId, final @ModelAttribute( MODEL_NAME ) UserAvatarModel model ) {

		securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), model.getUser() );

		final File userAvatarFile = userPhotoFilePathUtilsService.getUserAvatarFile( userId );

		if ( userAvatarFile.exists() ) {
			final boolean isDeleted = org.apache.commons.io.FileUtils.deleteQuietly( userAvatarFile );
			if ( isDeleted ) {
				model.setAvatarFile( null );
				model.setCurrentAvatarFile( null );
			}
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
		return new Dimension( 1, 1 );
	}
}
