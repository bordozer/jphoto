package photo.list;

import common.AbstractTestCase;
import core.general.user.UserMembershipType;
import core.services.utils.sql.PhotoListQueryBuilder;
import mocks.GenreMock;
import mocks.UserMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

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

	@Test
	public void photoListQueryBuilderTest_7_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.filterByAuthor( new UserMock( 111 ) )
			.forPage( 3, 16 )
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '111' ) LIMIT 16 OFFSET 32;", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_8_Test() {
		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.filterByMembershipType( UserMembershipType.MODEL )
			.forPage( 3, 16 )
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) WHERE ( users.membershipType = '2' ) LIMIT 16 OFFSET 32;", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_9_Test() {

		final Date timeFrom = dateUtilsService.parseDateTime( "2014-08-20 11:12:13" );
		final Date timeTo = dateUtilsService.parseDateTime( "2014-08-23 20:02:53" );

		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.votingBetween( timeFrom, timeTo )
			.forPage( 1, 16 )
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '2014-08-20 00:00:00' ) AND photoVoting.votingTime <= '2014-08-23 23:59:59' ) GROUP BY photos.id LIMIT 16;", queryBuilder.getQuery().build() );
	}

	@Test
	public void photoListQueryBuilderTest_10_Test() {

		final Date timeFrom = dateUtilsService.parseDateTime( "2014-08-20 11:12:13" );
		final Date timeTo = dateUtilsService.parseDateTime( "2014-08-23 20:02:53" );

		final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder( dateUtilsService )
			.votingBetween( timeFrom, timeTo )
			.forPage( 1, 16 )
			.sortBySumMarks()
			;

		assertEquals( WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '2014-08-20 00:00:00' ) AND photoVoting.votingTime <= '2014-08-23 23:59:59' ) GROUP BY photos.id ORDER BY SUM( photoVoting.mark ) DESC LIMIT 16;", queryBuilder.getQuery().build() );
	}
}
