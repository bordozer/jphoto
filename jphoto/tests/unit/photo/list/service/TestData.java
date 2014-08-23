package photo.list.service;

import core.general.genre.Genre;
import core.general.user.User;
import mocks.GenreMock;
import mocks.UserMock;

class TestData {

	User accessor;
	Genre genre;

	TestData() {
		accessor = new UserMock( 111 );
		genre = new GenreMock( 222 );
	}
}

