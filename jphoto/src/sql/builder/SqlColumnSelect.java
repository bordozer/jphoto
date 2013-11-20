package sql.builder;

public class SqlColumnSelect extends SqlColumn implements SqlColumnSelectable {

	public SqlColumnSelect( final SqlTable table, final String name ) {
		super( table, name );
	}

	@Override
	public String toString() {
		return String.format( "%s.%s", getTable().getName(), getName() );
	}

	@Override
	public String build() {
		return String.format( "%s.%s", getTable().getName(), getName() );
	}

	@Override
	public String buildForClause() {
		return String.format( "%s.%s", getTable().getName(), getName() );
	}
}
