package core.enums;

import core.interfaces.IdentifiableNameable;

public enum UserTeamMemberType implements IdentifiableNameable {

	MODEL( 1, "UserTeamMemberType: Model", "model.png" )
	, PHOTOGRAPH( 2, "UserTeamMemberType: Photograph", "photo.png" )
	, MAKEUP_MASTER( 3, "UserTeamMemberType: Makeup master", "makeup.png" )
	, HAIR_DRESSER( 4, "UserTeamMemberType: Hair dresser", "hair.png" )
	;

	private final int id;
	private final String name;
	private final String icon;

	private UserTeamMemberType( final int id, final String name, final String icon ) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public static UserTeamMemberType getById( final int id ) {
		for ( final UserTeamMemberType upgradeTaskResult : UserTeamMemberType.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal UserTeamMemberType id: %d", id ) );
	}
}
