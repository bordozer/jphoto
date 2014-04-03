package ui.controllers.photos.list;

import sql.builder.SqlSortOrder;

public enum PhotoFilterSortOrder {

	ASC( "1", "Ascending", SqlSortOrder.ASC )
	, DESC( "2", "Descending", SqlSortOrder.DESC )
	;

	private final String id;
	private final String name;
	private final SqlSortOrder sortOrder;

	private PhotoFilterSortOrder( final String id, final String name, final SqlSortOrder sortOrder ) {
		this.id = id;
		this.name = name;
		this.sortOrder = sortOrder;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public SqlSortOrder getSortOrder() {
		return sortOrder;
	}

	public static PhotoFilterSortOrder getById( final String id ) {
		for ( final PhotoFilterSortOrder sortOrder : PhotoFilterSortOrder.values() ) {
			if ( sortOrder.getId().equals( id ) ) {
				return sortOrder;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoFilterSortOrder: %s", id ) );
	}
}
