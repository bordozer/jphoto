package core.services.system;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;

public interface NotificationService {

	void newPhotoNotification( final Photo photo );

	void newCommentNotification( final PhotoComment comment );
}
