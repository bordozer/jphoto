package core.services.notification.text.message;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.notification.text.AbstractNotificationTextStrategy;
import core.services.security.Services;

public class NewPhotoOfFavoriteAuthorPrivateMessageTextStrategy extends AbstractNotificationTextStrategy {

	private final Photo photo;

	public NewPhotoOfFavoriteAuthorPrivateMessageTextStrategy( final Photo photo, final Services services ) {
		super( services );
		this.photo = photo;
	}

	@Override
	public String getNotificationSubject() {
		return String.format( "Private message - Subject: %s has uploaded new photo %s", getPhotoAuthor(), photo );
	}

	@Override
	public String getNotificationText() {
		return String.format( "Private message - Body: %s has uploaded new photo %s", getPhotoAuthor(), photo );
	}

	private User getPhotoAuthor() {
		return services.getUserService().load( photo.getUserId() );
	}
}
