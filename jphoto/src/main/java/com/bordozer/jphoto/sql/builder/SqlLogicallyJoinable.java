package com.bordozer.jphoto.sql.builder;

public interface SqlLogicallyJoinable {

    String join();

    String getJoinOperator();
}
