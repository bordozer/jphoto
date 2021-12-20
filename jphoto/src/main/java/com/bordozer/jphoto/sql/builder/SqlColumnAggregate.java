package com.bordozer.jphoto.sql.builder;

public class SqlColumnAggregate implements SqlColumnSelectable {

    private final SqlColumnSelect column;
    private SqlFunctions functions;
    private String alias;

    public SqlColumnAggregate(final SqlColumnSelect column, final SqlFunctions functions, final String alias) {
        this.column = column;
        this.functions = functions;
        this.alias = alias;
    }

    @Override
    public String build() {
        return String.format("%s( %s ) AS %s", functions, column, alias);
    }

    public SqlColumnSelect getColumn() {
        return column;
    }

    public SqlFunctions getFunctions() {
        return functions;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public int hashCode() {
        int result;
        result = column.hashCode();
        result = 31 * result + functions.hashCode();
        result = 31 * result + alias.hashCode();
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

        if (!(obj instanceof SqlColumnAggregate)) {
            return false;
        }

        final SqlColumnAggregate aggColumn = (SqlColumnAggregate) obj;
        return aggColumn.getColumn().equals(column)
                && aggColumn.getFunctions().equals(functions)
                && aggColumn.getAlias().equals(alias);
    }

    @Override
    public String buildForClause() {
        return String.format("%s( %s )", functions, column);
    }

    @Override
    public String toString() {
        return String.format("%s( %s ) AS %s", functions, column, alias);
    }
}
