package core.general.cache.entries;

import core.interfaces.Cacheable;

public class UserGenrePhotosQty implements Cacheable {

	private final int userId;
	private final int genreId;
	private final int photosQty;

	public UserGenrePhotosQty( final int userId, final int genreId, final int photosQty ) {
		this.userId = userId;
		this.genreId = genreId;
		this.photosQty = photosQty;
	}

	public int getUserId() {
		return userId;
	}

	public int getGenreId() {
		return genreId;
	}

	public int getPhotosQty() {
		return photosQty;
	}
}
