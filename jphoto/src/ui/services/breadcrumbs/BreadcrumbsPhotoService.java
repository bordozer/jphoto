package ui.services.breadcrumbs;

import core.general.photo.Photo;
import core.general.user.User;
import elements.PageTitleData;
import ui.controllers.photos.edit.PhotoEditWizardStep;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

public interface BreadcrumbsPhotoService {

	PageTitleData getUploadPhotoBreadcrumbs( final User user, final PhotoEditWizardStep wizardStep );

	PageTitleData getPhotoEditDataBreadcrumbs( final Photo photo );

	PageTitleData getPhotoCardBreadcrumbs( final Photo photo, final User accessor );

	PageTitleData getPhotoActivitiesBreadcrumbs( final Photo photo, final User accessor );

	PageTitleData getUserPhotoPreviewsBreadcrumbs( final Photo photo, final User accessor );

	PageTitleData getPhotoAppraisementBreadcrumbs( final Photo photo, final User accessor );
}