package core.general.user;

import utils.TranslatorUtils;

public enum UserBookedMembersType {
	MEMBERS( 1, "Members" ), FRIENDS( 2, "Friends" ), BLACKLIST( 3, "Black list" );

	private final int id;
	private final String name;

	UserBookedMembersType( final int id, final String name ) {
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

	public static UserBookedMembersType getById( final int id ) {
		for ( UserBookedMembersType userBookedMembersType : UserBookedMembersType.values() ) {
			if ( userBookedMembersType.getId() == id ) {
				return userBookedMembersType;
			}
		}

		return null;
	}
}
