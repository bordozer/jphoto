package core.services.notification.data;

import core.enums.FavoriteEntryType;
import core.general.photo.Photo;
import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.services.notification.send.AbstractSendStrategy;
import core.services.notification.text.AbstractNotificationTextStrategy;
import core.services.security.Services;
import core.services.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NewPhotoOfFavoriteAuthorDataHolder extends AbstractNotificationDataHolder {

	private Photo photo;

	public NewPhotoOfFavoriteAuthorDataHolder( final Photo photo, final Services services ) {
		super( services );
		this.photo = photo;
	}

	@Override
	public List<NotificationData> getNotificationsData( final AbstractSendStrategy sendStrategy, final AbstractNotificationTextStrategy notificationTextStrategy ) {
		final UserService userService = services.getUserService();

		final ArrayList<NotificationData> result = newArrayList();

		final List<Integer> usersWhoHasPhotoAuthorInFavorites = services.getFavoritesService().getAllUsersIdsWhoHasThisEntryInFavorites( photo.getUserId(), FavoriteEntryType.USER );
		for ( final int userId : usersWhoHasPhotoAuthorInFavorites ) {
			final User user = userService.load( userId );

			if( user.getEmailNotificationTypes().contains( EmailNotificationType.PHOTO_OF_FAVORITE_MEMBER ) ) {
				result.add( new NotificationData( user, notificationTextStrategy, sendStrategy ) );
			}
		}

		return result;
	}
}
