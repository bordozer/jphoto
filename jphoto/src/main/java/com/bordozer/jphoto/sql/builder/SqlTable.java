package com.bordozer.jphoto.sql.builder;

public class SqlTable {

    private final String name;

    public SqlTable(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SqlTable)) {
            return false;
        }

        final SqlTable table = (SqlTable) obj;
        return table.getName().equals(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
