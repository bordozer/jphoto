package com.bordozer.jphoto.sql.builder;

import com.bordozer.jphoto.core.log.LogHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class BaseSqlSelectQuery {

    public static final String ALL_COLUMNS = "*";
    public static final String COMMA = ", ";

    private final SqlTable mainTable;
    private List<SqlJoin> joins = newArrayList();

    private boolean isDistinct;

    private SqlLogicallyJoinable whereCondition;
    private List<SqlBuildable> groupColumns = newArrayList();
    private SqlLogicallyJoinable havingColumns;
    private List<SqlBuildable> sortColumns = newArrayList();

    private int offset;
    private int limit;
    private int NO_LIMIT = Integer.MAX_VALUE;

    private boolean finalSemicolor = true;

    public BaseSqlSelectQuery(final SqlTable mainTable) {
        this.mainTable = mainTable;
    }

    protected abstract void buildSelectColumns(final StringBuilder builder);

    public String build() {
        final StringBuilder builder = new StringBuilder("SELECT ");

        if (isDistinct) {
            builder.append("DISTINCT ");
        }

        buildSelectColumns(builder);

        buildFromClause(builder);

        buildWhereClause(builder, whereCondition);

        buildGroupingColumns(builder);

        buildHavingColumns(builder, havingColumns);

        buildSortingColumns(builder);

        buildOffsetAndLimit(builder, offset, limit);

        if (finalSemicolor) {
            builder.append(";");
        }

        final String sql = builder.toString();

        final LogHelper log = new LogHelper();
        //		log.debug( com.bordozer.jphoto.sql );

        return sql;
    }


    private void buildFromClause(final StringBuilder builder) {
        builder.append(" FROM ").append(mainTable.getName()).append(" AS ").append(mainTable.getName());

        if (joins != null) {
            for (final SqlJoin join : joins) {
                builder.append(join.join());
            }
        }
    }

    private void buildWhereClause(final StringBuilder builder, final SqlLogicallyJoinable conditionLists) {
        if (conditionLists == null) {
            return;
        }

        builder.append(" WHERE ");
        builder.append(conditionLists.join());
    }

    private void buildGroupingColumns(final StringBuilder builder) {
        if (groupColumns == null || groupColumns.size() == 0) {
            return;
        }
        builder.append(" GROUP BY ");
        buildCommaSeparatedColumns(builder, groupColumns);
    }

    private void buildHavingColumns(final StringBuilder builder, final SqlLogicallyJoinable conditionLists) {
        if (havingColumns == null) {
            return;
        }
        builder.append(" HAVING ");
        builder.append(conditionLists.join());
    }

    private void buildSortingColumns(final StringBuilder builder) {
        if (sortColumns == null || sortColumns.size() == 0) {
            return;
        }
        builder.append(" ORDER BY ");

        buildCommaSeparatedColumns(builder, sortColumns);
    }

    private void buildOffsetAndLimit(final StringBuilder builder, final int offset, final int limit) {
        if (limit > 0 && limit < NO_LIMIT) {
            builder.append(" LIMIT ").append(limit);
        }

        if (offset > 0) {
            if (limit == 0) {
                builder.append(" LIMIT ").append(NO_LIMIT);
            }
            builder.append(" OFFSET ").append(offset);
        }
    }

    protected void buildCommaSeparatedColumns(final StringBuilder builder, final List<SqlBuildable> columns) {
        final List<String> selects = newArrayList();
        for (final SqlBuildable column : columns) {
            selects.add(column.build());
        }
        builder.append(StringUtils.join(selects, COMMA));
    }

    public SqlTable getMainTable() {
        return mainTable;
    }

    public boolean isDistinct() {
        return isDistinct;
    }

    public void setDistinct(final boolean distinct) {
        isDistinct = distinct;
    }

    public BaseSqlSelectQuery joinTable(final SqlJoin join) {
        joins.add(join);
        return this;
    }

    public BaseSqlSelectQuery setWhere(final SqlLogicallyJoinable logicallyConditions) {
        whereCondition = logicallyConditions;
        return this;
    }

    public BaseSqlSelectQuery addWhereAnd(final SqlLogicallyJoinable condition) {
        if (whereCondition == null) {
            whereCondition = new SqlLogicallyAnd(condition);
            return this;
        }

        whereCondition = new SqlLogicallyAnd(whereCondition, condition);
        return this;
    }

    public BaseSqlSelectQuery addGrouping(final SqlColumnSelect column) {
        groupColumns.add(column);
        return this;
    }

    public BaseSqlSelectQuery setHaving(final SqlLogicallyJoinable logicallyConditions) {
        this.havingColumns = logicallyConditions;
        return this;
    }

    public BaseSqlSelectQuery addSortingAsc(final SqlColumnSelectable column) {
        sortColumns.add(new SqlColumnSort(column, SqlSortOrder.ASC));
        return this;
    }

    public BaseSqlSelectQuery addSortingDesc(final SqlColumnSelectable column) {
        sortColumns.add(new SqlColumnSort(column, SqlSortOrder.DESC));
        return this;
    }

    public BaseSqlSelectQuery addSorting(final SqlColumnSelectable column, final SqlSortOrder sortOrder) {
        sortColumns.add(new SqlColumnSort(column, sortOrder));
        return this;
    }

    public BaseSqlSelectQuery setOffset(final int offset) {
        this.offset = offset;
        return this;
    }

    public BaseSqlSelectQuery setLimit(final int limit) {
        this.limit = limit;
        return this;
    }

    public void resetGrouping() {
        groupColumns = newArrayList();
    }

    public void resetHaving() {
        havingColumns = null;
    }

    public void resetSortCriterias() {
        sortColumns = newArrayList();
    }

    public void resetLimit() {
        limit = NO_LIMIT;
    }

    public void resetOffset() {
        offset = 0;
    }

    public void resetLimitAndOffset() {
        resetLimit();
        resetOffset();
    }

    public void setJoins(final List<SqlJoin> joins) {
        this.joins = joins;
    }

    public void setWhereCondition(final SqlLogicallyJoinable whereCondition) {
        this.whereCondition = whereCondition;
    }

    public void setGroupColumns(final List<SqlBuildable> groupColumns) {
        this.groupColumns = groupColumns;
    }

    public void setHavingColumns(final SqlLogicallyJoinable havingColumns) {
        this.havingColumns = havingColumns;
    }

    public void setSortColumns(final List<SqlBuildable> sortColumns) {
        this.sortColumns = sortColumns;
    }

    public boolean isFinalSemicolor() {
        return finalSemicolor;
    }

    public void setFinalSemicolor(final boolean finalSemicolor) {
        this.finalSemicolor = finalSemicolor;
    }

    public List<SqlJoin> getJoins() {
        return joins;
    }

    public SqlSelectQuery cloneQuery() {
        final SqlSelectQuery selectQuery = new SqlSelectQuery(mainTable);

        selectQuery.setJoins(joins);
        selectQuery.setWhereCondition(whereCondition);
        selectQuery.setGroupColumns(groupColumns);
        selectQuery.setHavingColumns(havingColumns);
        selectQuery.setSortColumns(sortColumns);

        selectQuery.setLimit(limit);
        selectQuery.setOffset(offset);

        return selectQuery;
    }

    @Override
    public String toString() {
        return build();
    }
}
