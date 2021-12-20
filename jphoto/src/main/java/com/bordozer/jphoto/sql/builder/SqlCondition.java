package com.bordozer.jphoto.sql.builder;

import com.bordozer.jphoto.core.services.utils.DateUtilsService;

import java.util.Date;

public class SqlCondition implements SqlLogicallyJoinable {

    private final SqlColumnSelectable column;

    private final SqlCriteriaOperator operator;
    private final Object value;

    private final DateUtilsService dateUtilsService;

    public SqlCondition(final SqlColumnSelectable column, final SqlCriteriaOperator operator, final Object value, final DateUtilsService dateUtilsService) {
        this.column = column;
        this.operator = operator;
        this.value = value;
        this.dateUtilsService = dateUtilsService;
    }

    public SqlColumnSelectable getColumn() {
        return column;
    }

    public SqlCriteriaOperator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String join() {
        Object val = value;

        if (val instanceof Date) {
            val = dateUtilsService.formatDateTime((Date) value);
        }

        if (operator.isLikeCriteria()) {
            switch (operator) {
                case LIKE:
                    return String.format("%s %s '%%%s%%'", column.buildForClause(), getJoinOperator(), val);
                case LIKE_BEGINS:
                    return String.format("%s %s '%%%s'", column.buildForClause(), getJoinOperator(), val);
                case LIKE_ENDS:
                    return String.format("%s %s '%s%%'", column.buildForClause(), getJoinOperator(), val);
                default:
                    throw new IllegalArgumentException(String.format("Unsupported LIKE operator: %s", operator));
            }
        }

        return String.format("%s %s '%s'", column.buildForClause(), getJoinOperator(), val);
    }

    @Override
    public String getJoinOperator() {
        return operator.op();
    }

    @Override
    public int hashCode() {
        int result;
        result = column.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj.getClass().equals(this.getClass()))) {
            return false;
        }

        final SqlCondition condition = (SqlCondition) obj;
        return condition.getColumn().equals(column) && condition.getOperator().equals(operator) && condition.getValue().equals(value);
    }

    @Override
    public String toString() {
        return String.format("%s %s '%s'", column, operator.op(), value);
    }
}
