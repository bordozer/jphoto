package ui.services.breadcrumbs;

import core.general.user.User;
import elements.PageTitleData;

public interface PageTitlePhotoCommentUtilsService {

	PageTitleData getPhotoCommentsToUserData( User user );

	PageTitleData getUnreadPhotoCommentsToUserData( User user );

	String getPhotoRootTranslated();
}
