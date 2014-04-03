package ui.services.breadcrumbs.items;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;

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

	public static BreadcrumbsBuilder breadcrumbs( final Services services ) {
		return new BreadcrumbsBuilder( services ).addPortalPageLinkBreadcrumb();
	}

	public static BreadcrumbsBuilder pageTitle( final AbstractBreadcrumb breadcrumb, final Services services ) {
		return new BreadcrumbsBuilder( services ).addProjectNameBreadcrumb().add( breadcrumb );
	}

	public static BreadcrumbsBuilder pageHeader( final AbstractBreadcrumb breadcrumb, final Services services ) {
		return new BreadcrumbsBuilder( services ).add( breadcrumb );
	}

	public static BreadcrumbsBuilder getInstance( final Services services ) {
		return new BreadcrumbsBuilder( services );
	}

	public BreadcrumbsBuilder add( final AbstractBreadcrumb breadcrumb ) {
		breadcrumbs.add( breadcrumb );
		return this;
	}

	public BreadcrumbsBuilder addPortalPageBreadcrumb() {
		breadcrumbs.add( new PortalPageBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addPortalPageLinkBreadcrumb() {
		breadcrumbs.add( new PortalPageLinkBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addProjectNameBreadcrumb() {
		breadcrumbs.add( new ProjectNameBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addProjectNameLinkBreadcrumb() {
		breadcrumbs.add( new ProjectNameLinkBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotoGalleryBreadcrumb() {
		breadcrumbs.add( new PhotoGalleryBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotoUploadBreadcrumb() {
		breadcrumbs.add( new PhotoUploadBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotoEditBreadcrumb() {
		breadcrumbs.add( new PhotoEditBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotoGalleryLinkBreadcrumb() {
		breadcrumbs.add( new PhotoGalleryLinkBreadcrumb( services ) );
		return this;
	}

	public BreadcrumbsBuilder addStringBreadcrumb( final String breadcrumb ) {
		breadcrumbs.add( new StringBreadcrumb( breadcrumb, services ) );
		return this;
	}

	public BreadcrumbsBuilder addTranslatableStringBreadcrumb( final String breadcrumb ) {
		breadcrumbs.add( new TranslatableStringBreadcrumb( breadcrumb, services ) );
		return this;
	}

	public BreadcrumbsBuilder addUserCardLinkBreadcrumb( final User user ) {
		breadcrumbs.add( new UserCardLinkBreadcrumb( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotoCardBreadcrumb( final Photo photo ) {
		breadcrumbs.add( new PhotoCardBreadcrumb( photo, services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotosByGenreBreadcrumb( final Genre genre ) {
		breadcrumbs.add( new PhotosByGenreBreadcrumb( genre, services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotosByUserBreadcrumb( final User user ) {
		breadcrumbs.add( new PhotosByUserBreadcrumb( user, services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotosByUserAndGenreBreadcrumb( final User user, final Genre genre ) {
		breadcrumbs.add( new PhotosByUserAndGenreBreadcrumb( user, genre, services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotoAppraisalCategoryParameter( final PhotoVotingCategory photoVotingCategory, final Genre genre ) {
		breadcrumbs.add( new PhotoAppraisalCategoryBreadcrumb( photoVotingCategory, services ) );
		return this;
	}

	public BreadcrumbsBuilder addFormattedDateBreadcrumb( final Time date, final Genre genre ) {
		breadcrumbs.add( new FormattedDateBreadcrumb( date, services ) );
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
}
