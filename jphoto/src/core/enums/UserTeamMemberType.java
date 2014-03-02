package core.enums;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum UserTeamMemberType {

	MODEL( 1, "Model", "model.png" )
	, PHOTOGRAPH( 2, "Photograph", "photo.png" )
	, MAKEUP_MASTER( 3, "Makeup master", "makeup.png" )
	, HAIR_DRESSER( 4, "Hair dresser", "hair.png" )
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

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( name ); // TODO: translate
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
