package core.general.user;

import utils.StringUtilities;

public enum UserStatus {
	CANDIDATE( 1, "candidate")
	, MEMBER( 2, "member")
	;

	private final int id;
	private final String name;

	private UserStatus( final int id, final String name ) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( name ); // TODO: translate
	}

	public static UserStatus getById( final int id ) {
		for ( final UserStatus userStatus : UserStatus.values() ) {
			if ( userStatus.getId() == id ) {
				return userStatus;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal id: %s", id ) );
	}
}
