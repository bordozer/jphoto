package ui.services.breadcrumbs;

import core.general.activity.ActivityType;
import core.general.photo.Photo;
import core.general.user.User;
import elements.PageTitleData;

public interface PageTitleService {

	PageTitleData userPhotoVotingData( final Photo photo, final User accessor );

	PageTitleData userPhotoPreviewsData( final Photo photo, final User accessor );

	PageTitleData getActivityStreamData( final ActivityType activityType );
}
