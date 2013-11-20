package core.general.cache.keys;

public class UserGenreCompositeKey implements CacheCompositeKey {

	private final int userId;
	private final int genreId;

	public UserGenreCompositeKey( final int userId, final int genreId ) {
		this.userId = userId;
		this.genreId = genreId;
	}

	public int getUserId() {
		return userId;
	}

	public int getGenreId() {
		return genreId;
	}

	@Override
	public int hashCode() {
		return userId * genreId;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( ! ( obj instanceof UserGenreCompositeKey ) ) {
			return false;
		}

		final UserGenreCompositeKey key = ( UserGenreCompositeKey ) obj;
		return key.getUserId() == userId && key.getGenreId() == genreId;
	}

	@Override
	public String toString() {
		return String.format( "UserGenreCompositeKey: userId=%d, genreId=%d", userId, genreId );
	}
}
