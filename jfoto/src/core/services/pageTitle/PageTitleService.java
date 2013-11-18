package core.services.pageTitle;

import core.general.photo.Photo;
import core.general.user.User;
import elements.PageTitleData;

public interface PageTitleService {

	PageTitleData photoCardTitle( final Photo photo, final User accessor );

	PageTitleData userPhotoVotingData( final Photo photo, final User accessor );

	PageTitleData userPhotoPreviewsData( final Photo photo, final User accessor );

	PageTitleData getActivityStreamData();
}
