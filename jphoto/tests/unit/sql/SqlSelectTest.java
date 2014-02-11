package sql;

import common.AbstractTestCase;
import core.services.dao.BaseEntityDao;
import core.services.dao.PhotoDaoImpl;
import core.services.dao.PhotoVotingDaoImpl;
import core.services.dao.UserDaoImpl;
import org.junit.Before;
import org.junit.Test;
import sql.builder.*;

import java.util.Date;

import static org.junit.Assert.*;

public class SqlSelectTest extends AbstractTestCase {

	private final static String FIELD_ID = BaseEntityDao.ENTITY_ID;

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void table() {
		final SqlTable mainTable1 = new SqlTable( "photos" );
		final SqlTable mainTable2 = new SqlTable( "users" );
		final SqlTable mainTable3 = new SqlTable( "photos" );

		assertFalse( "Objects are equal but should not be", mainTable1.equals( mainTable2 ) );

		assertTrue( "Objects are not equal", mainTable1.equals( mainTable1 ) );
		assertTrue( "Objects are not equal", mainTable1.equals( mainTable3 ) );
	}

	@Test
	public void selectColumn() {
		final SqlTable table = new SqlTable( "photos" );

		final SqlColumnSelect column1 = new SqlColumnSelect( table, "column1" );
		final SqlColumnSelect column2 = new SqlColumnSelect( table, "column2" );
		final SqlColumnSelect column3 = new SqlColumnSelect( table, "column1" );

		assertFalse( "Objects are equal but should not be", column1.equals( column2 ) );

		assertTrue( "Objects are not equal", column1.equals( column1 ) );
		assertTrue( "Objects are not equal", column1.equals( column3 ) );

		assertEquals( "Objects are not equal", "photos.column1", column1.build() );
		assertEquals( "Objects are not equal", "photos.column1", column1.buildForClause() );
	}

	@Test
	public void sortColumn() {
		final SqlTable table1 = new SqlTable( "photos" );
		final SqlTable table2 = new SqlTable( "users" );

		final SqlColumnSelect selectCol1 = new SqlColumnSelect( table1, "column1" );
		final SqlColumnSelect selectCol2 = new SqlColumnSelect( table1, "column2" );
		final SqlColumnSelect selectCol3 = new SqlColumnSelect( table2, "column1" );

		final SqlColumnSort column1 = new SqlColumnSort( selectCol1 );
		final SqlColumnSort column2 = new SqlColumnSort( selectCol1, SqlSortOrder.ASC );
		final SqlColumnSort column3 = new SqlColumnSort( selectCol2, SqlSortOrder.ASC );
		final SqlColumnSort column4 = new SqlColumnSort( selectCol3, SqlSortOrder.DESC );

		assertFalse( "Objects are equal but should not be", column1.equals( column3 ) );
		assertFalse( "Objects are equal but should not be", column1.equals( column4 ) );

		assertTrue( "Objects are not equal", column1.equals( column1 ) );
		assertTrue( "Objects are not equal", column1.equals( column2 ) );
	}

