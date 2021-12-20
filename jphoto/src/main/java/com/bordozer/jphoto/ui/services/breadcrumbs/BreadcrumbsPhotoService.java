package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.elements.PageTitleData;

public interface BreadcrumbsPhotoService {

    PageTitleData getUploadPhotoBreadcrumbs(final User user);

    PageTitleData getPhotoEditDataBreadcrumbs(final Photo photo);

    PageTitleData getPhotoCardBreadcrumbs(final Photo photo, final User accessor);

    PageTitleData getPhotoActivitiesBreadcrumbs(final Photo photo, final User accessor);

    PageTitleData getUserPhotoPreviewsBreadcrumbs(final Photo photo, final User accessor);

    PageTitleData getPhotoAppraisementBreadcrumbs(final Photo photo, final User accessor);
}
