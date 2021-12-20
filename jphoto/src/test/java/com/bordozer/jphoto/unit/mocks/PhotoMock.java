package mocks;

import com.bordozer.jphoto.core.general.photo.Photo;

public class PhotoMock extends Photo {

    public PhotoMock() {
        this(777);
    }

    public PhotoMock(final int id) {
        setId(id);
        setName(String.format("Photo #%d", id));
        setGenreId(new mocks.GenreMock().getId());
    }

    @Override
    public String toString() {
        return String.format("Photo #%d, author #%d", getId(), getUserId());
    }
}
