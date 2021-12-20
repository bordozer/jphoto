package com.bordozer.jphoto.sql.builder;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class SqlJoin implements SqlLogicallyJoinable {

    private final SqlTable joinTable;
    private final List<SqlLogicallyJoinable> joinConditions = newArrayList();

    public abstract SqlJoinType getJoinType();

    protected SqlJoin(final SqlTable joinTable, final SqlJoinCondition joinCondition, final SqlJoinCondition... joinConditions) {
        this.joinTable = joinTable;
        addJoinConditions(joinCondition, joinConditions);
    }

    @Override
    public String join() {
        final String joinColumns = SqlSelectUtils.joinWithoutBrackets(joinConditions, SqlLogicalOperator.AND.op());
        return String.format(" %s %s ON (%s)", getJoinType().getJoin(), joinTable, joinColumns);
    }

    public SqlJoin addJoinConditions(final SqlJoinCondition joinCondition, final SqlJoinCondition... joinConditions) {
        this.joinConditions.add(joinCondition);

        if (joinConditions != null) {
            Collections.addAll(this.joinConditions, joinConditions);
        }

        return this;
    }

    public List<SqlLogicallyJoinable> getJoinConditions() {
        return joinConditions;
    }

    public static SqlJoin inner(final SqlTable joinTable, final SqlJoinCondition joinCondition, final SqlJoinCondition... joinConditions) {
        return new SqlJoin(joinTable, joinCondition, joinConditions) {
            @Override
            public SqlJoinType getJoinType() {
                return SqlJoinType.INNER;
            }

            @Override
            public String getJoinOperator() {
                return SqlJoinType.INNER.getJoin();
            }
        };
    }

    public static SqlJoin leftOuter(final SqlTable joinTable, final SqlJoinCondition joinCondition, final SqlJoinCondition... joinConditions) {
        return new SqlJoin(joinTable, joinCondition, joinConditions) {
            @Override
            public SqlJoinType getJoinType() {
                return SqlJoinType.LEFT_OUTER;
            }

            @Override
            public String getJoinOperator() {
                return SqlJoinType.LEFT_OUTER.getJoin();
            }
        };
    }

    public static SqlJoin rightOuter(final SqlTable joinTable, final SqlJoinCondition joinCondition, final SqlJoinCondition... joinConditions) {
        return new SqlJoin(joinTable, joinCondition, joinConditions) {
            @Override
            public SqlJoinType getJoinType() {
                return SqlJoinType.RIGHT_OUTER;
            }

            @Override
            public String getJoinOperator() {
                return SqlJoinType.RIGHT_OUTER.getJoin();
            }
        };
    }

    public static SqlJoin fullOuter(final SqlTable joinTable, final SqlJoinCondition joinCondition, final SqlJoinCondition... joinConditions) {
        return new SqlJoin(joinTable, joinCondition, joinConditions) {
            @Override
            public SqlJoinType getJoinType() {
                return SqlJoinType.FULL_OUTER;
            }

            @Override
            public String getJoinOperator() {
                return SqlJoinType.FULL_OUTER.getJoin();
            }
        };
    }

    public SqlTable getJoinTable() {
        return joinTable;
    }
}
