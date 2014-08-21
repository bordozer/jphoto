package photo.list.filtering;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.utils.DateUtilsService;
import mocks.PhotoMock;
import mocks.UserMock;

import java.util.Date;

public class TestData {

	final Date currentTime;

	User accessor;

	User photoAuthor;

	public User user;

	Photo photo;

	public boolean isRestricted;

	public TestData( final DateUtilsService dateUtilsService ) {
		currentTime = dateUtilsService.parseDateTime( "2014-08-20 18:22:01" );

		this.accessor = new UserMock(333 );
		this.photoAuthor = new UserMock( 444 );
		this.user = new UserMock( 555 );

		this.photo = new PhotoMock( 11111 );
		photo.setUserId( photoAuthor.getId() );
	}
}
