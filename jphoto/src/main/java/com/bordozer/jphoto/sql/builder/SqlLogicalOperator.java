package com.bordozer.jphoto.sql.builder;

public enum SqlLogicalOperator {

    AND("AND"), OR("OR");

    private String operator;

    SqlLogicalOperator(final String operator) {
        this.operator = operator;
    }

    public String op() {
        return operator;
    }
}
