package ui.services.breadcrumbs;

import ui.elements.PageTitleData;
import ui.activity.ActivityType;

public interface CommonBreadcrumbsService {

	PageTitleData getActivityStreamBreadcrumbs( final ActivityType activityType );
}
