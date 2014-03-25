package controllers.photos.list;

public enum PhotoFilterSortColumn {

	POSTING_TIME( "1", "Posing time" )
	, COMMENTS_COUNT( "2", "Count of comment" )
	, VIEWS_COUNT( "3", "Count of views" )
	, RATING( "4", "Rating" )
	;

	private final String id;
	private final String name;

	private PhotoFilterSortColumn( final String id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static PhotoFilterSortColumn getById( final String id ) {
		for ( final PhotoFilterSortColumn sortOrder : PhotoFilterSortColumn.values() ) {
			if ( sortOrder.getId().equals( id ) ) {
				return sortOrder;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoFilterSortColumn: %s", id ) );
	}
}