package com.bordozer.jphoto.sql.builder;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SqlSelectQuery extends BaseSqlSelectQuery {

    private List<SqlBuildable> selectColumns = newArrayList();

    public SqlSelectQuery(final SqlTable mainTable) {
        super(mainTable);
    }

    public SqlSelectQuery addSelect(final SqlColumnSelectable column, final SqlColumnSelectable... columns) {
        selectColumns.add(column);

        if (columns != null) {
            Collections.addAll(this.selectColumns, columns);
        }

        return this;
    }

    public void resetSelectColumns() {
        selectColumns = newArrayList();
    }

    public void setSelectColumns(final List<SqlBuildable> selectColumns) {
        this.selectColumns = selectColumns;
    }

    protected void buildSelectColumns(final StringBuilder builder) {
        if (selectColumns == null || selectColumns.size() == 0) {
            builder.append(getMainTable()).append(".").append(ALL_COLUMNS);
            return;
        }
        buildCommaSeparatedColumns(builder, selectColumns);
    }

    @Override
    public SqlSelectQuery cloneQuery() {
        final SqlSelectQuery selectQuery = super.cloneQuery();
        selectQuery.setSelectColumns(selectColumns);
        return selectQuery;
    }
}
