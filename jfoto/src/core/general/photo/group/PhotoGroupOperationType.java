package core.general.photo.group;

import utils.TranslatorUtils;

public enum PhotoGroupOperationType {
	ARRANGE_PHOTO_ALBUMS( 1, "Arrange photo albums" )
	, ARRANGE_TEAM_MEMBERS( 4, "Arrange team members" )
	, DELETE_PHOTOS( 3, "Delete photos" )
	, ARRANGE_NUDE_CONTENT( 5, "Set nude content" )
	, MOVE_TO_GENRE( 6, "Move to category" )
	, SEPARATOR( -1, "- - - - - - - - - - - - - - - - - - - - - - -" )
	;

	private final int id;
	private final String name;

	private PhotoGroupOperationType( final int id, final String name ) {
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
		return TranslatorUtils.translate( name );
	}

	public static PhotoGroupOperationType getById( final int id ) {
		for ( final PhotoGroupOperationType groupOperationType : PhotoGroupOperationType.values() ) {
			if ( id != -1 && groupOperationType.getId() == id ) {
				return groupOperationType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoGroupOperation id: %d", id ) );
	}
}
