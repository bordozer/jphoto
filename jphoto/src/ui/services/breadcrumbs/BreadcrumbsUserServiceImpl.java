package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.enums.FavoriteEntryType;
import core.enums.UserCardTab;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.system.MenuService;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.UserListBreadcrumbs;
import utils.StringUtilities;

import static ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

public class BreadcrumbsUserServiceImpl implements BreadcrumbsUserService {

	public static final String USER_ROOT = "Members";
	
	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;
	
	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private Services services;

	@Override
	public PageTitleData getUserListBreadcrumbs() {

		final UserListBreadcrumbs userListText = new UserListBreadcrumbs( services );
		final String title = BreadcrumbsBuilder.pageTitle( userListText, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( userListText, services ).build();

		final String breadcrumbs = portalPage( services )
			.userList()
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getUserRegistrationBreadcrumbs() {

		final UserListBreadcrumbs userListText = new UserListBreadcrumbs( services );
		final String title = BreadcrumbsBuilder.pageTitle( userListText, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( userListText, services ).build();

		final String breadcrumbs = portalPage( services )
			.userListLink()
			.translatableString( MenuService.MAIN_MENU_REGISTER )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getVotesForUserRankInGenreData( final User user, final Genre genre ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Votes for rank in genre", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), entityLinkUtilsService.getPhotosByUserLink( user, EnvironmentContext.getLanguage() )
			, entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre, EnvironmentContext.getCurrentUser().getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserEditData( final User user ) {
		return getUserData( user, translatorService.translate( "Edit", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData setUserAvatarData( final User user ) {
		return getUserData( user, translatorService.translate( "Avatar", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData getUserCardData( final User user, final UserCardTab userCardTab ) {
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), userCardTab.getNameTranslated() );

		final String userLinkOrName = userCardTab.isDefaultTab() ? user.getNameEscaped() : entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), userLinkOrName, userCardTab.getNameTranslated() );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserListByFilter() {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "User filter", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUsersByMembershipType( final UserMembershipType membershipType ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = StringUtilities.toUpperCaseFirst( translatorService.translate( membershipType.getNamePlural(), EnvironmentContext.getLanguage() ) );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberListData( final User user ) {
		return getUserData( user, translatorService.translate( "Team", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData getUserPrivateMessagesListData( final User user ) {
		return getUserData( user, translatorService.translate( "Private Messages", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData getUserTeamMemberNewData( final User user ) {
		final String aNew = translatorService.translate( "New", EnvironmentContext.getLanguage() );

		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Team", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserTeamMemberListLink( user.getId(), EnvironmentContext.getLanguage() ), aNew );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberEditData( final UserTeamMember userTeamMember ) {
		final String edit = translatorService.translate( "Edit", EnvironmentContext.getLanguage() );

		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Team", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, userTeamMember.getTeamMemberName(), tran, edit );

		final User user = userTeamMember.getUser();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserTeamMemberListLink( user.getId(), EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserTeamMemberCardLink( userTeamMember, EnvironmentContext.getLanguage() ), edit );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberCardData( final UserTeamMember userTeamMember ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Team", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, userTeamMember.getTeamMemberName(), tran );

		final User user = userTeamMember.getUser();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserTeamMemberListLink( user.getId(), EnvironmentContext.getLanguage() ), StringUtilities.escapeHtml( userTeamMember.getTeamMemberName() ) );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsData( final User user ) {
		return getUserData( user, translatorService.translate( "Photo albums", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsNew( final User user ) {
		final String tran = translatorService.translate( "New", EnvironmentContext.getLanguage() );
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserPhotoAlbumListLink( user.getId(), EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsEdit( final UserPhotoAlbum photoAlbum ) {
		final User user = photoAlbum.getUser();
		final String tran = translatorService.translate( "Edit", EnvironmentContext.getLanguage() );
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserPhotoAlbumListLink( user.getId(), EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserPhotoAlbumPhotosLink( photoAlbum, EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsPhotos( final UserPhotoAlbum photoAlbum ) {
		final User user = photoAlbum.getUser();
		final String tran = StringUtilities.escapeHtml( photoAlbum.getName() );
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserPhotoAlbumListLink( user.getId(), EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getFavoriteEntry( final User user, final FavoriteEntryType favoriteEntryType ) {
		return getUserData( user, String.format( "%s / %s", translatorService.translate( "Favorites", EnvironmentContext.getLanguage() ), favoriteEntryType.getName() ) );
	}

	@Override
	public PageTitleData getAddedToFavoritesByEntry( final User user ) {
		return getUserData( user, String.format( "%s", translatorService.translate( "Added to Favorites by", EnvironmentContext.getLanguage() ) ) );
	}

	@Override
	public String getUserRootTranslated() {
		return translatorService.translate( USER_ROOT, EnvironmentContext.getLanguage() );
	}

	@Override
	public PageTitleData getUserData( final User user, final String tran ) {
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserWrongLogin() {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Can't login", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getChangeUserPasswordData( final User user ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Change password", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() )
			, entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() )
			, tran
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserNotificationsControlData( final User user ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Notifications control", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(
			entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() )
			, entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() )
			, tran
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosOfUserFavoriteMembers( final User user ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Photos of the favorite members", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(
			entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() )
			, entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() )
			, tran
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}
}
