package sql.builder;

public enum SqlSortOrder {

	ASC( "ASC" ), DESC( "DESC" );

	private String sort;

	SqlSortOrder( final String sort ) {
		this.sort = sort;
	}

	public String getSort() {
		return sort;
	}

	public static SqlSortOrder getSortOrder( final String sort ) {
		for ( SqlSortOrder sortOrder : SqlSortOrder.values() ) {
			if ( sortOrder.getSort().equals( sort ) ) {
				return sortOrder;
			}
		}

		return null;
	}

}
