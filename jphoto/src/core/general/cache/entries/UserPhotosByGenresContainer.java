package core.general.cache.entries;

import core.general.genre.Genre;
import core.interfaces.Cacheable;
import controllers.users.card.UserCardGenreInfo;

import java.util.Map;

public class UserPhotosByGenresContainer implements Cacheable {

	private final int userId;
	private final Map<Genre, UserCardGenreInfo> userPhotosByGenresMap;

	public UserPhotosByGenresContainer( final int userId, final Map<Genre, UserCardGenreInfo> userPhotosByGenresMap ) {
		this.userId = userId;
		this.userPhotosByGenresMap = userPhotosByGenresMap;
	}

	public int getUserId() {
		return userId;
	}

	public Map<Genre, UserCardGenreInfo> getUserPhotosByGenresMap() {
		return userPhotosByGenresMap;
	}
}
