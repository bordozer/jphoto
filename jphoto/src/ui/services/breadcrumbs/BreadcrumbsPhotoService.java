package ui.services.breadcrumbs;

import core.general.photo.Photo;
import core.general.user.User;
import ui.controllers.photos.edit.PhotoEditWizardStep;
import ui.elements.PageTitleData;

public interface BreadcrumbsPhotoService {

	PageTitleData getUploadPhotoBreadcrumbs( final User user, final PhotoEditWizardStep wizardStep );

	PageTitleData getPhotoEditDataBreadcrumbs( final Photo photo );

	PageTitleData getPhotoCardBreadcrumbs( final Photo photo, final User accessor );

	PageTitleData getPhotoActivitiesBreadcrumbs( final Photo photo, final User accessor );

	PageTitleData getUserPhotoPreviewsBreadcrumbs( final Photo photo, final User accessor );

	PageTitleData getPhotoAppraisementBreadcrumbs( final Photo photo, final User accessor );
}
