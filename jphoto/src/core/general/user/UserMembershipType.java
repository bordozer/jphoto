package core.general.user;

public enum UserMembershipType {

	AUTHOR( 1, "author", "author plural" )
	, MODEL( 2, "model", "model plural" )
	, MAKEUP_MASTER( 3, "makeup master", "makeup master plural" )
	;
	private final int id;
	private final String name;
	private final String namePlural;

	UserMembershipType( int id, String name, String namePlural ) {
		this.id = id;
		this.name = name;
		this.namePlural = namePlural;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNamePlural() {
		return namePlural;
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
