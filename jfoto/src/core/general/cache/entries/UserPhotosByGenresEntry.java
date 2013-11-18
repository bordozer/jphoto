package core.general.cache.entries;

import core.general.genre.Genre;
import core.interfaces.Cacheable;
import controllers.users.card.UserCardGenreInfo;

import java.util.Map;

public class UserPhotosByGenresEntry implements Cacheable {

	private final int userId;
	private Map<Genre, UserCardGenreInfo> userPhotosByGenresMap;

	public UserPhotosByGenresEntry( final int userId ) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public Map<Genre, UserCardGenreInfo> getUserPhotosByGenresMap() {
		return userPhotosByGenresMap;
	}

	public void setUserPhotosByGenresMap( final Map<Genre, UserCardGenreInfo> userPhotosByGenresMap ) {
		this.userPhotosByGenresMap = userPhotosByGenresMap;
	}
}
