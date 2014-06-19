package mocks;

import core.general.photo.Photo;

import java.util.Date;

public class PhotoMock extends Photo {

	public PhotoMock() {
		this( 777 );
	}

	public PhotoMock( final int id ) {
		setId( id );
		setName( String.format( "Photo #%d", id ) );
		setGenreId( new GenreMock().getId() );
	}
}
