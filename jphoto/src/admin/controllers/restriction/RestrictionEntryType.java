package admin.controllers.restriction;

public enum RestrictionEntryType {

	USER( 1 )
	, PHOTO( 2 )
	;

	private final int id;

	RestrictionEntryType( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
