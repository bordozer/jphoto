package menuItems.photo;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.unit.common.AbstractTestCase;

public class PhotoMenuItemTestData {

    private final User photoAuthor;
    private final User accessor;

    private final Genre genre;
    private final Photo photo;

    PhotoMenuItemTestData() {
        photoAuthor = new User(222);
        photoAuthor.setName("Photo Author");
        photoAuthor.setLanguage(AbstractTestCase.MENU_LANGUAGE);

        accessor = new User(333);
        accessor.setName("Just a User");
        accessor.setLanguage(AbstractTestCase.MENU_LANGUAGE);

        genre = new Genre();
        genre.setId(433);
        genre.setName("Photo category");

        photo = new Photo();
        photo.setId(567);
        photo.setName("The photo");
        photo.setUserId(photoAuthor.getId());
        photo.setGenreId(genre.getId());
    }

    public User getPhotoAuthor() {
        return photoAuthor;
    }

    public User getAccessor() {
        return accessor;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Genre getGenre() {
        return genre;
    }
}
