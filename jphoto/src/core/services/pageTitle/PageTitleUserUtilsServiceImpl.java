package core.services.pageTitle;

import core.enums.FavoriteEntryType;
import core.enums.UserCardTab;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.translator.TranslatorService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import utils.StringUtilities;

public class PageTitleUserUtilsServiceImpl implements PageTitleUserUtilsService {

	public static final String USER_ROOT = "Members";
	
	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;
	
	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public PageTitleData getUserListData() {
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( rootTranslated );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserNewData() {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Register" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getVotesForUserRankInGenreData( final User user, final Genre genre ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Votes for rank in genre" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getPhotosByUserLink( user ), entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );

//		return getUserData( user, tran );
	}

	@Override
	public PageTitleData getUserEditData( final User user ) {
		return getUserData( user, translatorService.translate( "Edit" ) );
	}

	@Override
	public PageTitleData setUserAvatarData( final User user ) {
		return getUserData( user, translatorService.translate( "Avatar" ) );
	}

	@Override
	public PageTitleData getUserCardData( final User user, final UserCardTab userCardTab ) {
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), userCardTab.getNameTranslated() );

		final String userLinkOrName = userCardTab.isDefaultTab() ? user.getNameEscaped() : entityLinkUtilsService.getUserCardLink( user );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), userLinkOrName, userCardTab.getNameTranslated() );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserListByFilter() {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Filter" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUsersByMembershipType( final UserMembershipType membershipType ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = StringUtilities.toUpperCaseFirst( translatorService.translate( membershipType.getNamePlural() ) );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberListData( final User user ) {
		return getUserData( user, translatorService.translate( "Team" ) );
	}

	@Override
	public PageTitleData getUserPrivateMessagesListData( final User user ) {
		return getUserData( user, translatorService.translate( "Private Messages" ) );
	}

	@Override
	public PageTitleData getUserTeamMemberNewData( final User user ) {
		final String aNew = translatorService.translate( "New" );

		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Team" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getUserTeamMemberListLink( user.getId() ), aNew );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberEditData( final UserTeamMember userTeamMember ) {
		final String edit = translatorService.translate( "Edit" );

		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Team" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, userTeamMember.getTeamMemberName(), tran, edit );

		final User user = userTeamMember.getUser();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getUserTeamMemberListLink( user.getId() ), entityLinkUtilsService.getUserTeamMemberCardLink( userTeamMember ), edit );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberCardData( final UserTeamMember userTeamMember ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Team" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, userTeamMember.getTeamMemberName(), tran );

		final User user = userTeamMember.getUser();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getUserTeamMemberListLink( user.getId() ), StringUtilities.escapeHtml( userTeamMember.getTeamMemberName() ) );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsData( final User user ) {
		return getUserData( user, translatorService.translate( "Photo albums" ) );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsNew( final User user ) {
		final String tran = translatorService.translate( "New" );
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getUserPhotoAlbumListLink( user.getId() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsEdit( final UserPhotoAlbum photoAlbum ) {
		final User user = photoAlbum.getUser();
		final String tran = translatorService.translate( "Edit" );
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getUserPhotoAlbumListLink( user.getId() ), entityLinkUtilsService.getUserPhotoAlbumPhotosLink( photoAlbum ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumsPhotos( final UserPhotoAlbum photoAlbum ) {
		final User user = photoAlbum.getUser();
		final String tran = StringUtilities.escapeHtml( photoAlbum.getName() );
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), entityLinkUtilsService.getUserPhotoAlbumListLink( user.getId() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getFavoriteEntry( final User user, final FavoriteEntryType favoriteEntryType ) {
		return getUserData( user, String.format( "%s / %s", translatorService.translate( "Favorites" ), favoriteEntryType.getName() ) );
	}

	@Override
	public PageTitleData getAddedToFavoritesByEntry( final User user ) {
		return getUserData( user, String.format( "%s", translatorService.translate( "Added to Favorites by" ) ) );
	}

	@Override
	public String getUserRootTranslated() {
		return translatorService.translate( USER_ROOT );
	}

	@Override
	public PageTitleData getUserData( final User user, final String tran ) {
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), entityLinkUtilsService.getUserCardLink( user ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserWrongLogin() {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Can't login" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink(), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getChangeUserPasswordData( final User user ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Change password" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink()
			, entityLinkUtilsService.getUserCardLink( user )
			, tran
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserNotificationsControlData( final User user ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Notifications control" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(
			entityLinkUtilsService.getUsersRootLink()
			, entityLinkUtilsService.getUserCardLink( user )
			, tran
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosOfUserFavoriteMembers( final User user ) {
		final String rootTranslated = getUserRootTranslated();
		final String tran = translatorService.translate( "Photos of the favorite members" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(
			entityLinkUtilsService.getUsersRootLink()
			, entityLinkUtilsService.getUserCardLink( user )
			, tran
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}
}
