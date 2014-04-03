package ui.services.breadcrumbs;

import core.general.activity.ActivityType;
import elements.PageTitleData;

public interface PageTitleService {

	PageTitleData getActivityStreamData( final ActivityType activityType );
}
