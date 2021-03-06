package photo.list.filtering;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import mocks.PhotoMock;
import mocks.UserMock;

import java.util.Date;

public class TestData {

    final Date currentTime;

    User accessor;

    User photoAuthor;

    User user;

    Photo photo;

    boolean isRestricted;

    boolean isPhotoWithingAnonymousPeriod;
    boolean isPhotoAuthorInInvisibilityList;

    boolean isPhotoAuthorHidden;

    public TestData(final DateUtilsService dateUtilsService) {
        currentTime = dateUtilsService.parseDateTime("2014-08-20 18:22:01");

        this.accessor = new UserMock(333);
        this.photoAuthor = new UserMock(444);
        this.user = new UserMock(555);

        this.photo = new PhotoMock(11111);
        photo.setUserId(photoAuthor.getId());
    }
}
