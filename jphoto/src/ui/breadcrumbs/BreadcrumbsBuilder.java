package ui.breadcrumbs;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.services.system.Services;

import java.sql.Time;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BreadcrumbsBuilder {

	protected final Services services;

	private final List<AbstractBreadcrumb> breadcrumbs = newArrayList();

	public BreadcrumbsBuilder( final Services services ) {
		this.services = services;

		breadcrumbs.add( new MainPageBreadcrumb( services ) );
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

	public BreadcrumbsBuilder addPhotosByUserAndGenreBreadcrumb( final User user, final Genre genre ) {
		breadcrumbs.add( new PhotosByUserAndGenreBreadcrumb( user, genre, services ) );
		return this;
	}

	public BreadcrumbsBuilder addPhotoAppraisalCategoryParameter( final PhotoVotingCategory photoVotingCategory, final Genre genre ) {
		breadcrumbs.add( new PhotoAppraisalCategoryParameter( photoVotingCategory, services ) );
		return this;
	}

	public BreadcrumbsBuilder addFormattedDateBreadcrumb( final Time date, final Genre genre ) {
		breadcrumbs.add( new FormattedDateBreadcrumb( date, services ) );
		return this;
	}
}
