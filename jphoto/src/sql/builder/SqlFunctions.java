package sql.builder;

public enum SqlFunctions {

	SUM( "SUM" ), COUNT( "COUNT" ), AVG( "AVG" ), MIN( "MIN" ), MAX( "MAX" );

	private String function;

	SqlFunctions( final String function ) {
		this.function = function;
	}

	public String getFunction() {
		return function;
	}
}
