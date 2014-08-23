package photo.list;

import common.AbstractTestCase;
import core.services.utils.sql.PhotoListQueryBuilder;
import mocks.GenreMock;
import mocks.UserMock;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PhotoListQueryBuilderTest extends AbstractTestCase {

	public static final String WRONG_SQL = "Wrong SQL";

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void photoListQueryBuilderTest_1_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService );

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos;", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_2_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.sortByUploadTime()
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos ORDER BY photos.uploadTime DESC;", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_3_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.filterByAuthor( new UserMock( 111 ) )
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '111' );", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_4_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.filterByGenre( new GenreMock( 222 ) )
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( photos.genreId = '222' );", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_5_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.filterByAuthor( new UserMock( 111 ) )
			.filterByGenre( new GenreMock( 222 ) )
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( ( photos.userId = '111' ) AND photos.genreId = '222' );", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_6_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.filterByAuthor( new UserMock( 111 ) )
			.filterByGenre( new GenreMock( 222 ) )
			.sortByUploadTime()
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( ( photos.userId = '111' ) AND photos.genreId = '222' ) ORDER BY photos.uploadTime DESC;", queryBuilder.getQuery().build() );
	}
}
