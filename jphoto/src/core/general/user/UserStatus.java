package core.general.user;

import core.interfaces.IdentifiableNameable;

public enum UserStatus implements IdentifiableNameable {

	CANDIDATE( 1, "UserStatus: candidate")
	, MEMBER( 2, "UserStatus: member")
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

	public static UserStatus getById( final int id ) {
		for ( final UserStatus userStatus : UserStatus.values() ) {
			if ( userStatus.getId() == id ) {
				return userStatus;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal id: %s", id ) );
	}
}
