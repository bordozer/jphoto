package core.services.system;

import admin.services.scheduler.ScheduledTasksExecutionService;
import controllers.users.login.UserLoginModel;
import core.context.EnvironmentContext;
import core.enums.PrivateMessageType;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UsersSecurity;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.entry.PrivateMessageService;
import core.services.photo.PhotoCommentService;
import core.services.security.SecurityService;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsService;
import elements.menus.MenuItem;
import elements.PageModel;
import elements.PageTitleData;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceType;
import utils.StringUtilities;
import utils.UserUtils;

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
	private PhotoCommentService photoCommentService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private PrivateMessageService privateMessageService;

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

	private final LogHelper log = new LogHelper( PageTemplateServiceImpl.class );

	@Override
	public String renderPageHeader( final PageModel pageModel ) {
		final User currentUser = EnvironmentContext.getCurrentUser(); // TODO: EnvironmentContext in a service!

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

		model.put( "workername", systemVarsService.getTomcatWorkerName() );
		model.put( "siteUrlWithPrefix", urlUtilsService.getBaseURLWithPrefix() );
		model.put( "imagePath", urlUtilsService.getSiteImagesPath() );

		model.put( "portalpageurl", urlUtilsService.getBaseURLWithPrefixClosed() );
		model.put( "registerurl", urlUtilsService.getUserNewLink() );

		model.put( "progectname", systemVarsService.getProjectName() );
		model.put( "slogan", translatorService.translate( "Post your soul here" ) );

		final Map<MenuItem,List<MenuItem>> menuElements = menuService.getMenuElements( currentUser );
		final int menuElementsQty = menuElements.size();
		model.put( "menuElements", menuElements );
		model.put( "menuElementsQty", menuElementsQty );
		model.put( "menuWidth", menuElementsQty * 15 );
		model.put( "breadcrumbs", breadcrumbs );

		model.put( "loginFormAction", urlUtilsService.getUserLoginLink() );
		model.put( "showLoginForm", pageModel.isShowLoginForm() );
		model.put( "loginFormLogin", translatorService.translate( "Login" ) );
		model.put( "rememberMeText", translatorService.translate( "Remember me" ) );

		model.put( "loginFormPassword", translatorService.translate( "Password" ) );
		model.put( "defaultDebugPassword", UsersSecurity.DEFAULT_DEBUG_PASSWORD );
		model.put( "loginFormLoginControl", UserLoginModel.LOGIN_FORM_LOGIN_CONTROL );
		model.put( "loginFormPasswordControl", UserLoginModel.LOGIN_FORM_PASSWORD_CONTROL );
		model.put( "loginUserAutomaticallyControl", UserLoginModel.LOGIN_FORM_LOGIN_USER_AUTOMATICALLY_CONTROL );

		model.put( "logoutText", translatorService.translate( "Logout" ) );

		model.put( "loggedUserId", currentUser.getId() );
		model.put( "loggedUserName", StringUtilities.escapeHtml( currentUser.getName() ) );
		model.put( "loggedUserCardUrl", urlUtilsService.getUserCardLink( currentUser.getId() ) );
		model.put( "loggedUserCardUrlTitle", translatorService.translate( "Your card" ) );

		model.put( "uploadPhotoText", translatorService.translate( "Upload photo" ) );
		model.put( "isSuperAdminUser", securityService.isSuperAdminUser( currentUser ) );
		model.put( "baseAdminPrefix", urlUtilsService.getAdminBaseURLWithPrefix() );

		try {
			model.put( "isSchedulerRunning", scheduledTasksExecutionService.isRunning() );
		} catch ( final SchedulerException e ) {
			model.put( "isSchedulerRunning", false );
		}
		model.put( "schedulerIsStoppedIcon", String.format( "<img src=\"%s/scheduler/SchedulerIsStopped.png\" height=\"16\" width=\"16\" title=\"%s\" />"
			, urlUtilsService.getSiteImagesPath(), translatorService.translate( "The scheduler is stopped!" ) ) );

		final String hiMessage = EnvironmentContext.getHiMessage();
		if ( StringUtils.isNotEmpty( hiMessage ) ) {
			model.put( "hiMessage", StringUtilities.escapeHtml( hiMessage ) );
			EnvironmentContext.getEnvironment().setHiMessage( StringUtils.EMPTY );
		}

		final int unreadCommentsQty = photoCommentService.getUnreadCommentsQty( currentUser.getId() );
		String unreadCommentsText = StringUtils.EMPTY;
		if ( unreadCommentsQty > 0 ) {
			unreadCommentsText = String.format( "<a href='%1$s' title=\"%2$s\"><img src=\"%3$s/icons16/newComments16.png\"> +%4$s</a>"
				, urlUtilsService.getUnreadCommentsToUserList( currentUser.getId() )
				, translatorService.translate( "You have $1 new comment(s)", unreadCommentsQty )
				, urlUtilsService.getSiteImagesPath()
				, unreadCommentsQty
			);
		}
		model.put( "unreadCommentsQtyText", unreadCommentsText );

		// TODO: TEMP -->
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
		// TODO: TEMP <--

		final List<Genre> genres = genreService.loadAll();
		final List<String> genreLinks = newArrayList();
		for ( Genre genre : genres ) {
			String genreLink = entityLinkUtilsService.getPhotosByGenreLink( genre );
			genreLinks.add( genreLink );
		}
		model.put( "genres", genres );
		model.put( "genreLinks", genreLinks );

		final List<NewPrivateMessage> privateMessages = newArrayList();
		if ( UserUtils.isCurrentUserLoggedUser() ) {
			for ( final PrivateMessageType messageType : PrivateMessageType.values() ) {

				if ( messageType == PrivateMessageType.USER_PRIVATE_MESSAGE_OUT ) {
					continue;
				}

				final int messagesCount = privateMessageService.getNewReceivedPrivateMessagesCount( currentUser.getId(), messageType );

				if ( messagesCount > 0 ) {
					final NewPrivateMessage newPrivateMessage = new NewPrivateMessage( messageType, messagesCount );
					newPrivateMessage.setLink( urlUtilsService.getPrivateMessagesList( currentUser.getId(), messageType ) );
					newPrivateMessage.setHint( String.format( "%s: +%d", messageType.getName(), messagesCount ) );

					privateMessages.add( newPrivateMessage );
				}
			}
		}
		model.put( "newPrivateMessages", privateMessages );

		model.put( "pageHatMaxWidth", EnvironmentContext.getDeviceType() == DeviceType.MOBILE ? "400px" : "800px"  );

		final Template template = velocityEngine.getTemplate( "pageheader.vm" );
		template.merge( model, writer );

		return writer.toString();
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

	public class UserLogin {

		private final String name;
		private final String login;

		private UserLogin( final String name, final String login ) {
			this.name = name;
			this.login = login;
		}

		public String getLogin() {
			return login;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return String.format( "User %s, %s", login, name );
		}
	}

	public class NewPrivateMessage {
		private final PrivateMessageType privateMessageType;
		private final int newMessagesCount;
		private String link;
		private String hint;

		public NewPrivateMessage( final PrivateMessageType privateMessageType, final int newMessagesCount ) {
			this.privateMessageType = privateMessageType;
			this.newMessagesCount = newMessagesCount;
		}

		public PrivateMessageType getPrivateMessageType() {
			return privateMessageType;
		}

		public int getNewMessagesCount() {
			return newMessagesCount;
		}

		public String getLink() {
			return link;
		}

		public void setLink( final String link ) {
			this.link = link;
		}

		public String getHint() {
			return hint;
		}

		public void setHint( final String hint ) {
			this.hint = hint;
		}
	}
}
