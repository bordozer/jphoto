package com.bordozer.jphoto.sql.builder;

public enum SqlJoinType {
    INNER("INNER JOIN"), FULL_OUTER("LEFT JOIN"), LEFT_OUTER("LEFT OUTER JOIN"), RIGHT_OUTER("RIGHT OUTER JOIN");

    private final String join;

    private SqlJoinType(final String join) {
        this.join = join;
    }

    public String getJoin() {
        return join;
    }
}
