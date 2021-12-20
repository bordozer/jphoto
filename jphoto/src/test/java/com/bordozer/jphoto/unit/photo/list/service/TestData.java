package photo.list.service;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import mocks.GenreMock;
import mocks.UserMock;

class TestData {

    User accessor;
    Genre genre;
    User user;

    TestData() {
        accessor = new UserMock(111);
        accessor.setName("Accessor");

        user = new UserMock(112);
        user.setName("User card owner");

        genre = new GenreMock(222);
    }
}

