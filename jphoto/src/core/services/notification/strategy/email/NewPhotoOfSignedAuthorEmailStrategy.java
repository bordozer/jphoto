package core.services.notification.strategy.email;

import core.enums.FavoriteEntryType;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.notification.strategy.NotificationData;
import core.services.notification.strategy.message.AbstractPrivateMessageStrategy;
import core.services.notification.text.email.NewPhotoOfSignedAuthorEmailTextStrategy;
import core.services.security.Services;
import core.services.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NewPhotoOfSignedAuthorEmailStrategy extends AbstractPrivateMessageStrategy {

	private final Photo photo;

	public NewPhotoOfSignedAuthorEmailStrategy( final Photo photo, final Services services ) {
		super( services );
		this.photo = photo;
	}

	@Override
	protected List<NotificationData> getUsersSendNotificationTo() {
		final UserService userService = services.getUserService();

		final ArrayList<NotificationData> result = newArrayList();

		final List<Integer> usersWhoSignedGetNotificationAboutNewPhoto = services.getFavoritesService().getAllUsersIdsWhoHasThisEntryInFavorites( photo.getUserId(), FavoriteEntryType.NEW_PHOTO_NOTIFICATION );
		for ( final int userId : usersWhoSignedGetNotificationAboutNewPhoto ) {
			final User user = userService.load( userId );
			result.add( new NotificationData( user, new NewPhotoOfSignedAuthorEmailTextStrategy( photo, services ) ) );
		}

		return result;
	}
}

