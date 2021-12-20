package com.bordozer.jphoto.sql.builder;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SqlSelectUtils {

    public static String joinWithBrackets(List<SqlLogicallyJoinable> logicallyItems, final String joinOperator) {
        final StringBuilder builder = new StringBuilder();

        builder.append(" (").append(joinWithoutBrackets(logicallyItems, joinOperator)).append(") ");

        return builder.toString().trim();
    }

    public static String joinWithoutBrackets(final List<SqlLogicallyJoinable> logicallyItems, final String joinOperator) {
        final List<String> whereBlock = newArrayList();
        for (final SqlLogicallyJoinable logicallyItem : logicallyItems) {
            whereBlock.add(String.format(" %s ", logicallyItem.join()));
        }
        return StringUtils.join(whereBlock, joinOperator);
    }
}
