package core.services.notification;

import core.general.message.PrivateMessage;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;

public interface NotificationService {

	void newPhotoNotification( final Photo photo );

	void newCommentNotification( final PhotoComment comment );

	void newPrivateMessage( final PrivateMessage privateMessage );
}
