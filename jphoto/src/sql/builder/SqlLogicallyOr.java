package sql.builder;

public class SqlLogicallyOr extends SqlConditionList {

	public SqlLogicallyOr( final SqlLogicallyJoinable... conditions ) {
		super( conditions );
	}

	@Override
	public String getJoinOperator() {
		return SqlLogicalOperator.OR.op();
	}
}
