package core.general.user;

public enum UserMembershipType {

	AUTHOR( 1, "author", "authors" )
	, MODEL( 2, "model", "models" )
	, MAKEUP_MASTER( 3, "makeup master", "makeup masters" )
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
		return name; // TODO: translate
	}

	public String getNames() {
		return names; // TODO: translate
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
