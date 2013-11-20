package sql.builder;

public abstract class WhereSubQueryBase implements SqlLogicallyJoinable {

	private final SqlBuildable column;
	private final SqlIdsSelectQuery idsSelectQuery;

	public WhereSubQueryBase( final SqlBuildable column, final SqlIdsSelectQuery idsSelectQuery ) {
		this.column = column;
		this.idsSelectQuery = idsSelectQuery;
		idsSelectQuery.setFinalSemicolor( false );
	}

	public SqlBuildable getColumn() {
		return column;
	}

	public SqlIdsSelectQuery getIdsSelectQuery() {
		return idsSelectQuery;
	}

	@Override
	public String join() {
		return String.format( "%s %s ( %s )", column.build(), getJoinOperator(), idsSelectQuery.build() );
	}
}
