package sql.builder;

public class WhereSubQueryIn extends WhereSubQueryBase {

	public WhereSubQueryIn( final SqlBuildable column, final SqlIdsSelectQuery idsSelectQuery ) {
		super( column, idsSelectQuery );
	}

	@Override
	public String getJoinOperator() {
		return "IN";
	}
}
