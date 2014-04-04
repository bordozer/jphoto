package core.general.user;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public enum UserMembershipType {

	AUTHOR( 1, "UserMembershipType: author", "UserMembershipType: author plural" )
	, MODEL( 2, "UserMembershipType: model", "UserMembershipType: model plural" )
	, MAKEUP_MASTER( 3, "UserMembershipType: makeup master", "UserMembershipType: makeup master plural" )
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

	public static List<UserMembershipType> getByIds( final List<Integer> ids ) {
		return Lists.transform( ids, new Function<Integer, UserMembershipType>() {
			@Override
			public UserMembershipType apply( final Integer membershipTypeId ) {
				return UserMembershipType.getById( membershipTypeId );
			}
		} );
	}
}
