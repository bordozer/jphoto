package com.bordozer.jphoto.sql;

import com.bordozer.jphoto.core.services.dao.BaseEntityDao;
import com.bordozer.jphoto.sql.builder.SqlBuildable;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlColumnSelectable;
import com.bordozer.jphoto.sql.builder.SqlCondition;
import com.bordozer.jphoto.sql.builder.SqlCriteriaOperator;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlLogicallyJoinable;
import com.bordozer.jphoto.sql.builder.SqlSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlTable;
import com.bordozer.jphoto.sql.builder.WhereSubQueryIn;
import com.bordozer.jphoto.sql.builder.WhereSubQueryNotIn;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WhereSubQueryTest extends AbstractTestCase {

    private final static String FIELD_ID = BaseEntityDao.ENTITY_ID;

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void whereInSubQuery() {
        final SqlTable tUsers = new SqlTable("users");

        final SqlIdsSelectQuery subQuery = new SqlIdsSelectQuery(tUsers);
        final SqlColumnSelectable tUsersColId = new SqlColumnSelect(tUsers, FIELD_ID);
        final SqlLogicallyJoinable where = new SqlCondition(tUsersColId, SqlCriteriaOperator.EQUALS, 777, dateUtilsService);
        subQuery.addWhereAnd(where);

        final SqlTable tPhotos = new SqlTable("photos");


        final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery(tPhotos);

        final SqlBuildable tPhotoColUserId = new SqlColumnSelect(tPhotos, "userId");
        final SqlLogicallyJoinable whereIn = new WhereSubQueryIn(tPhotoColUserId, subQuery);
        sqlBuilderQuery.addWhereAnd(whereIn);

        final String actualValue = sqlBuilderQuery.build();
        final String expectedValue = "SELECT photos.* FROM photos AS photos WHERE ( photos.userId IN ( SELECT users.id FROM users AS users WHERE ( users.id = '777' ) ) );";

        assertEquals("Actual SQL is wrong", expectedValue, actualValue);
    }

    @Test
    public void whereNotInSubQuery() {
        final SqlTable tUsers = new SqlTable("users");

        final SqlIdsSelectQuery subQuery = new SqlIdsSelectQuery(tUsers);
        final SqlColumnSelectable tUsersColId = new SqlColumnSelect(tUsers, FIELD_ID);
        final SqlLogicallyJoinable where = new SqlCondition(tUsersColId, SqlCriteriaOperator.EQUALS, 777, dateUtilsService);
        subQuery.addWhereAnd(where);

        final SqlTable tPhotos = new SqlTable("photos");


        final SqlSelectQuery sqlBuilderQuery = new SqlSelectQuery(tPhotos);

        final SqlBuildable tPhotoColUserId = new SqlColumnSelect(tPhotos, "userId");
        final SqlLogicallyJoinable whereIn = new WhereSubQueryNotIn(tPhotoColUserId, subQuery);
        sqlBuilderQuery.addWhereAnd(whereIn);

        final String actualValue = sqlBuilderQuery.build();
        final String expectedValue = "SELECT photos.* FROM photos AS photos WHERE ( photos.userId NOT IN ( SELECT users.id FROM users AS users WHERE ( users.id = '777' ) ) );";

        assertEquals("Actual SQL is wrong", expectedValue, actualValue);
    }
}
