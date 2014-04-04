package core.services.system;

import core.context.EnvironmentContext;
import core.enums.FavoriteEntryType;
import core.enums.PrivateMessageType;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import elements.menus.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class MenuServiceImpl implements MenuService {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public Map<MenuItem, List<MenuItem>> getMenuElements( final User user ) {

		final Map<MenuItem, List<MenuItem>> menus = newLinkedHashMap();

		createPhotosMenu( menus );
//		createPhotosByGenreMenu( menus );
		createBestPhotosMenu( menus );
		createMembersMenu( menus );
		createLoggedUserMenu( menus, user );
		createUserBookmarksMenu( menus, user );
		createMessageMenu( menus, user );
		createUserNotificationMenu( menus, user );

		if ( securityService.isSuperAdminUser( user.getId() ) ) {
			createAdminMenu( menus );
		}

		return menus;
	}

	private void createPhotosMenu( final Map<MenuItem, List<MenuItem>> menus ) {
		final List<MenuItem> menuItems = newArrayList();

		final MenuItem menuItem = allPhotosMenu();
		menuItems.add( menuItem );

		menuItems.add( todayPhotos() );
		menuItems.add( yesterdayPhotos() );
		menuItems.add( thisWeekPhotos() );
		menuItems.add( thisMonthPhotos() );
		menuItems.addAll( byMembershipMenus( UrlUtilsServiceImpl.PHOTOS_URL ) );

		menus.put( new MenuItem( menuItem.getCaption(), menuItem.getLink() ), menuItems );
	}

	private void createPhotosByGenreMenu( final Map<MenuItem, List<MenuItem>> menus, final Language language ) {
		final List<MenuItem> menuItems = newArrayList();

		final List<Genre> genres = genreService.loadAll();
		for ( final Genre genre : genres ) {
			final String caption = translatorService.translateGenre( genre, language );
			final String link = urlUtilsService.getPhotosByGenreLink( genre.getId() );
			menuItems.add( new MenuItem( caption, link ) );
		}

		menus.put( new MenuItem( translatorService.translate( "Categories", getLanguage() ), "" ), menuItems );
	}

	private void createBestPhotosMenu( final Map<MenuItem, List<MenuItem>> menus ) {
		final List<MenuItem> menuItems = newArrayList();

		menuItems.add( photosAbsoluteBest() );

		menuItems.add( photosTodayBest() );
		menuItems.add( photosYesterdayBest() );
		final MenuItem bestPhotos = photosPeriodBest( 3 );
		menuItems.add( bestPhotos );
		menuItems.add( photosWeekBest() );
		menuItems.add( photosMonthBest() );
		menuItems.addAll( bestPhotosByMembershipMenus( UrlUtilsServiceImpl.PHOTOS_URL ) );

		final MenuItem menuItem = new MenuItem( translatorService.translate( "Main menu: The best photos", getLanguage() ), bestPhotos.getLink() );

		menus.put( menuItem, menuItems );
	}

	private void createMembersMenu( final Map<MenuItem, List<MenuItem>> menus ) {
		final List<MenuItem> menuItems = newArrayList();

		final MenuItem menuItem = allUsersMenu();
		menuItems.add( menuItem );

		menuItems.addAll( byMembershipMenus( UrlUtilsServiceImpl.USERS_URL ) );
		menuItems.add( getActivityStreamMenu() );

		menus.put( new MenuItem( translatorService.translate( MAIN_MENU_MEMBERS, getLanguage() ), menuItem.getLink() ), menuItems );
	}

	private MenuItem getActivityStreamMenu() {
		final String caption = translatorService.translate( "Main menu: Activity stream", getLanguage() );
		final String link = urlUtilsService.getActivityStreamUrl();
		return new MenuItem( caption, link );
	}

	private void createLoggedUserMenu( final Map<MenuItem, List<MenuItem>> menus, final User user ) {
		final List<MenuItem> menuItems = newArrayList();

		final MenuItem menuItem;
		if ( user.getId() > 0 ) {
			menuItems.add( uploadPhotoMenu() );
			final MenuItem userCardMenu = userCardMenu( user );
			menuItems.add( userCardMenu );
			menuItems.add( userPhotosMenu( user ) );
			menuItems.add( userDataMenu( user ) );
			menuItems.add( changePasswordMenu( user ) );
			menuItems.add( userAvatarMenu( user ) );
			menuItems.add( userTeamMenu( user ) );
			menuItems.add( userPhotoAlbumsMenu( user ) );
			menuItems.add( userTechMenu( user ) );

			final String link = userCardMenu.getLink();
			menuItem = new MenuItem( translatorService.translate( "Main menu: My club", getLanguage() ), link );
		} else {
			menuItem = registerMenu();
			menuItems.add( new MenuItem( translatorService.translate( MAIN_MENU_REGISTER, getLanguage() ), menuItem.getLink() ) );
		}

		menus.put( menuItem, menuItems );
	}

	private void createUserBookmarksMenu( final Map<MenuItem, List<MenuItem>> menus, final User user ) {
		final List<MenuItem> menuItems = newArrayList();

		if ( user.getId() > 0 ) {
			final MenuItem favoritesMenu = userFavoritePhotosMenu( user );
			menuItems.add( favoritesMenu );
			menuItems.add( userBookmarkedPhotosMenu( user ) );
			menuItems.add( userFavoriteMembersMenu( user ) );
			menuItems.add( userFriendsMenu( user ) );
			menuItems.add( userBlackListMenu( user ) );

			final MenuItem menuItem = new MenuItem( translatorService.translate( "Main menu: My bookmarks", getLanguage() ), favoritesMenu.getLink() );
			menus.put( menuItem, menuItems );
		}
	}

	private void createMessageMenu( final Map<MenuItem, List<MenuItem>> menus, final User user ) {
		final List<MenuItem> menuItems = newArrayList();

		if ( user.getId() > 0 ) {
			menuItems.add( getCommentsToMeMenu( user ) );
			menuItems.add( getWrittenCommentsMenu( user ) );
			menuItems.add( getPrivateMessagesReceivedMenu( user ) );
			menuItems.add( getPrivateMessagesSentMenu( user ) );
			menuItems.add( getActivityNotificationMenu( user ) );
			menuItems.add( getSystemNotificationMenu( user ) );

			if ( securityService.isSuperAdminUser( user.getId() ) ) {
				menuItems.add( getAdminNotificationMenu( user ) );
			}

			menus.put( MenuItem.noLinkMenu( translatorService.translate( "Main menu: Messages", getLanguage() ) ), menuItems );

			menuItems.add( userNotificationsControlMenu( user ) );
		}
	}

	private MenuItem getCommentsToMeMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: Comments to me", getLanguage() );
		final String link = urlUtilsService.getCommentsToUserList( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem getWrittenCommentsMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: Written comments", getLanguage() );
		final String link = urlUtilsService.getUserCommentsList( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem getPrivateMessagesReceivedMenu( final User user ) {
		final String caption = translatorService.translate( MAIN_MENU_PRIVATE_MESSAGES_RECEIVED, getLanguage() );
		final String link = urlUtilsService.getPrivateMessagesList( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem getPrivateMessagesSentMenu( final User user ) {
		final String caption = translatorService.translate( MAIN_MENU_PRIVATE_MESSAGES_SENT, getLanguage() );
		final String link = urlUtilsService.getPrivateMessagesList( user.getId(), PrivateMessageType.USER_PRIVATE_MESSAGE_OUT );
		return new MenuItem( caption, link );
	}

	private MenuItem getActivityNotificationMenu( final User user ) {
		final String caption = translatorService.translate( PrivateMessageType.ACTIVITY_NOTIFICATIONS.getName(), getLanguage() );
		final String link = urlUtilsService.getPrivateMessagesList( user.getId(), PrivateMessageType.ACTIVITY_NOTIFICATIONS );
		return new MenuItem( caption, link );
	}

	private MenuItem getSystemNotificationMenu( final User user ) {
		final String caption = translatorService.translate( PrivateMessageType.SYSTEM_NOTIFICATIONS.getName(), getLanguage() );
		final String link = urlUtilsService.getPrivateMessagesList( user.getId(), PrivateMessageType.SYSTEM_NOTIFICATIONS );
		return new MenuItem( caption, link );
	}

	private MenuItem getAdminNotificationMenu( final User user ) {
		final String caption = translatorService.translate( PrivateMessageType.ADMIN_NOTIFICATIONS.getName(), getLanguage() );
		final String link = urlUtilsService.getPrivateMessagesList( user.getId(), PrivateMessageType.ADMIN_NOTIFICATIONS );
		return new MenuItem( caption, link );
	}

	private void createUserNotificationMenu( final Map<MenuItem, List<MenuItem>> menus, final User user ) {
		final List<MenuItem> menuItems = newArrayList();

		if ( user.getId() > 0 ) {
			menuItems.add( getSubscriptionOnNewCommentsMenu( user ) );
			menuItems.add( getSubscriptionOnNewPhotosMenu( user ) );

			menus.put( MenuItem.noLinkMenu( translatorService.translate( "Main menu: My subscription", getLanguage() ) ), menuItems );
		}
	}

	private void createAdminMenu( final Map<MenuItem, List<MenuItem>> menus ) {
		final List<MenuItem> menuItems = newArrayList();

		menuItems.add( configurationMenu() );
		menuItems.add( jobsMenu() );
		menuItems.add( schedulerMenu() );
		menuItems.add( genresMenu() );
		menuItems.add( anonymousDaysMenu() );
		menuItems.add( votingCategoriesMenu() );
		menuItems.add( translatorMenu() );
		menuItems.add( reloadTranslatorMenu() );
		menuItems.add( controlPanelMenu() );
		menuItems.add( upgradeMenu() );

		menus.put( MenuItem.noLinkMenu( translatorService.translate( "Main menu: Administration", getLanguage() ) ), menuItems );
	}

	private List<MenuItem> byMembershipMenus( final String url ) {
		final List<MenuItem> menuItems = newArrayList();

		for ( UserMembershipType membershipType : UserMembershipType.values() ) {
			menuItems.add( membershipMenu( membershipType, url ) );
		}

		return menuItems;
	}

	private List<MenuItem> bestPhotosByMembershipMenus( final String url ) {
		final List<MenuItem> menuItems = newArrayList();

		for ( UserMembershipType membershipType : UserMembershipType.values() ) {
			menuItems.add( membershipBestMenu( membershipType, url ) );
		}

		return menuItems;
	}

	private MenuItem membershipMenu( final UserMembershipType membershipType, final String url ) {
		final String caption = translatorService.translate( String.format( "Main menu: %s: %s", url, membershipType.getName() ), getLanguage() );
		final String link = urlUtilsService.getPhotosByMembership( membershipType, url );
		return new MenuItem( caption, link );
	}

	private MenuItem membershipBestMenu( final UserMembershipType membershipType, final String url ) {
		final String nerd = String.format( "Main menu: The best photos: %s", membershipType.getName() );
		final String caption = translatorService.translate( nerd, getLanguage() );
		final String link = urlUtilsService.getPhotosByMembershipBest( membershipType, url );
		return new MenuItem( caption, link );
	}

	private MenuItem genresMenu() {
		final String caption = translatorService.translate( "Main menu: Photo categories", getLanguage() );
		final String link = urlUtilsService.getAdminGenreListLink();
		return new MenuItem( caption, link );
	}

	private MenuItem votingCategoriesMenu() {
		final String caption = translatorService.translate( "Main menu: Voting Categories", getLanguage() );
		final String link = urlUtilsService.getAdminVotingCategoriesLink();
		return new MenuItem( caption, link );
	}

	private MenuItem translatorMenu() {
		final String caption = translatorService.translate( "Main menu: Translator", getLanguage() );
		final String link = urlUtilsService.getAdminTranslatorLink();
		return new MenuItem( caption, link );
	}

	private MenuItem reloadTranslatorMenu() {
		final String caption = translatorService.translate( "Main menu: Reload translations", getLanguage() );
		return MenuItem.jsFunctionLinkMenu( caption, "reloadTranslations(); return false;" );
	}

	private MenuItem controlPanelMenu() {
		final String caption = translatorService.translate( "Main menu: Control panel", getLanguage() );
		final String link = urlUtilsService.getAdminControlPanelLink();
		return new MenuItem( caption, link );
	}

	private MenuItem jobsMenu() {
		final String caption = translatorService.translate( "Main menu: Jobs", getLanguage() );
		final String link = urlUtilsService.getAdminJobsLink();
		return new MenuItem( caption, link );
	}

	private MenuItem schedulerMenu() {
		final String caption = translatorService.translate( "Main menu: Scheduler", getLanguage() );
		final String link = urlUtilsService.getAdminSchedulerTaskListLink();
		return new MenuItem( caption, link );
	}

	private MenuItem upgradeMenu() {
		final String caption = translatorService.translate( "Main menu: DB upgrade", getLanguage() );
		final String link = urlUtilsService.getAdminUpgradeLink();
		return new MenuItem( caption, link );
	}

	private MenuItem anonymousDaysMenu() {
		final String caption = translatorService.translate( "Main menu: Anonymous Days", getLanguage() );
		final String link = urlUtilsService.getAdminAnonymousDaysLink();
		return new MenuItem( caption, link );
	}

	private MenuItem configurationMenu() {
		final String caption = translatorService.translate( "Main menu: Configuration", getLanguage() );
		final String link = urlUtilsService.getAdminSystemConfigurationListLink();
		return new MenuItem( caption, link );
	}

	private MenuItem uploadPhotoMenu() {
		final String caption = translatorService.translate( MAIN_MENU_UPLOAD_PHOTO, getLanguage() );
		final String link = urlUtilsService.getPhotoNewLink();
		return new MenuItem( caption, link );
	}

	private MenuItem userCardMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Card", getLanguage() );
		final String link = urlUtilsService.getUserCardLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userPhotosMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Photos", getLanguage() );
		final String link = urlUtilsService.getPhotosByUserLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userDataMenu( final User user ) {
		final String caption = translatorService.translate( MAIN_MENU_PROFILE_SETTINGS, getLanguage() );
		final String link = urlUtilsService.getUserEditLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem changePasswordMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: Change password", getLanguage() );
		final String link = urlUtilsService.getChangeUserPasswordLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userAvatarMenu( final User user ) {
		final String caption = translatorService.translate( MAIN_MENU_MY_AVATAR, getLanguage() );
		final String link = urlUtilsService.getEditUserAvatarLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userTeamMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Team", getLanguage() );
		final String link = urlUtilsService.getUserTeamMembersLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userPhotoAlbumsMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Albums", getLanguage() );
		final String link = urlUtilsService.getUserPhotoAlbumListLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userTechMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Tech", getLanguage() );
		final String link = urlUtilsService.getUserTechLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userNotificationsControlMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: Notifications Control", getLanguage() );
		final String link = urlUtilsService.getUserNotificationsMenu( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userFavoritePhotosMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.PHOTO.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoritePhotosLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userFavoriteMembersMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.USER.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoriteMembersLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userBookmarkedPhotosMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.BOOKMARK.getName(), getLanguage() );
		final String link = urlUtilsService.getUserBookmarkedPhotosLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userFriendsMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.FRIEND.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoriteFriendsLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem userBlackListMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.BLACKLIST.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoriteBlackListLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem getSubscriptionOnNewCommentsMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: On new comments", getLanguage() );
		final String link = urlUtilsService.getPhotosWithSubscribeOnNewCommentsLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem getSubscriptionOnNewPhotosMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: On new photos", getLanguage() );
		final String link = urlUtilsService.getUsersNewPhotosNotificationMenuLink( user.getId() );
		return new MenuItem( caption, link );
	}

	private MenuItem allUsersMenu() {
		final String caption = translatorService.translate( "Main menu: All members", getLanguage() );
		final String link = urlUtilsService.getAllUsersLink();
		return new MenuItem( caption, link );
	}

	private MenuItem allPhotosMenu() {
		final String caption = translatorService.translate( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, getLanguage() );
		final String link = urlUtilsService.getAllPhotosLink();
		return new MenuItem( caption, link );
	}

	private MenuItem todayPhotos() {
		final String caption = translatorService.translate( "Main menu: Today's photos", getLanguage() );

		final Date currentDate = dateUtilsService.getCurrentDate();
		final String link = urlUtilsService.getPhotosUploadedInPeriodUrl( currentDate, currentDate );

		return new MenuItem( caption, link );
	}

	private MenuItem yesterdayPhotos() {
		final String caption = translatorService.translate( "Main menu: Yesterday's photos", getLanguage() );

		final Date yesterday = dateUtilsService.getDatesOffsetFromCurrentDate( -1 );
		final String link = urlUtilsService.getPhotosUploadedInPeriodUrl( yesterday, yesterday );

		return new MenuItem( caption, link );
	}

	private MenuItem thisWeekPhotos() {
		final String caption = translatorService.translate( "Main menu: This week's photos", getLanguage() );

		final Date firstSecondOfLastMonday = dateUtilsService.getFirstSecondOfLastMonday();
		final Date lastSecondOfNextSunday = dateUtilsService.getLastSecondOfNextSunday();
		final String link = urlUtilsService.getPhotosUploadedInPeriodUrl( firstSecondOfLastMonday, lastSecondOfNextSunday );

		return new MenuItem( caption, link );
	}

	private MenuItem thisMonthPhotos() {
		final String caption = translatorService.translate( "Main menu: This month's photos", getLanguage() );

		final Date firstSecondOfMonth = dateUtilsService.getFirstSecondOfMonth();
		final Date lastSecondOfMonth = dateUtilsService.getLastSecondOfMonth();
		final String link = urlUtilsService.getPhotosUploadedInPeriodUrl( firstSecondOfMonth, lastSecondOfMonth );

		return new MenuItem( caption, link );
	}

	private MenuItem photosAbsoluteBest() {
		final String caption = translatorService.translate( "Main menu: Absolute best", getLanguage() );
		final String link = urlUtilsService.getPhotosAbsoluteBestURL();
		return new MenuItem( caption, link );
	}

	private MenuItem photosTodayBest() {
		final String caption = translatorService.translate( "Main menu: Best today's photos", getLanguage() );
		final String link = urlUtilsService.getPhotosBestOnDateUrl( dateUtilsService.getCurrentDate() );
		return new MenuItem( caption, link );
	}

	private MenuItem photosYesterdayBest() {
		final String caption = translatorService.translate( "Main menu: Best yesterday's photos", getLanguage() );
		final String link = urlUtilsService.getPhotosBestOnDateUrl( dateUtilsService.getDatesOffsetFromCurrentDate( -1 ) );
		return new MenuItem( caption, link );
	}

	private MenuItem photosPeriodBest( final int days ) {
		final Date dateFrom = dateUtilsService.getDatesOffsetFromCurrentDate( -days );
		final Date dateTo = dateUtilsService.getCurrentDate();
		final String caption = translatorService.translate( "Main menu: Best for $1 days", getLanguage(), String.valueOf( days ) );
		final String link = urlUtilsService.getPhotosBestInPeriodUrl( dateFrom, dateTo );
		return new MenuItem( caption, link );
	}

	private MenuItem photosWeekBest() {
		final String caption = translatorService.translate( "Main menu: Best week's photos", getLanguage() );

		final Date firstSecondOfLastMonday = dateUtilsService.getFirstSecondOfLastMonday();
		final Date lastSecondOfNextSunday = dateUtilsService.getLastSecondOfNextSunday();
		final String link = urlUtilsService.getPhotosBestInPeriodUrl( firstSecondOfLastMonday, lastSecondOfNextSunday );

		return new MenuItem( caption, link );
	}

	private MenuItem photosMonthBest() {
		final String caption = translatorService.translate( "Main menu: Best month's photos", getLanguage() );

		final Date firstSecondOfMonth = dateUtilsService.getFirstSecondOfMonth();
		final Date lastSecondOfMonth = dateUtilsService.getLastSecondOfMonth();
		final String link = urlUtilsService.getPhotosBestInPeriodUrl( firstSecondOfMonth, lastSecondOfMonth );

		return new MenuItem( caption, link );
	}

	private MenuItem registerMenu() {
		final String caption = translatorService.translate( "Main menu: Register", getLanguage() );
		final String link = urlUtilsService.getUserNewLink();
		return new MenuItem( caption, link );
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
