package ui.services.breadcrumbs;

import core.general.user.User;
import elements.PageTitleData;

public interface BreadcrumbsPhotoCommentService {

	PageTitleData getPhotoCommentsToUserData( User user );

	PageTitleData getUnreadPhotoCommentsToUserData( User user );

	String getPhotoRootTranslated();
}
