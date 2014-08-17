package photo.list.factory;

import core.enums.RestrictionType;
import core.general.genre.Genre;
import core.general.user.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

class TestData {

	public final User accessor;

	public User user;

	public Genre genre;

	public Date currentTime;

	public List<Integer> photoIds;

	public List<Map<Integer, RestrictionType>> restrictedPhotos;

	TestData() {
		accessor = new User( 111 );
		accessor.setName( "Accessor" );
	}
}
