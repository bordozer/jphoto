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
		return String.format( "New Photo Of Favorite Author - Private message Subject: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( getPhotoAuthor() ), services.getEntityLinkUtilsService().getPhotoCardLink( photo ) );
	}

	@Override
	public String getNotificationText() {
		return String.format( "New Photo Of Favorite Author - Private message Body: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( getPhotoAuthor() ), services.getEntityLinkUtilsService().getPhotoCardLink( photo ) );
	}

	private User getPhotoAuthor() {
		return services.getUserService().load( photo.getUserId() );
	}
}