	@Test
	public void columnAggregate() {
		final SqlTable table1 = new SqlTable( "photos" );
		final SqlTable table2 = new SqlTable( "users" );

		final SqlColumnSelect selectCol1 = new SqlColumnSelect( table1, "column1" );
		final SqlColumnSelect selectCol2 = new SqlColumnSelect( table1, "column2" );

		final SqlColumnAggregate column1 = new SqlColumnAggregate( selectCol1, SqlFunctions.SUM, "sumcolumn1" );
		final SqlColumnAggregate column2 = new SqlColumnAggregate( selectCol2, SqlFunctions.SUM, "sumcolumn1" );
		final SqlColumnAggregate column3 = new SqlColumnAggregate( selectCol1, SqlFunctions.AVG, "sumcolumn1" );
		final SqlColumnAggregate column4 = new SqlColumnAggregate( selectCol1, SqlFunctions.SUM, "sumcolumn2" );

		final SqlColumnAggregate column6 = new SqlColumnAggregate( selectCol1, SqlFunctions.SUM, "sumcolumn1" );

		final SqlColumnSelect column5 = new SqlColumnSelect( table1, "column1" );

		assertFalse( "Objects are equal but should not be", column1.equals( column2 ) );
		assertFalse( "Objects are equal but should not be", column1.equals( column3 ) );
		assertFalse( "Objects are equal but should not be", column1.equals( column4 ) );
		assertFalse( "Objects are equal but should not be", column1.equals( column5 ) );

		assertTrue( "Objects are not equal", column1.equals( column1 ) );
		assertTrue( "Objects are not equal", column1.equals( column6 ) );

		assertEquals( "Objects are not equal", "SUM( photos.column1 ) AS sumcolumn1", column1.build() );
		assertEquals( "Objects are not equal", "SUM( photos.column1 )", column1.buildForClause() );
	}

	@Test
	public void columnHaving() {
		final SqlTable table1 = new SqlTable( "photos" );
		final SqlTable table2 = new SqlTable( "users" );
		final SqlTable table3 = new SqlTable( "photos" );

		final SqlColumnHaving column1 = new SqlColumnHaving( table1, "column1" );
		final SqlColumnHaving column2 = new SqlColumnHaving( table1, "column2" );
		final SqlColumnHaving column3 = new SqlColumnHaving( table2, "column1" );
		final SqlColumnHaving column4 = new SqlColumnHaving( table3, "column1" );
		final SqlColumnHaving column5 = new SqlColumnHaving( table3, "column2" );

		final SqlColumnSelect column6 = new SqlColumnSelect( table1, "column1" );

		assertFalse( "Objects are equal but should not be", column1.equals( column2 ) );
		assertFalse( "Objects are equal but should not be", column1.equals( column3 ) );
		assertFalse( "Objects are equal but should not be", column1.equals( column5 ) );
		assertFalse( "Objects are equal but should not be", column1.equals( column6 ) );

		assertTrue( "Objects are not equal", column1.equals( column1 ) );
		assertTrue( "Objects are not equal", column1.equals( column4 ) );
	}

	@Test
	public void condition() {
		final SqlTable table1 = new SqlTable( "photos" );

		final SqlColumnSelect column1 = new SqlColumnSelect( table1, "column1" );
		final SqlColumnSelect column2 = new SqlColumnSelect( table1, "column2" );
		final SqlColumnSelect column3 = new SqlColumnSelect( table1, "column1" );

		final SqlCondition condition1 = new SqlCondition( column1, SqlCriteriaOperator.GRATER_THEN, 777, dateUtilsService );
		final SqlCondition condition2 = new SqlCondition( column2, SqlCriteriaOperator.GRATER_THEN, 777, dateUtilsService );
		final SqlCondition condition3 = new SqlCondition( column1, SqlCriteriaOperator.EQUALS, 777, dateUtilsService );
		final SqlCondition condition4 = new SqlCondition( column1, SqlCriteriaOperator.GRATER_THEN, 666, dateUtilsService );
		final SqlCondition condition5 = new SqlCondition( column3, SqlCriteriaOperator.GRATER_THEN, 777, dateUtilsService );

		assertFalse( "Objects are equal but should not be", condition1.equals( condition2 ) );
		assertFalse( "Objects are equal but should not be", condition1.equals( condition3 ) );
		assertFalse( "Objects are equal but should not be", condition1.equals( condition4 ) );

		assertTrue( "Objects are not equal", condition1.equals( condition1 ) );
		assertTrue( "Objects are not equal", condition1.equals( condition5 ) );
	}

