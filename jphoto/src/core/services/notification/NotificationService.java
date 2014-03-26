package core.services.notification;

import core.general.message.PrivateMessage;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.services.translator.Language;

public interface NotificationService {

	void newPhotoNotification( final Photo photo, final Language language );

	void newCommentNotification( final PhotoComment comment, final Language language );

	void newPrivateMessage( final PrivateMessage privateMessage, final Language language );
}
