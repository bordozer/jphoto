package ui.services.breadcrumbs;

import core.general.activity.ActivityType;
import core.general.photo.Photo;
import core.general.user.User;
import elements.PageTitleData;

public interface PageTitleService {

	PageTitleData photoCardTitle( final Photo photo, final User accessor, final String title );

	PageTitleData userPhotoVotingData( final Photo photo, final User accessor );

	PageTitleData userPhotoPreviewsData( final Photo photo, final User accessor );

	PageTitleData getActivityStreamData( final ActivityType activityType );

//	PageTitleData getPhotoActivityStreamData( final Photo photo );
}