	@Test
	public void sqlJoin() {
		final SqlTable table1 = new SqlTable( "photos" );
		final SqlTable table2 = new SqlTable( "users" );

		final SqlColumnSelect column1 = new SqlColumnSelect( table1, "column1" );
		final SqlColumnSelect column2 = new SqlColumnSelect( table2, "column1" );
		final SqlColumnSelect column3 = new SqlColumnSelect( table1, "column2" );
		final SqlColumnSelect column4 = new SqlColumnSelect( table1, "column1" ); // == column1

		final SqlJoinCondition joinCondition1 = new SqlJoinCondition( column1, column2 );
		final SqlJoinCondition joinCondition2 = new SqlJoinCondition( column1, column3 );
		final SqlJoinCondition joinCondition3 = new SqlJoinCondition( column4, column2 );
		final SqlJoinCondition joinCondition4 = new SqlJoinCondition( column4, column2 );

		assertFalse( "Objects are equal but should not be", joinCondition1.equals( joinCondition2 ) );

		assertTrue( "Objects are not equal", joinCondition1.equals( joinCondition1 ) );
		assertTrue( "Objects are not equal", joinCondition1.equals( joinCondition3 ) );
		assertTrue( "Objects are not equal", joinCondition1.equals( joinCondition4 ) );
	}

