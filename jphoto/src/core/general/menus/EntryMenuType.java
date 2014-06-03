package core.general.menus;

public enum EntryMenuType {

	PHOTO( 1, "EntryMenuType: Photo" )
	, COMMENT( 2, "EntryMenuType: Comment" )
	, USER( 3, "EntryMenuType: Member" )
	;

	private final int id;
	private final String name;

	private EntryMenuType( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static EntryMenuType getById( final int id ) {
		for ( final EntryMenuType entryMenuType : EntryMenuType.values() ) {
			if ( entryMenuType.getId() == id ) {
				return entryMenuType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal EntryMenuType id: %d", id ) );
	}
}
