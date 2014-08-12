package ui.services.menu.main;

import core.enums.FavoriteEntryType;
import core.enums.PrivateMessageType;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.translator.nerds.LinkNerdText;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import ui.elements.MenuItem;
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

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public Map<MenuItem, List<MenuItem>> getMenuElements( final User user ) {

		final Map<MenuItem, List<MenuItem>> menus = newLinkedHashMap();

		createPhotosMenu( menus );

		if ( configurationService.getBoolean( ConfigurationKey.SYSTEM_UI_SHOW_PHOTOS_BY_CATEGORIES_MENU_ITEM ) ) {
			createPhotosByGenreMenu( menus, getLanguage() );
		}

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

		final List<Genre> genres = genreService.loadAllSortedByNameForLanguage( language );
		for ( final Genre genre : genres ) {
			final String caption = translatorService.translateGenre( genre, language );
			final String link = urlUtilsService.getPhotosByGenreLink( genre.getId() );
			menuItems.add( new MenuItem( caption, link ) );
		}

		menus.put( new MenuItem( translatorService.translate( "Main menu: Photos by categories", getLanguage() ), urlUtilsService.getGenreListLink() ), menuItems );
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
			menuItems.add( getUserPhotosOfFavoriteMembersMenu( user ) );
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
			menuItems.add( getReceivedCommentsMenu( user ) );
			menuItems.add( getReceivedUnreadCommentsMenu( user ) );
			menuItems.add( getWrittenCommentsMenu( user ) );

			menuItems.addAll( getPrivateMessagesMenus( user ) );

			menus.put( MenuItem.noLinkMenu( translatorService.translate( "Main menu: Messages", getLanguage() ) ), menuItems );

			menuItems.add( userNotificationsControlMenu( user ) );
		}
	}

	private MenuItem getReceivedCommentsMenu( final User user ) {
		final String caption = translatorService.translate( LinkNerdText.USER_STATISTICS_COMMENTS_RECEIVED.getText(), getLanguage() );
		final String link = urlUtilsService.getReceivedComments( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-comments-received.png" );

		return menuItem;
	}

	private MenuItem getReceivedUnreadCommentsMenu( final User user ) {
		final String caption = translatorService.translate( LinkNerdText.USER_STATISTICS_COMMENTS_RECEIVED_UNREAD.getText(), getLanguage() );
		final String link = urlUtilsService.getReceivedUnreadComments( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "icons16/newComments16.png" );

		return menuItem;
	}

	private MenuItem getWrittenCommentsMenu( final User user ) {
		final String caption = translatorService.translate( LinkNerdText.USER_STATISTICS_COMMENTS_WRITTEN.getText(), getLanguage() );
		final String link = urlUtilsService.getWrittenComments( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-comments-written.png" );

		return menuItem;
	}

	private List<MenuItem> getPrivateMessagesMenus( final User user ) {
		final List<MenuItem> result = newArrayList();

		for ( final PrivateMessageType privateMessageType : PrivateMessageType.values() ) {

			if ( privateMessageType == PrivateMessageType.ADMIN_NOTIFICATIONS && ! securityService.isSuperAdminUser( user.getId() ) ) {
				continue;
			}

			final String caption = translatorService.translate( privateMessageType.getName(), getLanguage() );
			final String link = urlUtilsService.getPrivateMessagesList( user.getId() );

			final MenuItem menuItem = new MenuItem( caption, link );
			menuItem.setIcon( String.format( "messages/%s", privateMessageType.getIcon() ) );

			result.add( menuItem );
		}

		return result;
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
		menuItems.add( votingCategoriesMenu() );
		menuItems.add( anonymousDaysMenu() );
		menuItems.add( restrictionListMenu() );
		menuItems.add( translatorMenu() );
		menuItems.add( reloadTranslatorMenu() );
		menuItems.add( controlPanelMenu() );
		menuItems.add( upgradeMenu() );

		menus.put( MenuItem.noLinkMenu( translatorService.translate( MAIN_MENU_ADMIN_ROOT, getLanguage() ) ), menuItems );
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
//		final String caption = entityLinkUtilsService.getMembershipPhotosLinkText( membership); //translatorService.translate( String.format( MAIN_MENU_MEMBERSHIP_TYPE_NERD, url, membershipType.getName() ), getLanguage() );
		final String caption = translatorService.translate( String.format( MAIN_MENU_MEMBERSHIP_TYPE_NERD, url, membershipType.getName() ), getLanguage() ); // TODO: try to use entityLinkUtilsService.getMembershipPhotosLinkText()
		final String link = urlUtilsService.getPhotosByMembership( membershipType, url );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( String.format( "userMembershipType/%s", membershipType.getIcon() ) );

		return menuItem;
	}

	private MenuItem membershipBestMenu( final UserMembershipType membershipType, final String url ) {
		final String nerd = String.format( "Main menu: The best photos: %s", membershipType.getName() );
		final String caption = translatorService.translate( nerd, getLanguage() );
		final String link = urlUtilsService.getPhotosByMembershipBest( membershipType, url );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( String.format( "userMembershipType/%s", membershipType.getIcon() ) );

		return menuItem;
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

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "icons32/translate.png" );

		return menuItem;
	}

	private MenuItem reloadTranslatorMenu() {
		final String caption = translatorService.translate( "Main menu: Reload translations", getLanguage() );

		final MenuItem menuItem = MenuItem.jsFunctionLinkMenu( caption, "reloadTranslations(); return false;" );
		menuItem.setIcon( "menus/main-menu-translation-reload.png" );

		return menuItem;
	}

	private MenuItem controlPanelMenu() {
		final String caption = translatorService.translate( "Main menu: Control panel", getLanguage() );
		final String link = urlUtilsService.getAdminControlPanelLink();
		return new MenuItem( caption, link );
	}

	private MenuItem jobsMenu() {
		final String caption = translatorService.translate( MAIN_MENU_ADMIN_JOBS, getLanguage() );
		final String link = urlUtilsService.getAdminJobsLink();

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "jobTemplate16x16.png" );

		return menuItem;
	}

	private MenuItem schedulerMenu() {
		final String caption = translatorService.translate( "Main menu: Scheduler", getLanguage() );
		final String link = urlUtilsService.getAdminSchedulerTaskListLink();

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "scheduler/SchedulerIsRunning.png" );

		return menuItem;
	}

	private MenuItem upgradeMenu() {
		final String caption = translatorService.translate( "Main menu: DB upgrade", getLanguage() );
		final String link = urlUtilsService.getAdminUpgradeLink();

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-update-db.png" );

		return menuItem;
	}

	private MenuItem anonymousDaysMenu() {
		final String caption = translatorService.translate( "Main menu: Anonymous Days", getLanguage() );
		final String link = urlUtilsService.getAdminAnonymousDaysLink();

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "icons24/admin-special-flag-anonymous-posting.png" );

		return menuItem;
	}

	private MenuItem restrictionListMenu() {
		final String caption = translatorService.translate( MAIN_MENU_RESTRICTION_LIST, getLanguage() );
		final String link = urlUtilsService.getRestrictionListLink();

		final MenuItem menuItem = new MenuItem( caption, link );
//		menuItem.setIcon( "icons24/admin-special-flag-anonymous-posting.png" );

		return menuItem;
	}

	private MenuItem configurationMenu() {
		final String caption = translatorService.translate( "Main menu: Configuration", getLanguage() );
		final String link = urlUtilsService.getAdminSystemConfigurationListLink();

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "system-configuration-default.png" );

		return menuItem;
	}

	private MenuItem uploadPhotoMenu() {
		final String caption = translatorService.translate( MAIN_MENU_UPLOAD_PHOTO, getLanguage() );
		final String link = urlUtilsService.getPhotoNewLink();

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-upload-photo.png" );

		return menuItem;
	}

	private MenuItem userCardMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Card", getLanguage() );
		final String link = urlUtilsService.getUserCardLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-profile.png" );

		return menuItem;
	}

	private MenuItem userPhotosMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Photos", getLanguage() );
		final String link = urlUtilsService.getPhotosByUserLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-photos.png" );

		return menuItem;
	}

	private MenuItem userDataMenu( final User user ) {
		final String caption = translatorService.translate( MAIN_MENU_PROFILE_SETTINGS, getLanguage() );
		final String link = urlUtilsService.getUserEditLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-settings.png" );

		return menuItem;
	}

	private MenuItem changePasswordMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: Change password", getLanguage() );
		final String link = urlUtilsService.getChangeUserPasswordLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-password.png" );

		return menuItem;
	}

	private MenuItem userAvatarMenu( final User user ) {
		final String caption = translatorService.translate( MAIN_MENU_MY_AVATAR, getLanguage() );
		final String link = urlUtilsService.getEditUserAvatarLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-avatar.png" );

		return menuItem;
	}

	private MenuItem userTeamMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Team", getLanguage() );
		final String link = urlUtilsService.getUserTeamMembersLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-team.png" );

		return menuItem;
	}

	private MenuItem userPhotoAlbumsMenu( final User user ) {
		final String caption = translatorService.translate( "Main menu: My Albums", getLanguage() );
		final String link = urlUtilsService.getUserPhotoAlbumListLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/main-menu-user-albums.png" );

		return menuItem;
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
		final String caption = translatorService.translate( FavoriteEntryType.FAVORITE_PHOTOS.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoritePhotosLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( String.format( "favorites/%s", FavoriteEntryType.FAVORITE_PHOTOS.getRemoveIcon() ) );

		return menuItem;
	}

	private MenuItem userFavoriteMembersMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.FAVORITE_MEMBERS.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoriteMembersLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( String.format( "favorites/%s", FavoriteEntryType.FAVORITE_MEMBERS.getRemoveIcon() ) );

		return menuItem;
	}

	private MenuItem getUserPhotosOfFavoriteMembersMenu( final User user ) {
		final String caption = translatorService.translate( LinkNerdText.USER_STATISTICS_PHOTOS_OF_USER_FAVORITE_MEMBERS.getText(), getLanguage() );
		final String link = urlUtilsService.getUserPhotosOfFavoriteMembersLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( "menus/menu_photos.png" );

		return menuItem;
	}

	private MenuItem userBookmarkedPhotosMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.BOOKMARKED_PHOTOS.getName(), getLanguage() );
		final String link = urlUtilsService.getUserBookmarkedPhotosLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( String.format( "favorites/%s", FavoriteEntryType.BOOKMARKED_PHOTOS.getRemoveIcon() ) );

		return menuItem;
	}

	private MenuItem userFriendsMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.FRIENDS.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoriteFriendsLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( String.format( "favorites/%s", FavoriteEntryType.FRIENDS.getRemoveIcon() ) );

		return menuItem;
	}

	private MenuItem userBlackListMenu( final User user ) {
		final String caption = translatorService.translate( FavoriteEntryType.BLACKLIST.getName(), getLanguage() );
		final String link = urlUtilsService.getUserFavoriteBlackListLink( user.getId() );

		final MenuItem menuItem = new MenuItem( caption, link );
		menuItem.setIcon( String.format( "favorites/%s", FavoriteEntryType.BLACKLIST.getRemoveIcon() ) );

		return menuItem;
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
		final String caption = translatorService.translate( MAIN_MENU_ABSOLUTE_BEST, getLanguage() );
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
