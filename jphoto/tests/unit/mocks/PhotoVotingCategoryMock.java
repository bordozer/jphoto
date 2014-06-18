package mocks;

import core.general.photo.PhotoVotingCategory;

public class PhotoVotingCategoryMock extends PhotoVotingCategory {

	public PhotoVotingCategoryMock( final int id ) {
		setId( id );
		setName( String.format( "Appraisal category #%d", id ) );
	}
}
