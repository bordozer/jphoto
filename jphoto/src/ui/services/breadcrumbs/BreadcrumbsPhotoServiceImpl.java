package ui.services.breadcrumbs;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.controllers.photos.edit.PhotoEditWizardStep;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.PhotoEditBreadcrumb;
import ui.services.breadcrumbs.items.PhotoUploadBreadcrumb;

public class BreadcrumbsPhotoServiceImpl implements BreadcrumbsPhotoService {

	@Autowired
	private Services services;

	@Override
	public PageTitleData getUploadPhotoBreadcrumbs( final User user, final PhotoEditWizardStep wizardStep ) {

		final String title = BreadcrumbsBuilder.pageTitle( new PhotoUploadBreadcrumb( services ), services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( new PhotoUploadBreadcrumb( services ), services ).build();

		final String breadcrumbs = root()
			.addUserCardLinkBreadcrumb( user )
			.addPhotoUploadBreadcrumb()
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoEditDataBreadcrumbs( final Photo photo, final User user, final Genre genre ) {

		final String title = BreadcrumbsBuilder.pageTitle( new PhotoEditBreadcrumb( services ), services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( new PhotoEditBreadcrumb( services ), services ).build();

		final String breadcrumbs = root()
			.addUserCardLinkBreadcrumb( user )
			.addPhotoCardBreadcrumb( photo )
			.addPhotoEditBreadcrumb()
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	private BreadcrumbsBuilder root() {
		return BreadcrumbsBuilder.breadcrumbs( services );
	}
}
