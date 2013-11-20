package sql.builder;

public class SqlJoinCondition implements SqlLogicallyJoinable {

	private final SqlColumnSelect column;
	private final SqlColumnSelect joinedColumn;

	public SqlJoinCondition( final SqlColumnSelect column, final SqlColumnSelect joinedColumn ) {
		this.column = column;
		this.joinedColumn = joinedColumn;
	}

	public SqlColumnSelect getColumn() {
		return column;
	}

	public SqlColumnSelect getJoinedColumn() {
		return joinedColumn;
	}

	@Override
	public String join() {
		return String.format( "%s %s %s", column, getJoinOperator(), joinedColumn );
	}

	@Override
	public String getJoinOperator() {
		return "=";
	}

	@Override
	public int hashCode() {
		int result;
		result = column.hashCode();
		result = 31 * result + joinedColumn.hashCode();
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

		final SqlJoinCondition joinCondition = ( SqlJoinCondition ) obj;
		return joinCondition.getColumn().equals( column )
			   && joinCondition.getJoinedColumn().equals( joinedColumn );
	}

	@Override
	public String toString() {
		return String.format( "Join: %s on %s", column, joinedColumn );
	}
}
