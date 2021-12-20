package com.bordozer.jphoto.sql.builder;

public class SqlLogicallyAnd extends SqlConditionList {

    public SqlLogicallyAnd(final SqlLogicallyJoinable... conditions) {
        super(conditions);
    }

    @Override
    public String getJoinOperator() {
        return SqlLogicalOperator.AND.op();
    }
}
