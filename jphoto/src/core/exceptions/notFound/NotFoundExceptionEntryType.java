package core.exceptions.notFound;

public enum NotFoundExceptionEntryType {
	USER( "NotFoundExceptionEntryType: Member" )
	, PHOTO( "NotFoundExceptionEntryType: Photo" )
	, GENRE( "NotFoundExceptionEntryType: Photo category" );

	private final String name;

	private NotFoundExceptionEntryType( final String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
