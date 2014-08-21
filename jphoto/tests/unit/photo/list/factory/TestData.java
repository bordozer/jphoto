package photo.list.factory;

import core.enums.RestrictionType;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

class TestData {

	public final User accessor;

	public User user;

	public Genre genre;

	public Date currentTime;

	public Date votingTimeFrom;

	public Date votingTimeTo;

	public List<Photo> photos;

	public List<Pair<Integer, RestrictionType>> restrictedPhotos;

	TestData( final User accessor ) {
		this.accessor = accessor;

		photos = getPhotos();
	}

	TestData() {
		accessor = new User( 111 );
		accessor.setName( "Accessor" );

		photos = getPhotos();
	}

	private List<Photo> getPhotos() {
		final List<Photo> result = newArrayList();

		result.add( getPhoto( 2000 ) );
		result.add( getPhoto( 2001 ) );
		result.add( getPhoto( 2002 ) );
		result.add( getPhoto( 2003 ) );
		result.add( getPhoto( 2004 ) );
		result.add( getPhoto( 2005 ) );

		return result;
	}

	private Photo getPhoto( final int photoId ) {
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setName( String.format( "Photo #%d", photoId ) );
		photo.setUserId( photoId + 1000 );

		return photo;
	}

	public List<Integer> getPhotoIds() {
		final List<Integer> result = newArrayList();
		for ( final Photo photo : photos ) {
			result.add( photo.getId() );
		}
		return result;
	}
}