	@Test
	public void theSimplestSelectAllFields() {
		final SqlTable mainTable = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( mainTable );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.* FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void theDistinct() {
		final SqlTable mainTable = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( mainTable );
		sqlBuilderQuery.setDistinct( true );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT DISTINCT photos.* FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void setSelectField() {
		final SqlTable mainTable = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( mainTable );

		sqlBuilderQuery.addSelect( new SqlColumnSelect( mainTable, "userId" ) );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.userId FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.addSelect( new SqlColumnSelect( mainTable, "genreId" ) );

		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.setDistinct( true );

		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT DISTINCT photos.userId, photos.genreId FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

	}

	@Test
	public void setSelectFieldsDifferentTables() {
		final SqlTable mainTable = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( mainTable );

		final SqlColumnSelect column1 = new SqlColumnSelect( mainTable, "userId" );
		sqlBuilderQuery.addSelect( column1 );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.userId FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.addSelect( new SqlColumnSelect( mainTable, "genreId" ) );

		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		final SqlTable table2 = new SqlTable( "users" );
		final SqlColumnSelect column2 = new SqlColumnSelect( table2, "name" );
		final SqlColumnSelect column3 = new SqlColumnSelect( table2, FIELD_ID );

		sqlBuilderQuery.addSelect( column2 );

		final SqlJoin join2 = SqlJoin.inner( table2, new SqlJoinCondition( column1, column3 ) );
		sqlBuilderQuery.joinTable( join2 );

		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.userId, photos.genreId, users.name FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void setSelectAndSortingFields() {
		final SqlTable mainTable = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( mainTable );

		final SqlColumnSelect column0 = new SqlColumnSelect( mainTable, FIELD_ID );
		final SqlColumnSelect column1 = new SqlColumnSelect( mainTable, "userId" );
		final SqlColumnSelect column2 = new SqlColumnSelect( mainTable, "genreId" );
		final SqlColumnSelect column3 = new SqlColumnSelect( mainTable, "uploadTime" );

		sqlBuilderQuery.addSelect( column1 ).addSelect( column2 );

		sqlBuilderQuery.addSortingAsc( column3 );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos ORDER BY photos.uploadTime ASC;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.addSorting( column0, SqlSortOrder.DESC );

		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos ORDER BY photos.uploadTime ASC, photos.id DESC;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void setSelectAndWhereCriteria() {
		final SqlTable mainTable = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( mainTable );

		final SqlColumnSelect column1 = new SqlColumnSelect( mainTable, "userId" );
		final SqlColumnSelect column2 = new SqlColumnSelect( mainTable, "genreId" );

		sqlBuilderQuery.addSelect( column1 ).addSelect( column2 );

		final SqlColumnSelect idColumn = new SqlColumnSelect( mainTable, FIELD_ID );
		final int value = 777;
		final SqlCondition condition = new SqlCondition( idColumn, SqlCriteriaOperator.EQUALS, value, dateUtilsService );

		final SqlConditionList conditionList = new SqlLogicallyAnd( condition );
		sqlBuilderQuery.setWhere( conditionList );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos WHERE ( photos.id = '777' );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void setSelectAndTwoWhereCriterias() {
		final SqlTable table = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		final SqlColumnSelect column1 = new SqlColumnSelect( table, "userId" );
		final SqlColumnSelect column2 = new SqlColumnSelect( table, "genreId" );

		sqlBuilderQuery.addSelect( column1 ).addSelect( column2 );

		final SqlColumnSelect idColumn = new SqlColumnSelect( table, FIELD_ID );
		final int value1 = 777;
		final SqlCondition condition1 = new SqlCondition( idColumn, SqlCriteriaOperator.EQUALS, value1, dateUtilsService );

		final int value2 = 888;
		final SqlCondition condition2 = new SqlCondition( idColumn, SqlCriteriaOperator.EQUALS, value2, dateUtilsService );

		final int value3 = 999;
		final SqlCondition condition3 = new SqlCondition( idColumn, SqlCriteriaOperator.EQUALS, value3, dateUtilsService );

		final SqlConditionList conditionList1 = new SqlLogicallyAnd( condition1 );
		final SqlConditionList conditionList2 = new SqlLogicallyOr( condition2, condition3 );
		sqlBuilderQuery.setWhere( new SqlLogicallyOr( conditionList1, conditionList2 ) );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos WHERE ( ( photos.id = '777' ) OR ( photos.id = '888' OR photos.id = '999' ) );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void selectWithJoin() {

		final SqlTable tablePhotos = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( tablePhotos );
		final SqlColumnSelect column1 = new SqlColumnSelect( tablePhotos, "userId" );
		final SqlColumnSelect column2 = new SqlColumnSelect( tablePhotos, "genreId" );

		final SqlTable tableUsers = new SqlTable( "users" );
		final SqlColumnSelect column3 = new SqlColumnSelect( tableUsers, "id" );

		final SqlJoin join1 = SqlJoin.leftOuter( tableUsers, new SqlJoinCondition( column1, column3 ) );

		sqlBuilderQuery.addSelect( column1, column2 ).joinTable( join1 );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos LEFT OUTER JOIN users ON ( photos.userId = users.id );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		final SqlTable tableGenres = new SqlTable( "genres" );
		final SqlColumnSelect column5 = new SqlColumnSelect( tableGenres, "id" );
		final SqlJoin join2 = SqlJoin.inner( tableGenres, new SqlJoinCondition( column2, column5 ) );
		sqlBuilderQuery.joinTable( join2 );

		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos LEFT OUTER JOIN users ON ( photos.userId = users.id ) INNER JOIN genres ON ( photos.genreId = genres.id );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void selectWithJoinWithTwoConditions() {
		final SqlTable tablePhotos = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( tablePhotos );
		final SqlColumnSelect column1 = new SqlColumnSelect( tablePhotos, "userId" );
		final SqlColumnSelect column2 = new SqlColumnSelect( tablePhotos, "genreId" );

		final SqlTable tableUsers = new SqlTable( "users" );
		final SqlColumnSelect column3 = new SqlColumnSelect( tableUsers, "id" );
		final SqlColumnSelect column4 = new SqlColumnSelect( tableUsers, "photosInLine" );

		SqlJoinCondition joinCondition1 = new SqlJoinCondition( column1, column3 );
		SqlJoinCondition joinCondition2 = new SqlJoinCondition( column2, column4 );
		final SqlJoin join1 = SqlJoin.leftOuter( tableUsers, joinCondition1, joinCondition2 );

		sqlBuilderQuery.addSelect( column1, column2 ).joinTable( join1 );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos LEFT OUTER JOIN users ON ( photos.userId = users.id AND photos.genreId = users.photosInLine );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void selectWithGrouping() {

		final SqlTable table = new SqlTable( "photos" );
		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		final SqlColumnSelect userIdCol = new SqlColumnSelect( table, "userId" );

		sqlBuilderQuery.addSelect( userIdCol ).addGrouping( userIdCol );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.userId FROM photos AS photos GROUP BY photos.userId;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.addSorting( userIdCol, SqlSortOrder.DESC );
		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.userId FROM photos AS photos GROUP BY photos.userId ORDER BY photos.userId DESC;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void selectWithAggregateColumns() {

		final SqlTable table = new SqlTable( "photos" );
		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		final SqlColumnSelect userIdCol = new SqlColumnSelect( table, "userId" );
		final SqlColumnSelect photoIdCol = new SqlColumnSelect( table, FIELD_ID );
		final SqlColumnAggregate aggregate = new SqlColumnAggregate( photoIdCol, SqlFunctions.COUNT, "photoQty" );

		sqlBuilderQuery.addSelect( userIdCol, aggregate ).addGrouping( userIdCol );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.userId, COUNT( photos.id ) AS photoQty FROM photos AS photos GROUP BY photos.userId;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.addSorting( userIdCol, SqlSortOrder.DESC );
		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.userId, COUNT( photos.id ) AS photoQty FROM photos AS photos GROUP BY photos.userId ORDER BY photos.userId DESC;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void limit() {
		final SqlTable table = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		sqlBuilderQuery.setLimit( 10 );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.* FROM photos AS photos LIMIT 10;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void limitAndOffset() {
		final SqlTable table = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		sqlBuilderQuery.setOffset( 10 );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.* FROM photos AS photos LIMIT 2147483647 OFFSET 10;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.setLimit( 25 );

		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT photos.* FROM photos AS photos LIMIT 25 OFFSET 10;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void resetLimitAndOffset() {
		final SqlTable table = new SqlTable( "photos" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		sqlBuilderQuery.setLimit( 10 );
		sqlBuilderQuery.setOffset( 10 );

		sqlBuilderQuery.resetLimitAndOffset();

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.* FROM photos AS photos;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void bestPhotosId() {
		final SqlSelectQuery selectQuery = getQuery();

		final String actualValue = selectQuery.build();
		final String expectedValue = "SELECT photoVoting.photoId, SUM( photoVoting.mark ) AS summark "
									 + "FROM photoVoting AS photoVoting "
									 + "WHERE ( photoVoting.votingTime >= '20012-05-22 00:00:00' "
									 + "AND photoVoting.votingTime <= '20012-05-22 23:59:59' ) "
									 + "HAVING SUM( photoVoting.mark ) >= '25' "
									 + "ORDER BY SUM( photoVoting.mark ) DESC;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void cloneTest() {
		final SqlSelectQuery selectQuery = getQuery();
		final SqlSelectQuery cloneQuery = selectQuery.cloneQuery();

		String actualValue = selectQuery.build();
		String expectedValue = cloneQuery.build();

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		cloneQuery.addSelect( new SqlColumnSelect( new SqlTable( "table" ), "column" ) );
		actualValue = selectQuery.build();
		expectedValue = cloneQuery.build();

		assertNotSame( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void whereWithJoingColumn() {
		final SqlTable tPhotos = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
		final SqlTable tUsers = new SqlTable( UserDaoImpl.TABLE_USERS );

		final SqlSelectQuery selectQuery = new SqlSelectQuery( tPhotos );

		final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tUsersColID = new SqlColumnSelect( tUsers, UserDaoImpl.ENTITY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColUserId, tUsersColID );
		final SqlJoin join = SqlJoin.inner( tUsers, joinCondition );
		selectQuery.joinTable( join );

		final SqlColumnSelectable tUsersColMemberType = new SqlColumnSelect( tUsers, UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE );
		final SqlLogicallyJoinable condition = new SqlCondition( tUsersColMemberType, SqlCriteriaOperator.EQUALS, 1, dateUtilsService );
		selectQuery.setWhere( condition );

		final String actualValue = selectQuery.build();
		final String expectedValue = "SELECT photos.* FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) WHERE users.membershipType = '1';";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void conditionLike() {
		final SqlTable table = new SqlTable( "photos" );
		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		final SqlColumnSelect column1 = new SqlColumnSelect( table, "userId" );
		final SqlColumnSelect column2 = new SqlColumnSelect( table, "genreId" );

		final int value1 = 777;
		final SqlCondition condition1 = new SqlCondition( column1, SqlCriteriaOperator.LIKE, value1, dateUtilsService );

		sqlBuilderQuery.addSelect( column1, column2 ).setWhere( condition1 );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos WHERE photos.userId LIKE '%777%';";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void fastShape() {
		final SqlTable table = new SqlTable( "photos" );
		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( table );

		final SqlColumnSelect column1 = new SqlColumnSelect( table, "userId" );
		final SqlColumnSelect column2 = new SqlColumnSelect( table, "genreId" );
		final SqlColumnSelect column3 = new SqlColumnSelect( table, "uploadTime" );

		final int value1 = 777;
		final SqlCondition condition1 = new SqlCondition( column1, SqlCriteriaOperator.EQUALS, value1, dateUtilsService );

		sqlBuilderQuery.addSelect( column1, column2 ).setWhere( condition1 ).addSortingAsc( column3 );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.userId, photos.genreId FROM photos AS photos WHERE photos.userId = '777' ORDER BY photos.uploadTime ASC;";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void sqlIdsSelectQuery() {
		final SqlTable table = new SqlTable( "photos" );
		final SqlIdsSelectQuery sqlBuilderQuery = new SqlIdsSelectQuery( table );

		String actualValue = sqlBuilderQuery.build();
		String expectedValue = "SELECT photos.id FROM photos AS photos;";
		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );

		sqlBuilderQuery.setDistinct( true );
		actualValue = sqlBuilderQuery.build();
		expectedValue = "SELECT DISTINCT photos.id FROM photos AS photos;";
		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void sqlIdsSelectQueryWithCustomIdColumn() {
		final SqlTable table = new SqlTable( "photos" );

		final SqlColumnSelect idColumn = new SqlColumnSelect( table, "customIdColumn" );
		final SqlIdsSelectQuery sqlBuilderQuery = new SqlIdsSelectQuery( table, idColumn );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.customIdColumn FROM photos AS photos;";
		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	private SqlSelectQuery getQuery() {
		final Date date = dateUtilsService.parseDate( "20012-05-22" );

		final SqlTable tPhotoVoting = new SqlTable( "photoVoting" );

		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( tPhotoVoting );

		final SqlColumnSelect cPhotoId = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_PHOTO_ID );

		final SqlColumnSelect cMark = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_MARK );
		final SqlColumnSelect cVotingTime = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );
		final SqlColumnAggregate cSumMark = new SqlColumnAggregate( cMark, SqlFunctions.SUM, "summark" );

		final Date firstSecondOfDay = dateUtilsService.getFirstSecondOfDay( date );
		final Date lastSecondOfDay = dateUtilsService.getLastSecondOfDay( date );
		final SqlLogicallyJoinable condition1 = new SqlCondition( cVotingTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, firstSecondOfDay, dateUtilsService );
		final SqlLogicallyJoinable condition2 = new SqlCondition( cVotingTime, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, lastSecondOfDay, dateUtilsService );
		final SqlLogicallyJoinable condition = new SqlLogicallyAnd( condition1, condition2 );

		sqlBuilderQuery.addSelect( cPhotoId, cSumMark );
		sqlBuilderQuery.setWhere( condition );
		sqlBuilderQuery.addSorting( cSumMark, SqlSortOrder.DESC );

		final SqlCondition havingCondition = new SqlCondition( cSumMark, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, 25, dateUtilsService );
		sqlBuilderQuery.setHaving( havingCondition );
		return sqlBuilderQuery;
	}
}
