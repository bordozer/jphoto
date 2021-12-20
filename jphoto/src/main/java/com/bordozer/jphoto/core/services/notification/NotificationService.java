package com.bordozer.jphoto.core.services.notification;

import com.bordozer.jphoto.core.general.message.PrivateMessage;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;

public interface NotificationService {

    void newPhotoNotification(final Photo photo);

    void newCommentNotification(final PhotoComment comment);

    void newPrivateMessage(final PrivateMessage privateMessage);
}
