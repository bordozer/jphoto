package core.general.menus.comment;

import utils.StringUtilities;

public enum ComplaintReasonType {

	COMMENT_SPAM( 1, "Spam" )
	, COMMENT_SWORD_WORDS( 2, "Sword words" )
	, COMMENT_CUSTOM_COMPLAINT( 3, "Custom complaint" )
	, PHOTO_COPYRIGHT_COMPLAINT( 4, "Photo copyright complaint" )
	;

	private final int id;
	private final String name;

	private ComplaintReasonType( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static ComplaintReasonType getById( final int id ) {
		for ( final ComplaintReasonType complaintReasonType : ComplaintReasonType.values() ) {
			if ( complaintReasonType.getId() == id ) {
				return complaintReasonType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal ComplaintReasonType id: %d", id ) );
	}
}
