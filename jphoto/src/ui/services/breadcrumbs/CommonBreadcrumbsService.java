package ui.services.breadcrumbs;

import elements.PageTitleData;
import ui.activity.ActivityType;

public interface CommonBreadcrumbsService {

	PageTitleData getActivityStreamBreadcrumbs( final ActivityType activityType );
}
