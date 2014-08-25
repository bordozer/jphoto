package sql;

import common.AbstractTestCase;
import core.services.utils.sql.PhotoQueryServiceImpl;
import ui.context.Environment;
import ui.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.photo.PhotoListCriteriasServiceImpl;
import core.services.system.ConfigurationService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PhotoListCriteriasSQLTest extends AbstractTestCase {

	public static final int MIN_MARKS = 17;
	public static final int DAYS_OFFSET = 3;
	public static final int PHOTO_LIST_PHOTO_TOP_QTY = 4;
	public static final int CURRENT_PAGE = 3;
	public static final int ITEMS_ON_PAGE = 20;
	public static final int MIN_MARK_FOR_BEST = PhotoQueryServiceImpl.MIN_MARK_FOR_BEST;

	@Before
	public void setup() {
		super.setup();

		final User user = new User();

		final Environment environment = new Environment( user );
		EnvironmentContext.setEnv( environment );
	}

	@Test
	public void photosByUserAndGenreTopBest() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final User user = new User();
		user.setId( 999 );

		final Genre genre = new Genre();
		genre.setId( 777 );

		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenreTopBest( user, genre, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photos.userId = '999' ) AND photos.genreId = '777' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT %d;", MIN_MARK_FOR_BEST, PHOTO_LIST_PHOTO_TOP_QTY );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void photosByUserAndGenreBest() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final User user = new User();
		user.setId( 999 );

		final Genre genre = new Genre();
		genre.setId( 777 );

		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenreAbsolutelyBest( user, genre, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photos.userId = '999' ) AND photos.genreId = '777' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT %d OFFSET 40;", MIN_MARK_FOR_BEST, ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void photosByVotingCategory() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final User user = new User();
		user.setId( 999 );

		final PhotoVotingCategory votingCategory = new PhotoVotingCategory();
		votingCategory.setId( 888 );

		final PhotoListCriterias criterias = photoListCriteriasService.getForAppraisedByUserPhotos( votingCategory, user, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingCategoryId = '888' ) AND photoVoting.userId = '999' ) GROUP BY photos.id ORDER BY photos.uploadTime DESC LIMIT %d OFFSET 40;", ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void photosOnPeriod() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final Date fDateFrom = dateUtilsService.parseDateTime( "2013-01-01", "11:22:33" );
		final Date fDateTo = dateUtilsService.parseDateTime( "2013-01-05", "09:11:44" );

		final PhotoListCriterias criterias = photoListCriteriasService.getForPeriod( fDateFrom, fDateTo, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id "
													 + "FROM photos AS photos "
													 + "WHERE ( ( photos.uploadTime >= '%s' ) AND photos.uploadTime <= '%s' ) "
													 + "ORDER BY photos.uploadTime DESC "
													 + "LIMIT %d OFFSET 40;"
			, getFirstSecondOfDay( fDateFrom ), getLastSecondOfDay( fDateTo ), ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void photosOnPeriodBest() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final Date fDateFrom = dateUtilsService.parseDateTime( "2013-01-01", "11:22:33" );
		final Date fDateTo = dateUtilsService.parseDateTime( "2013-01-05", "09:11:44" );

		final PhotoListCriterias criterias = photoListCriteriasService.getForPeriodBest( fDateFrom, fDateTo, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id "
													 + "FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) "
													 + "WHERE ( ( photoVoting.votingTime >= '%s' ) "
													 + "AND photoVoting.votingTime <= '%s' ) "
													 + "GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' "
													 + "ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC "
													 + "LIMIT %d "
													 + "OFFSET 40;", getFirstSecondOfDay( fDateFrom ), getLastSecondOfDay( fDateTo ), MIN_MARKS, ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void photosByMembershipTopBest() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final PhotoListCriterias criterias = photoListCriteriasService.getForMembershipTypeTopBest( UserMembershipType.MODEL, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( ( users.membershipType = '2' ) AND photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT %d;", getDateFrom(), getDateTo(), MIN_MARKS, PHOTO_LIST_PHOTO_TOP_QTY );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void photosByMembership() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final PhotoListCriterias criterias = photoListCriteriasService.getForMembershipType( UserMembershipType.MAKEUP_MASTER, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) WHERE ( users.membershipType = '3' ) ORDER BY photos.uploadTime DESC LIMIT %d OFFSET 40;", ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void photosByMembershipBest() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final PhotoListCriterias criterias = photoListCriteriasService.getForMembershipTypeBestForPeriod( UserMembershipType.MODEL, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id "
													 + "FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) "
													 + "INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) "
													 + "WHERE ( ( ( users.membershipType = '2' ) "
													 + "AND photoVoting.votingTime >= '%s' ) "
													 + "AND photoVoting.votingTime <= '%s' ) "
													 + "GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' "
													 + "ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC "
													 + "LIMIT %d "
													 + "OFFSET 40;"
			, getDateFrom(), getDateTo(), MIN_MARKS, ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void userCardUserPhotosLast() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final User user = new User();
		user.setId( 999 );

		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosLast( user, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '999' ) ORDER BY photos.uploadTime DESC LIMIT %d OFFSET 40;", ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	@Test
	public void userCardUserLastVotedForPhotos() {

		final PhotoListCriteriasServiceImpl photoListCriteriasService = getPhotoListCriteriasService();

		final User user = new User();
		user.setId( 999 );

		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardLastAppraisedPhotos( user, EnvironmentContext.getCurrentUser() );

		final SqlIdsSelectQuery idsSelectQuery = photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, CURRENT_PAGE, ITEMS_ON_PAGE );

		final String actualResult = idsSelectQuery.build();
		final String expectedResult = String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photoVoting.userId = '999' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC LIMIT %d OFFSET 40;", ITEMS_ON_PAGE );

		assertEquals( "Actual SQL is wrong", expectedResult, actualResult );
	}

	private PhotoListCriteriasServiceImpl getPhotoListCriteriasService() {
		final PhotoListCriteriasServiceImpl photoListCriteriasService = new PhotoListCriteriasServiceImpl();
		photoListCriteriasService.setConfigurationService( getConfigurationService() );
		photoListCriteriasService.setDateUtilsService( dateUtilsService );

		return photoListCriteriasService;
	}

	private ConfigurationService getConfigurationService() {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO ) ).andReturn( MIN_MARKS ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS ) ).andReturn( DAYS_OFFSET ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY ) ).andReturn( PHOTO_LIST_PHOTO_TOP_QTY ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );
		return configurationService;
	}

	private String getDateTo() {
		return dateUtilsService.formatDateTime( dateUtilsService.getLastSecondOfDay( dateUtilsService.getCurrentDate() ) );
	}

	private String getDateFrom() {
		return dateUtilsService.formatDateTime( dateUtilsService.getFirstSecondOfDay( dateUtilsService.getDatesOffsetFromCurrentDate( -DAYS_OFFSET ) ) );
	}

	private String getFirstSecondOfDay( final Date date ) {
		return dateUtilsService.formatDateTime( dateUtilsService.getFirstSecondOfDay( date ) );
	}

	private String getLastSecondOfDay( final Date date ) {
		return dateUtilsService.formatDateTime( dateUtilsService.getLastSecondOfDay( date ) );
	}
}
