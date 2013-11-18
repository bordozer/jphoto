package core.general.user;

import utils.TranslatorUtils;

public enum UserMembershipType {

	AUTHOR( 1, TranslatorUtils.translate( "author" ), TranslatorUtils.translate( "authors" ) )
	, MODEL( 2, TranslatorUtils.translate( "model" ), TranslatorUtils.translate( "models" ) )
	, MAKEUP_MASTER( 3, TranslatorUtils.translate( "makeup master" ), TranslatorUtils.translate( "makeup masters" ) )
	;
	private final int id;
	private final String name;
	private final String names;

	UserMembershipType( int id, String name, String names ) {
		this.id = id;
		this.name = name;
		this.names = names;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNames() {
		return names;
	}

	public static UserMembershipType getById( String id ) {
		return  getById( Integer.parseInt( id ) );
	}

	public static UserMembershipType getById( int id ) {
		for ( UserMembershipType userMembershipType : UserMembershipType.values() ) {
			if ( userMembershipType.getId() == id ) {
				return userMembershipType;
			}
		}

		return null;
	}
}
