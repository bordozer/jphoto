package com.bordozer.jphoto.sql.builder;

public class WhereSubQueryNotIn extends WhereSubQueryBase {

    public WhereSubQueryNotIn(final SqlBuildable column, final SqlIdsSelectQuery idsSelectQuery) {
        super(column, idsSelectQuery);
    }

    @Override
    public String getJoinOperator() {
        return "NOT IN";
    }
}
