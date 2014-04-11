package core.services.user;

import core.exceptions.UserRequestSecurityException;
import core.general.user.User;
import core.general.user.UsersSecurity;
import core.services.dao.UsersSecurityDao;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.NetworkUtils;
import core.services.validation.DataRequirementService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import ui.context.EnvironmentContext;
import ui.controllers.users.edit.UserEditDataModel;
import utils.FormatUtils;
import utils.UserUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsersSecurityServiceImpl implements UsersSecurityService {

	public static final String AUTHORIZATION_KEY_COOKIE = "AuthorizationKeyCookie";
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";

	@Autowired
	private UsersSecurityDao usersSecurityDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private NetworkUtils networkUtils;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private DataRequirementService dataRequirementService;

	@Autowired
	private UserService userService;

	@Override
	public boolean isUserPasswordCorrect( final User user, final String password ) {
		UsersSecurity usersSecurity = usersSecurityDao.load( user );

		// TODO: create an entry with default password -->
		if ( usersSecurity == null ) {
			usersSecurity = new UsersSecurity( user );
			usersSecurityDao.createEntry( user.getId(), getCryptPassword( usersSecurity.getUserPassword() ) );

			usersSecurity = usersSecurityDao.load( user );
		}
		// TODO: create an entry with default password <--

		return usersSecurity.getUserPassword().equals( getCryptPassword( password ) );
	}

	@Override
	public UsersSecurity load( final User user ) {
		return usersSecurityDao.load( user );
	}

	@Override
	public void registerUserLogin( final User user, final HttpServletRequest request ) {
		final UsersSecurity usersSecurity = usersSecurityDao.load( user );

		usersSecurity.setLastLoginIp( networkUtils.getClientIP( request ) );
		usersSecurity.setLastLoginTime( dateUtilsService.getCurrentTime() );

		usersSecurity.setAuthorizationKey( buildAuthorizationKey( usersSecurity ) );

		usersSecurityDao.save( usersSecurity );
	}

	@Override
	public void resetEnvironmentAndLogOutUser( final User user ) {

		if ( UserUtils.isLoggedUser( user ) ) {
			resetStoredAuthorizationKey( user );
		}

		switchUserToNotLoggedUser();
	}

	@Override
	public void assertLoggedUserRequestSecurityPassed( final User user, final HttpServletRequest request ) {

		if ( ! UserUtils.isLoggedUser( user.getId() ) ) {
			return;
		}

		final UsersSecurity usersSecurity = usersSecurityDao.load( user );

		if ( usersSecurity == null ) {
			switchUserToNotLoggedUser();
			return;
		}

		final String requestAuthorizationKey = getRequestAuthorizationKeyCookieValue( request );

		final String storedAuthorizationKey = usersSecurity.getAuthorizationKey();
		if ( !storedAuthorizationKey.equals( requestAuthorizationKey ) ) {

			resetEnvironmentAndLogOutUser( user );

			throw new UserRequestSecurityException( storedAuthorizationKey, requestAuthorizationKey );
		}
	}

	@Override
	public Date getLastUserActivityTime( final int userId ) {
		return usersSecurityDao.getLastUserActivityTime( userId );
	}

	@Override
	public void saveLastUserActivityTime( final int userId, final Date time ) {
		usersSecurityDao.saveLastUserActivityTime( userId, time );
	}

	@Override
	public void saveLastUserActivityTimeNow( final int userId ) {
		usersSecurityDao.saveLastUserActivityTime( userId, dateUtilsService.getCurrentTime() );
	}

	@Override
	public void createEntry( final int userId, final String uncryptedPassword ) {
		usersSecurityDao.createEntry( userId, getCryptPassword( uncryptedPassword ) );
	}

	@Override
	public void validatePasswordCreation( final String password, final String confirmPassword, final Errors errors ) {

		final Language language = EnvironmentContext.getLanguage();

		if ( StringUtils.isEmpty( password ) ) {
			errors.rejectValue( UserEditDataModel.USER_PASSWORD_FORM_CONTROL, translatorService.translate( "$1 should not be empty", language, FormatUtils.getFormattedFieldName( "Password" ) ) );
			return;
		}

		if ( password.length() < UserEditDataModel.MIN_PASSWORD_LENGTH || password.length() > UserEditDataModel.MAX_PASSWORD_LENGTH ) {
			final String mess = translatorService.translate( "$1 must have length at least $1 characters and maximum of $2", language, FormatUtils.getFormattedFieldName( "Password" ), String.valueOf( UserEditDataModel.MIN_PASSWORD_LENGTH ), String.valueOf( UserEditDataModel.MAX_PASSWORD_LENGTH ) );
			errors.rejectValue( UserEditDataModel.USER_PASSWORD_FORM_CONTROL, mess );
			return;
		}

		final Pattern pattern = Pattern.compile( PASSWORD_PATTERN );
		final Matcher matcher = pattern.matcher( password );
		if ( ! matcher.matches() ) {
			final StringBuilder builder = new StringBuilder();
			builder.append( translatorService.translate( "$1 is too simple!", language, FormatUtils.getFormattedFieldName( "Password" ) ) );
			builder.append( DataRequirementService.HINT_LINE_BREAK );
			builder.append( dataRequirementService.getUserRequirement().getPasswordRequirement( false ) );

			errors.rejectValue( UserEditDataModel.USER_PASSWORD_FORM_CONTROL, builder.toString() );
			return;
		}

		if ( StringUtils.isEmpty( confirmPassword ) ) {
			errors.rejectValue( UserEditDataModel.USER_CONFIRM_PASSWORD_FORM_CONTROL
				, translatorService.translate( "$1 should not be empty.", language, FormatUtils.getFormattedFieldName( "Confirm password" ) ) );
			return;
		}

		if ( ! password.equals( confirmPassword )  ) {
			errors.rejectValue( UserEditDataModel.USER_CONFIRM_PASSWORD_FORM_CONTROL
				, translatorService.translate( "$1 are not equal.", language, FormatUtils.getFormattedFieldName( "Passwords" ) ) );
		}
	}

	@Override
	public void changeUserPassword( final User user, final String password ) {
		usersSecurityDao.changeUserPassword( user.getId(), getCryptPassword( password ) );
	}

	@Override
	public String getStoredUserAuthorizationKey( final User user ) {
		return usersSecurityDao.load( user ).getAuthorizationKey();
	}

	private void switchUserToNotLoggedUser() {
		EnvironmentContext.switchUser( userService.getNotLoggedTemporaryUser() );
	}

	private void resetStoredAuthorizationKey( final User user ) {
		final UsersSecurity usersSecurity = usersSecurityDao.load( user );

		usersSecurity.setAuthorizationKey( StringUtils.EMPTY );

		usersSecurityDao.save( usersSecurity );
	}

	private String buildAuthorizationKey( final UsersSecurity usersSecurity ) {
		return String.format( "AuthorizationKey_%d_%s_%s_%s"
			, usersSecurity.getUser().getId()
			, usersSecurity.getUserPassword()
			, usersSecurity.getLastLoginIp()
			, usersSecurity.getLastLoginTime()
		);
	}

	private String getCryptPassword( final String password ) {
		return String.format( "CRYPT_%s", password ); //DigestUtils.md5Hex( password ); org.apache.commons.codec.digest
	}

	private String getRequestAuthorizationKeyCookieValue( final HttpServletRequest request ) {
		for ( final Cookie cookie : request.getCookies() ) {
			if ( cookie.getName().equals( AUTHORIZATION_KEY_COOKIE ) ) {
				return cookie.getValue();
			}
		}

		return StringUtils.EMPTY;
	}
}
