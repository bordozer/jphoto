package core.services.notification.text.email;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.notification.text.AbstractNotificationTextStrategy;
import core.services.security.Services;

public class NewPhotoOfSignedAuthorEmailTextStrategy extends AbstractNotificationTextStrategy {

	private final Photo photo;

	public NewPhotoOfSignedAuthorEmailTextStrategy( final Photo photo, final Services services ) {
		super( services );
		this.photo = photo;
	}

	@Override
	public String getNotificationSubject() {
		return String.format( "New Photo Of Signed Author - Email Subject: %s has uploaded new photo '%s'", getPhotoAuthor(), services.getEntityLinkUtilsService().getPhotoCardLink( photo ) );
	}

	@Override
	public String getNotificationText() {
		return String.format( "New Photo Of Signed Author - Email Body: %s has uploaded new photo '%s'", getPhotoAuthor(), services.getEntityLinkUtilsService().getPhotoCardLink( photo ) );
	}

	private User getPhotoAuthor() {
		return services.getUserService().load( photo.getUserId() );
	}
}
