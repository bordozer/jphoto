package com.bordozer.jphoto.sql.builder;

public class SqlColumnHaving extends SqlColumn {

    public SqlColumnHaving(final SqlTable table, final String name) {
        super(table, name);
    }

    public SqlColumnHaving(final SqlColumn column) {
        this(column.getTable(), column.getName());
    }

    @Override
    public String build() {
        return "TODO";
    }
}
