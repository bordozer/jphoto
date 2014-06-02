package ui.services.page;

import admin.services.scheduler.ScheduledTasksExecutionService;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UsersSecurity;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsService;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceType;
import ui.context.EnvironmentContext;
import ui.controllers.users.login.UserLoginModel;
import ui.elements.MenuItem;
import ui.elements.PageModel;
import ui.elements.PageTitleData;
import ui.services.MenuService;
import ui.services.page.icons.TitleIconLoader;
import utils.StringUtilities;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class PageTemplateServiceImpl implements PageTemplateService {

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private GenreService genreService;

	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SystemVarsService systemVarsService;
	
	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private ScheduledTasksExecutionService scheduledTasksExecutionService;

	@Autowired
	private Services services;

	private final LogHelper log = new LogHelper( PageTemplateServiceImpl.class );

	@Override
	public String renderPageHeader( final PageModel pageModel ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		final Language language = currentUser.getLanguage();

		final PageTitleData dataPage = pageModel.getPageTitleData();

		final String pageTitle = dataPage.getTitle();
		final String pageHeader = dataPage.getHeader();
		final String breadcrumbs = dataPage.getBreadcrumbs();

		final ToolManager manager = new ToolManager( true );
		final Context context = manager.createContext();

		final VelocityContext model = new VelocityContext( context );
		StringWriter writer = new StringWriter();

		model.put( "title", pageTitle );
		model.put( "pageHeader", pageHeader );

		model.put( "baseUrl", urlUtilsService.getBaseURL() );
		model.put( "imagePath", urlUtilsService.getSiteImagesPath() );

		model.put("StringUtils", StringUtils.class);

		model.put( "portalpageurl", urlUtilsService.getPortalPageURL() );
		model.put( "registerurl", urlUtilsService.getUserNewLink() );

		model.put( "progectname", systemVarsService.getProjectName() );
		model.put( "slogan", translatorService.translate( "System slogan: Post your soul here", language ) );

		final Map<MenuItem,List<MenuItem>> menuElements = menuService.getMenuElements( currentUser );
		final int menuElementsQty = menuElements.size();
		model.put( "menuElements", menuElements );
		model.put( "menuElementsQty", menuElementsQty );
		model.put( "menuWidth", menuElementsQty * 15 );
		model.put( "breadcrumbs", breadcrumbs );

		model.put( "loginFormAction", urlUtilsService.getUserLoginLink() );
		model.put( "showLoginForm", pageModel.isShowLoginForm() );
		model.put( "loginFormLogin", translatorService.translate( "Login", language ) );
		model.put( "rememberMeText", translatorService.translate( "Remember me", language ) );

		model.put( "loginFormPassword", translatorService.translate( "Password", language ) );
		model.put( "defaultDebugPassword", UsersSecurity.DEFAULT_DEBUG_PASSWORD );
		model.put( "loginFormLoginControl", UserLoginModel.LOGIN_FORM_LOGIN_CONTROL );
		model.put( "loginFormPasswordControl", UserLoginModel.LOGIN_FORM_PASSWORD_CONTROL );
		model.put( "loginUserAutomaticallyControl", UserLoginModel.LOGIN_FORM_LOGIN_USER_AUTOMATICALLY_CONTROL );

		model.put( "logoutText", translatorService.translate( "Logout", language ) );

		model.put( "loggedUserId", currentUser.getId() );
		model.put( "loggedUserName", StringUtilities.escapeHtml( currentUser.getName() ) );
		model.put( "loggedUserCardUrl", urlUtilsService.getUserCardLink( currentUser.getId() ) );
		model.put( "loggedUserCardUrlTitle", translatorService.translate( "Login info: Your card", language ) );

		model.put( "uploadPhotoText", translatorService.translate( "Main menu: Upload photo", language ) );
		model.put( "baseAdminPrefix", urlUtilsService.getBaseAdminURL() );

		model.put( "titleIcons", TitleIconLoader.getTitleIcons( services, scheduledTasksExecutionService ) );

		final String hiMessage = EnvironmentContext.getHiMessage();
		if ( StringUtils.isNotEmpty( hiMessage ) ) {
			model.put( "hiMessage", StringUtilities.escapeHtml( hiMessage ) );
			EnvironmentContext.getEnvironment().setHiMessage( StringUtils.EMPTY );
		}

		// TODO: TEMP -->
		addLoginSelectBox( model );
		// TODO: TEMP <--

		final List<Genre> genres = genreService.loadAllSortedByNameForLanguage( language );
		final List<String> genreLinks = newArrayList();
		for ( Genre genre : genres ) {
			genreLinks.add( entityLinkUtilsService.getPhotosByGenreLink( genre, language ) );
		}
		model.put( "genres", genres );
		model.put( "genreLinks", genreLinks );

		model.put( "pageHatMaxWidth", EnvironmentContext.getDeviceType() == DeviceType.MOBILE ? "400px" : "950px"  ); // TODO: should be a separate view for mobile devices

		fillUiLanguages( language, model );

		final Template template = velocityEngine.getTemplate( "pageheader.vm" );
		template.merge( model, writer );

		return writer.toString();
	}

	private void addLoginSelectBox( final VelocityContext model ) {
		final List<User> users = userService.loadAll();
		final List<UserLogin> userLogins = newArrayList();
		for ( final User user : users ) {
			String role = user.getUserStatus().getName().substring( 0, 1 );
			if ( securityService.isSuperAdminUser( user.getId() ) ) {
				role = "SA";
			}
			userLogins.add( new UserLogin( String.format( "%s (%s)", StringUtilities.escapeHtml( StringUtilities.truncateString( user.getName(), 13 ) ), role ), user.getLogin() ) );
		}
		Collections.sort( userLogins, new Comparator<UserLogin>() {
			@Override
			public int compare( final UserLogin o1, final UserLogin o2 ) {
				return StringUtilities.unescapeHtml( o1.getName() ).compareToIgnoreCase( StringUtilities.unescapeHtml( o2.getName() ) );
			}
		} );
		model.put( "userLogins", userLogins );
	}

	private void fillUiLanguages( final Language language, final VelocityContext model ) {
		// TODO: consider using GenericTranslatableList.languageTranslatableList( systemVarsService.getActiveLanguages(), translatorService )
		final List<LanguageWrapper> uiLanguages = newArrayList();
		for ( final Language lang : systemVarsService.getActiveLanguages() ) {
			final LanguageWrapper wrapper = new LanguageWrapper( lang );
			wrapper.setTitle( translatorService.translate( lang.getName(), lang ) );
			if ( language == lang ) {
				wrapper.setStyle( "selectedLanguage" );
				wrapper.setTitle( translatorService.translate( "$1 - selected UI language", lang, translatorService.translate( lang.getName(), lang ) ) );
			}
			uiLanguages.add( wrapper );
		}
		model.put( "uiLanguages", uiLanguages );
	}

	@Override
	public String renderPageFooter() {
		Template template = velocityEngine.getTemplate( "pagefooter.vm" );

		VelocityContext model = new VelocityContext();
		StringWriter writer = new StringWriter();

//		model.put( "title", pageTitle );

		template.merge( model, writer );

		return writer.toString();
	}
}
