package ui.services.breadcrumbs;

import ui.activity.ActivityType;
import ui.elements.PageTitleData;

public interface CommonBreadcrumbsService {

	PageTitleData getActivityStreamBreadcrumbs( final ActivityType activityType );
}
