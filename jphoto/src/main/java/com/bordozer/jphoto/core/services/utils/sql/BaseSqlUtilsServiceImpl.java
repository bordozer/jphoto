package com.bordozer.jphoto.core.services.utils.sql;

import com.bordozer.jphoto.core.services.dao.GenreDaoImpl;
import com.bordozer.jphoto.core.services.dao.UserDaoImpl;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.sql.builder.BaseSqlSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlColumnSelectable;
import com.bordozer.jphoto.sql.builder.SqlCondition;
import com.bordozer.jphoto.sql.builder.SqlCriteriaOperator;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlTable;
import com.bordozer.jphoto.utils.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("baseSqlUtilsService")
public class BaseSqlUtilsServiceImpl implements BaseSqlUtilsService {

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public void initLimitAndOffset(final BaseSqlSelectQuery sqlSelectQuery, final int page, final int itemsOnPage) {
        final int firstElementIndex = PagingUtils.getPageItemStartIndex(page, itemsOnPage);
        sqlSelectQuery.setLimit(itemsOnPage);
        sqlSelectQuery.setOffset(firstElementIndex);
    }

    @Override
    public SqlIdsSelectQuery getUsersIdsSQL() {
        return new SqlIdsSelectQuery(new SqlTable(UserDaoImpl.TABLE_USERS));
    }

    @Override
    public SqlIdsSelectQuery getGenresIdsSQL() {
        return new SqlIdsSelectQuery(new SqlTable(GenreDaoImpl.TABLE_GENRES));
    }

    @Override
    public void addUserSortByName(final BaseSqlSelectQuery selectQuery) {
        final SqlTable tUser = new SqlTable(UserDaoImpl.TABLE_USERS);
        final SqlColumnSelectable sortColumn = new SqlColumnSelect(tUser, UserDaoImpl.TABLE_COLUMN_NAME);
        selectQuery.addSortingAsc(sortColumn);
    }

    private SqlCondition getCondition(final String tableName, final String columnName, final long value, final SqlCriteriaOperator operator) {
        final SqlTable table = new SqlTable(tableName);
        final SqlColumnSelectable column = new SqlColumnSelect(table, columnName);

        return new SqlCondition(column, operator, value, dateUtilsService);
    }
}
