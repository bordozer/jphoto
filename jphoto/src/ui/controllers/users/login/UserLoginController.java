package ui.controllers.users.login;

import core.general.user.User;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import ui.services.security.UsersSecurityService;
import ui.services.security.UsersSecurityServiceImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping( UrlUtilsServiceImpl.USERS_URL )
public class UserLoginController {

	public static final String USER_LOGIN_MODEL = "userLoginModel";

	public static final String USER_LOGIN_COOKIE = "userLoginCookie";
	public static final int AUTOLOGIN_COOKIE_EXPIRATION_IN_SECONDS = 60 * 60 * 24 * 14;

	@Autowired
	private UserService userService;

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Autowired
	private UserLoginValidator userLoginValidator;

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( userLoginValidator );
	}

	@ModelAttribute( USER_LOGIN_MODEL )
	public UserLoginModel prepareModel() {
		return new UserLoginModel();
	}

	@RequestMapping( method = RequestMethod.POST, value = "login/" )
	public String processLogin( final @Valid @ModelAttribute( USER_LOGIN_MODEL ) UserLoginModel model, final BindingResult result, final HttpServletRequest request, final HttpServletResponse response ) {

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			model.setPageTitleData( breadcrumbsUserService.getUserWrongLoginBreadcrumbs() );
			return "users/login/Login";
		}

		final String userLogin = model.getUserlogin();
		final User user = userService.loadByLogin( userLogin );

		usersSecurityService.registerUserLogin( user, request );
		EnvironmentContext.switchUser( user );

		// Admin can not be set as autologin
		if ( ! securityService.isSuperAdminUser( user.getId() ) ) {
			setCookieForAutoLogin( model, request, response, userLogin );
		}

		addAuthorizationKeyCookie( user, request, response );

		String redirectTo = request.getHeader( "referer" );
		if ( redirectTo.equals( urlUtilsService.getUserLoginLink() ) ) {
			redirectTo = urlUtilsService.getPortalPageURL();
		}

		return String.format( "redirect:%s", redirectTo );
	}

	@RequestMapping( method = RequestMethod.GET, value = "logout/" )
	public String processLogout( final @ModelAttribute( USER_LOGIN_MODEL ) UserLoginModel model, final HttpServletRequest request, final HttpServletResponse response ) {

		final Cookie cookie = new Cookie( UserLoginController.USER_LOGIN_COOKIE, EnvironmentContext.getCurrentUser().getLogin() );
		cookie.setPath( getPath( request ) );
		cookie.setMaxAge( 0 );

		response.addCookie( cookie );

		usersSecurityService.resetEnvironmentAndLogOutUser( EnvironmentContext.getCurrentUser() );

		request.getSession().invalidate();

		return String.format( "redirect:%s", request.getHeader( "referer" ) );
	}

	private void setCookieForAutoLogin( final UserLoginModel model, final HttpServletRequest request, final HttpServletResponse response, final String userLogin ) {

		int expiration = 0; // A zero value causes the cookie to be deleted
		if ( model.isLoginUserAutomatically() ) {
			expiration = AUTOLOGIN_COOKIE_EXPIRATION_IN_SECONDS;
		}

		final Cookie cookie = new Cookie( USER_LOGIN_COOKIE, userLogin );
		cookie.setPath( getPath( request ) );
		cookie.setMaxAge( expiration );

		response.addCookie( cookie );
	}

	private void addAuthorizationKeyCookie( final User user, final HttpServletRequest request, final HttpServletResponse response ) {
		final Cookie cookie = new Cookie( UsersSecurityServiceImpl.AUTHORIZATION_KEY_COOKIE, usersSecurityService.getStoredUserAuthorizationKey( user ) );
		cookie.setPath( getPath( request ) );
		response.addCookie( cookie );
	}

	private String getPath( final HttpServletRequest request ) {
		return StringUtils.isNotEmpty( request.getContextPath() ) ? request.getContextPath() : "/";
	}
}
