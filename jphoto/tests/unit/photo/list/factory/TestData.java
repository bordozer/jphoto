package photo.list.factory;

import core.enums.RestrictionType;
import core.general.genre.Genre;
import core.general.user.User;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

class TestData {

	public final User accessor;

	public User user;

	public Genre genre;

	public Date currentTime;

	public Date votingTimeFrom;

	public Date votingTimeTo;

	public List<Integer> photoIds;

	public List<Pair<Integer, RestrictionType>> restrictedPhotos;

	TestData() {
		accessor = new User( 111 );
		accessor.setName( "Accessor" );
	}

	TestData( final User accessor ) {
		this.accessor = accessor;
	}
}
