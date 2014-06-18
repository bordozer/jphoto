package mocks;

import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GenreMock extends Genre {

	public GenreMock() {
		this( 333 );
	}

	public GenreMock( final int id ) {
		setId( id );
		setName( String.format( "Mock Photo Category #%d", id ) );
		setPhotoVotingCategories( getFakePhotoVotingCategories() );
	}

	private List<PhotoVotingCategory> getFakePhotoVotingCategories() {
		final List<PhotoVotingCategory> photoVotingCategories = newArrayList();

		final PhotoVotingCategory category1 = new PhotoVotingCategory();
		category1.setId( 3331 );
		category1.setName( "Appraisal category 1" );
		photoVotingCategories.add( category1 );

		final PhotoVotingCategory category2 = new PhotoVotingCategory();
		category2.setId( 3332 );
		category2.setName( "Appraisal category 2" );
		photoVotingCategories.add( category2 );

		return photoVotingCategories;
	}
}
