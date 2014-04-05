package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.enums.FavoriteEntryType;
import core.enums.PrivateMessageType;
import core.enums.UserCardTab;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.system.MenuService;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.nerds.LinkNerdText;
import core.services.translator.TranslatorService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;
import ui.services.breadcrumbs.items.UserListBreadcrumbs;
import ui.services.breadcrumbs.items.UserNameBreadcrumb;

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
	private BreadcrumbsPhotoGalleryService breadcrumbsPhotoGalleryService;

	@Autowired
	private Services services;

	@Override
	public PageTitleData getUserListBreadcrumbs() {

		final UserListBreadcrumbs breadcrumb = new UserListBreadcrumbs( services );
		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = portalPage( services )
			.userList()
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getUserRegistrationBreadcrumbs() {

		final TranslatableStringBreadcrumb breadcrumb = new TranslatableStringBreadcrumb( MenuService.MAIN_MENU_REGISTER, services );
		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = portalPage( services )
			.userListLink()
			.add( breadcrumb )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getVotesForUserRankInGenreBreadcrumbs( final User user, final Genre genre ) {

		final String breadcrumbs = breadcrumbsPhotoGalleryService.getUserPhotosInGenreLinkBreadcrumbs( user, genre )
																 .translatableString( "Breadcrumbs: Votes for rank in genre" )
																 .build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserEditBreadcrumbs( final User user ) {

		final String breadcrumbs = userCardLink( user )
			.translatableString( MenuService.MAIN_MENU_PROFILE_SETTINGS )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData setUserAvatarBreadcrumbs( final User user ) {

		final String breadcrumbs = userCardLink( user )
			.translatableString( "Breadcrumbs: User avatar" )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserCardBreadcrumbs( final User user, final UserCardTab userCardTab ) {

		final BreadcrumbsBuilder builder = portalPage( services ).userListLink();

		if ( userCardTab.isDefaultTab() ) {
			builder.string( String.format( "%s: %s", user.getNameEscaped(), translatorService.translate( UserCardTab.getDefaultUserCardTab().getName(), EnvironmentContext.getLanguage() ) ) );
		} else {
			builder.userCardLink( user ).translatableString( userCardTab.getName() );
		}

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), builder.build() );
	}

	@Override
	public PageTitleData getUserTeamMemberListData( final User user ) {

		final String breadcrumbs = userCardLink( user )
			.translatableString( EntityLinkUtilsService.BREADCRUMBS_USER_TEAM )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserPrivateMessagesListBreadcrumbs( final User user, final User withUser ) {
		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = userCardLink( user )
			.string( translatorService.translate( "Breadcrumbs: messaging with user $1", language, entityLinkUtilsService.getUserCardLink( withUser, language ) ) )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserPrivateMessagesListBreadcrumbs( final User user, final PrivateMessageType messageType ) {
		final String breadcrumbs = userCardLink( user )
			.translatableString( messageType.getName() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserListSearchResultBreadcrumbs() {

		final UserListBreadcrumbs breadcrumb = new UserListBreadcrumbs( services );
		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = portalPage( services )
			.userListLink()
			.translatableString( "User list: search result" )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getUsersFilteredByMembershipTypeBreadcrumbs( final UserMembershipType membershipType ) {

		final UserListBreadcrumbs breadcrumb = new UserListBreadcrumbs( services );
		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = portalPage( services )
			.userListLink()
			.translatableString( membershipType.getNamePlural() )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberNewBreadcrumbs( final User user ) {

		final UserNameBreadcrumb breadcrumb = new UserNameBreadcrumb( user, services );
		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = userTeamLink( user )
			.translatableString( "Breadcrumbs: New member of user team" )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberEditBreadcrumbs( final UserTeamMember userTeamMember ) {

		final User user = userTeamMember.getUser();

		final String breadcrumbs = userTeamLink( user )
			.userTeamMemberLink( userTeamMember )
			.translatableString( "Breadcrumbs: Editing user team member's data" )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserTeamMemberCardBreadcrumbs( final UserTeamMember userTeamMember ) {

		final User user = userTeamMember.getUser();

		final String breadcrumbs = userTeamLink( user )
			.userTeamMemberName( userTeamMember )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumListBreadcrumbs( final User user ) {

		final UserNameBreadcrumb breadcrumb = new UserNameBreadcrumb( user, services );
		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = userCardLink( user )
			.translatableString( EntityLinkUtilsService.USER_PHOTO_ALBUM_LIST )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumNewBreadcrumbs( final User user ) {

		final String breadcrumbs = userAlbumListLink( user )
			.translatableString( "Breadcrumbs: Create new user photo album" )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumEditBreadcrumbs( final UserPhotoAlbum photoAlbum ) {

		final User user = photoAlbum.getUser();

		final String breadcrumbs = userAlbumListLink( user )
			.userAlbumLinkLink( photoAlbum )
			.translatableString( "Breadcrumbs: User photo album edit" )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoAlbumPhotosBreadcrumbs( final UserPhotoAlbum photoAlbum ) {

		final User user = photoAlbum.getUser();

		final String breadcrumbs = userAlbumListLink( user )
			.translatableString( photoAlbum.getNameEscaped() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserFavoriteEntryListBreadcrumbs( final User user, final FavoriteEntryType favoriteEntryType ) {

		final String breadcrumbs = userCardLink( user )
			.translatableString( favoriteEntryType.getName() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserIsAddedInFavoriteMembersByBreadcrumbs( final User user ) {
		final String breadcrumbs = userCardLink( user )
			.translatableString( LinkNerdText.USER_STATISTICS_THE_USER_IS_ADDED_IN_FAVORITE_MEMBERS_BY.getText() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserWrittenCommentsBreadcrumb( final User user ) {
		final String breadcrumbs = userCardLink( user )
			.translatableString( LinkNerdText.USER_STATISTICS_COMMENTS_WRITTEN.getText() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserReceivedCommentsBreadcrumb( final User user ) {
		final String breadcrumbs = userCardLink( user )
			.translatableString( LinkNerdText.USER_STATISTICS_COMMENTS_RECEIVED.getText() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserWrittenUnreadCommentsBreadcrumb( final User user ) {
		final String breadcrumbs = userCardLink( user )
			.translatableString( LinkNerdText.USER_STATISTICS_COMMENTS_RECEIVED_UNREAD.getText() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosOfUserFavoriteMembersBreadcrumb( final User user ) {
		final String breadcrumbs = userCardLink( user )
			.translatableString( LinkNerdText.USER_STATISTICS_PHOTOS_OF_USER_FAVORITE_MEMBERS.getText() )
			.build();

		return new PageTitleData( userCardTitle( user ), userCardHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getUserWrongLogin() { // TODO: move to CommonBreadcrumbsService
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

	@Deprecated
	@Override
	public String getUserRootTranslated() {
		return translatorService.translate( USER_ROOT, EnvironmentContext.getLanguage() );
	}

	@Deprecated
	@Override
	public PageTitleData getUserData( final User user, final String tran ) {
		final String rootTranslated = getUserRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getUsersRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	private String userCardTitle( final User user ) {
		return BreadcrumbsBuilder.pageTitle( new UserNameBreadcrumb( user, services ), services ).build();
	}

	private String userCardHeader( final User user ) {
		return BreadcrumbsBuilder.pageHeader( new UserNameBreadcrumb( user, services ), services ).build();
	}

	private BreadcrumbsBuilder userCardLink( final User user ) {
		return portalPage( services ).userListLink().userCardLink( user );
	}

	private BreadcrumbsBuilder userTeamLink( final User user ) {
		return portalPage( services )
			.userListLink()
			.userCardLink( user )
			.userTeamLink( user );
	}

	private BreadcrumbsBuilder userAlbumListLink( final User user ) {
		return userCardLink( user )
			.userAlbumListLink( user );
	}
}