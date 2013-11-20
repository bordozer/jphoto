package sql;

import common.AbstractTestCase;
import core.services.dao.BaseEntityDao;
import core.services.utils.DateUtilsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

import static org.junit.Assert.assertEquals;

public class WhereSubQueryTest extends AbstractTestCase {

	private final static String FIELD_ID = BaseEntityDao.ENTITY_ID;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void whereInSubQuery() {
		final SqlTable tUsers = new SqlTable( "users" );

		final SqlIdsSelectQuery subQuery = new SqlIdsSelectQuery( tUsers );
		final SqlColumnSelectable tUsersColId = new SqlColumnSelect( tUsers, FIELD_ID );
		final SqlLogicallyJoinable where = new SqlCondition( tUsersColId, SqlCriteriaOperator.EQUALS, 777, dateUtilsService );
		subQuery.addWhereAnd( where );

		final SqlTable tPhotos = new SqlTable( "photos" );


		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( tPhotos );

		final SqlBuildable tPhotoColUserId = new SqlColumnSelect( tPhotos, "userId" );
		final SqlLogicallyJoinable whereIn = new WhereSubQueryIn( tPhotoColUserId, subQuery );
		sqlBuilderQuery.addWhereAnd( whereIn );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.* FROM photos AS photos WHERE ( photos.userId IN ( SELECT users.id FROM users AS users WHERE ( users.id = '777' ) ) );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}

	@Test
	public void whereNotInSubQuery() {
		final SqlTable tUsers = new SqlTable( "users" );

		final SqlIdsSelectQuery subQuery = new SqlIdsSelectQuery( tUsers );
		final SqlColumnSelectable tUsersColId = new SqlColumnSelect( tUsers, FIELD_ID );
		final SqlLogicallyJoinable where = new SqlCondition( tUsersColId, SqlCriteriaOperator.EQUALS, 777, dateUtilsService );
		subQuery.addWhereAnd( where );

		final SqlTable tPhotos = new SqlTable( "photos" );


		final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery( tPhotos );

		final SqlBuildable tPhotoColUserId = new SqlColumnSelect( tPhotos, "userId" );
		final SqlLogicallyJoinable whereIn = new WhereSubQueryNotIn( tPhotoColUserId, subQuery );
		sqlBuilderQuery.addWhereAnd( whereIn );

		final String actualValue = sqlBuilderQuery.build();
		final String expectedValue = "SELECT photos.* FROM photos AS photos WHERE ( photos.userId NOT IN ( SELECT users.id FROM users AS users WHERE ( users.id = '777' ) ) );";

		assertEquals( "Actual SQL is wrong", expectedValue, actualValue );
	}
}
