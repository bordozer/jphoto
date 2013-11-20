package sql.builder;

public class SqlColumnSort implements SqlBuildable {

	private final SqlColumnSelectable column;
	private SqlSortOrder sortOrder;

	public SqlColumnSort( final SqlColumnSelect column ) {
		this.column = column;
		this.sortOrder = SqlSortOrder.ASC;
	}

	public SqlColumnSort( final SqlColumnSelectable column, final SqlSortOrder sortOrder ) {
		this.column = column;
		this.sortOrder = sortOrder;
	}

	@Override
	public String build() {
		return String.format( "%s %s", column.buildForClause(), sortOrder.getSort() );
	}

	public SqlColumnSelectable getColumn() {
		return column;
	}

	public SqlSortOrder getSortOrder() {
		return sortOrder;
	}

	@Override
	public int hashCode() {
		int result;
		result = column.hashCode();
		result = 31 * result + sortOrder.hashCode();
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

		if ( !( obj instanceof SqlColumnSort ) ) {
			return false;
		}

		final SqlColumnSort columnSort = ( SqlColumnSort ) obj;
		return columnSort.getColumn().equals( column )
			   && columnSort.getSortOrder().equals( sortOrder );
	}

	@Override
	public String toString() {
		return String.format( "%s %s", column, sortOrder.getSort() );
	}
}
