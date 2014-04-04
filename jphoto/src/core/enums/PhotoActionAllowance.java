package core.enums;

import utils.StringUtilities;

public enum PhotoActionAllowance {
	ACTIONS_DENIED( 1, "Not allowed" )
	, CANDIDATES_AND_MEMBERS( 2, "Candidates and members" )
	, MEMBERS_ONLY( 3, "Members only" );

	private final int id;
	private final String name;

	private PhotoActionAllowance( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static PhotoActionAllowance getById( final int id ) {
		for ( final PhotoActionAllowance commentsAllowance : PhotoActionAllowance.values() ) {
			if ( commentsAllowance.getId() == id ) {
				return commentsAllowance;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoCommentsAllowance id: %d", id ) );
	}
}
