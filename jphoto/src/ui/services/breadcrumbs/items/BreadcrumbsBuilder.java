package ui.services.breadcrumbs.items;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.system.Services;
import core.services.translator.Language;
import ui.context.EnvironmentContext;

import java.sql.Time;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BreadcrumbsBuilder {

	public static final String BREADCRUMBS_PORTAL_PAGE = "Breadcrumbs: Portal page";
	public static final String BREADCRUMBS_PHOTO_GALLERY_ROOT = "Photo gallery root";

	protected final Services services;

	private final List<AbstractBreadcrumb> breadcrumbs = newArrayList();

	public BreadcrumbsBuilder( final Services services ) {
		this.services = services;
	}

	public static BreadcrumbsBuilder portalPage( final Services services ) {
		return new BreadcrumbsBuilder( services ).portalPageLink();
	}

	public static BreadcrumbsBuilder pageTitle( final AbstractBreadcrumb breadcrumb, final Services services ) {
		return new BreadcrumbsBuilder( services ).projectName().add( breadcrumb );
	}

	public static BreadcrumbsBuilder pageTitle( final User user, final AbstractBreadcrumb breadcrumb, final Services services ) {
		return new BreadcrumbsBuilder( services ).projectName().userName( user ).add( breadcrumb );
	}

	public static BreadcrumbsBuilder pageTitle( final int userId, final AbstractBreadcrumb breadcrumb, final Services services ) {
		return pageTitle( getUser( userId, services ), breadcrumb, services );
	}

	public static BreadcrumbsBuilder pageHeader( final AbstractBreadcrumb breadcrumb, final Services services ) {
		return new BreadcrumbsBuilder( services ).add( breadcrumb );
	}

	public BreadcrumbsBuilder add( final AbstractBreadcrumb breadcrumb ) {
		breadcrumbs.add( breadcrumb );
		return this;
	}

	public BreadcrumbsBuilder portalPage() {
		breadcrumbs.add( new PortalPageBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder portalPageLink() {
		breadcrumbs.add( new PortalPageLinkBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder projectName() {
		breadcrumbs.add( new ProjectNameBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder projectNameLink() {
		breadcrumbs.add( new ProjectNameLinkBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder photoGalleryLink() {
		breadcrumbs.add( new PhotoGalleryLinkBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder string( final String breadcrumb ) {
		breadcrumbs.add( new StringBreadcrumb( breadcrumb, services ) );
		return this;
	}

	public BreadcrumbsBuilder translatableString( final String breadcrumb ) {
		breadcrumbs.add( new TranslatableStringBreadcrumb( breadcrumb, services ) );
		return this;
	}

	public BreadcrumbsBuilder userList() {
		breadcrumbs.add( new UserListBreadcrumbs( services ) );
		return this;
	}

	public BreadcrumbsBuilder userListLink() {
		breadcrumbs.add( new UserListLinkBreadcrumbs( services ) );
		return this;
	}

	public BreadcrumbsBuilder userCardLink( final User user ) {
		breadcrumbs.add( new UserCardLinkBreadcrumb( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder userCardLink( final int userId ) {
		return userCardLink( getUser( userId, services ) );
	}

	public BreadcrumbsBuilder userName( final User user ) {
		breadcrumbs.add( new UserNameBreadcrumb( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder userName( final int userId ) {
		return userName( getUser( userId, services ) );
	}

	public BreadcrumbsBuilder userTeamLink( final User user ) {
		breadcrumbs.add( new UserTeamLinkBreadcrumbs( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder userTeamMemberLink( final UserTeamMember userTeamMember ) {
		breadcrumbs.add( new UserTeamMemberLinkBreadcrumbs( userTeamMember, services ) );
		return this;
	}

	public BreadcrumbsBuilder userTeamMemberName( final UserTeamMember userTeamMember ) {
		breadcrumbs.add( new UserTeamMemberNameBreadcrumbs( userTeamMember, services ) );
		return this;
	}

	public BreadcrumbsBuilder userAlbumListLink( final User user ) {
		breadcrumbs.add( new UserAlbumListBreadcrumbs( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder userAlbumLinkLink( final UserPhotoAlbum photoAlbum ) {
		breadcrumbs.add( new UserAlbumLinkBreadcrumbs( photoAlbum, services ) );
		return this;
	}

	public BreadcrumbsBuilder anonymousUser() {
		breadcrumbs.add( new AnonymousUserBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder photoCardLink( final Photo photo ) {
		breadcrumbs.add( new PhotoCardLinkBreadcrumb( photo, services ) );
		return this;
	}

	public BreadcrumbsBuilder photoName( final Photo photo ) {
		breadcrumbs.add( new PhotoNameBreadcrumb( photo, services ) );
		return this;
	}

	public BreadcrumbsBuilder photosByGenre( final Genre genre ) {
		breadcrumbs.add( new PhotosByGenreBreadcrumb( genre, services ) );
		return this;
	}

	public BreadcrumbsBuilder photosByGenre( final int genreId ) {
		return photosByGenre( getGenre( genreId ) );
	}

	public BreadcrumbsBuilder photosByUser( final User user ) {
		breadcrumbs.add( new PhotosByUserBreadcrumb( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder photosByUser( final int userId ) {
		return photosByUser( getUser( userId, services ) );
	}

	public BreadcrumbsBuilder photosByUserAndGenre( final User user, final Genre genre ) {
		breadcrumbs.add( new PhotosByUserAndGenreBreadcrumb( user, genre, services ) );
		return this;
	}

	public BreadcrumbsBuilder photosByUserAndGenre( final int userId, final int genreId ) {
		return photosByUserAndGenre( services.getUserService().load( userId ), getGenre( genreId ) );
	}

	public BreadcrumbsBuilder photoAppraisedPhotosLink( final User user ) {
		breadcrumbs.add( new PhotoAppraisedPhotosLinkBreadcrumb( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder activityStream() {
		breadcrumbs.add( new ActivityStreamBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder formattedDate( final Time date, final Genre genre ) {
		breadcrumbs.add( new FormattedDateBreadcrumb( date, services ) );
		return this;
	}

	public BreadcrumbsBuilder adminRoot() {
		breadcrumbs.add( new AdminRootBreadcrumbs( services ) );
		return this;
	}

	public BreadcrumbsBuilder adminJobList() {
		breadcrumbs.add( new AdminJobListBreadcrumbs( services ) );
		return this;
	}

	public String build() {
		return build( EnvironmentContext.getLanguage() );
	}

	public String build( final Language language ) {

		final StringBuilder builder = new StringBuilder(  );

		for ( final AbstractBreadcrumb breadcrumb : breadcrumbs ) {
			builder.append( breadcrumb.getValue( language ) ).append( " / " );
		}

		return builder.substring( 0, builder.length() - 2 );
	}

	private static User getUser( final int userId, final Services services ) {
		return services.getUserService().load( userId );
	}

	private Genre getGenre( final int genreId ) {
		return services.getGenreService().load( genreId );
	}
}
