package sql.builder;

public abstract class SqlColumn implements SqlBuildable {

	private final SqlTable table;
	private final String name;

	public SqlColumn( final SqlTable table, final String name ) {
		this.table = table;
		this.name = name;
	}

	public SqlTable getTable() {
		return table;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		int result;
		result = table.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj.getClass().equals( this.getClass() ) ) ) {
			return false;
		}

		final SqlColumn column = ( SqlColumn ) obj;
		return column.getTable().equals( table ) && column.getName().equals( name );
	}

	@Override
	public String toString() {
		return String.format( "%s.%s", table.getName(), name );
	}
}
