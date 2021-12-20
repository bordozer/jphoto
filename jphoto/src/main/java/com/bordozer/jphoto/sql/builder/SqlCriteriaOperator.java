package com.bordozer.jphoto.sql.builder;

import java.util.EnumSet;

public enum SqlCriteriaOperator {

    EQUALS("="), NOT_EQUALS("<>"), GRATER_THEN(">"), GREATER_THAN_OR_EQUAL_TO(">="), LESS_THEN("<"), LESS_THAN_OR_EQUAL_TO("<="), LIKE("LIKE"), LIKE_BEGINS("LIKE"), LIKE_ENDS("LIKE");

    private static EnumSet LIKE_CRITERIAS = EnumSet.of(LIKE, LIKE_BEGINS, LIKE_ENDS);

    private String operator;

    SqlCriteriaOperator(final String operator) {
        this.operator = operator;
    }

    public String op() {
        return operator;
    }

    public boolean isLikeCriteria() {
        return LIKE_CRITERIAS.contains(this);
    }
}
