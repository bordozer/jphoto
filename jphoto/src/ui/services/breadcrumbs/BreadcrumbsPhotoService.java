package ui.services.breadcrumbs;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import elements.PageTitleData;
import ui.controllers.photos.edit.PhotoEditWizardStep;

public interface BreadcrumbsPhotoService {

	PageTitleData getUploadPhotoBreadcrumbs( final User user, final PhotoEditWizardStep wizardStep );

	PageTitleData getPhotoEditDataBreadcrumbs( final Photo photo, final User user, final Genre genre );
}
