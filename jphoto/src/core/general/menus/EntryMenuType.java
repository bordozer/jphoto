package core.general.menus;

import utils.StringUtilities;

public enum EntryMenuType {

	PHOTO( 1, "Photo" )
	, COMMENT( 2, "Comment" )
	, USER( 3, "Member" )
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

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( name); // TODO: translate
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
