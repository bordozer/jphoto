package ui.services.breadcrumbs;

import core.general.activity.ActivityType;
import elements.PageTitleData;

public interface CommonBreadcrumbsService {

	PageTitleData getActivityStreamBreadcrumbs( final ActivityType activityType );
}
