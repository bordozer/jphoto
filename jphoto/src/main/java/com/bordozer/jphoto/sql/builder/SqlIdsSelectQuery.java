package com.bordozer.jphoto.sql.builder;

import com.bordozer.jphoto.core.services.dao.BaseEntityDao;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SqlIdsSelectQuery extends BaseSqlSelectQuery {

    private final SqlBuildable idColumn;

    public SqlIdsSelectQuery(final SqlTable mainTable) {
        super(mainTable);
        idColumn = new SqlColumnSelect(mainTable, BaseEntityDao.ENTITY_ID);
    }

    public SqlIdsSelectQuery(final SqlTable mainTable, final SqlColumnSelect idColumn) {
        super(mainTable);
        this.idColumn = idColumn;
    }

    protected void buildSelectColumns(final StringBuilder builder) {
        builder.append(idColumn.build());
    }

    @Override
    public SqlSelectQuery cloneQuery() {
        final SqlSelectQuery selectQuery = super.cloneQuery();
        final List<SqlBuildable> columns = newArrayList(idColumn);
        selectQuery.setSelectColumns(columns);
        return selectQuery;
    }
}
