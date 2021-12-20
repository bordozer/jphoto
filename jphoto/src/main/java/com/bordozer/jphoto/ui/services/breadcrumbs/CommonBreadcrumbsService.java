package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.ui.activity.ActivityType;
import com.bordozer.jphoto.ui.elements.PageTitleData;

public interface CommonBreadcrumbsService {

    PageTitleData getActivityStreamBreadcrumbs(final ActivityType activityType);

    PageTitleData genGenreListBreadcrumbs();
}
