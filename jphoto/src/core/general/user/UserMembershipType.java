package core.general.user;

import com.google.common.collect.Lists;
import core.interfaces.IdentifiableNameable;

import java.util.List;

public enum UserMembershipType implements IdentifiableNameable {

	AUTHOR( 1, "UserMembershipType: author", "UserMembershipType: author plural", "author.png" )
	, MODEL( 2, "UserMembershipType: model", "UserMembershipType: model plural", "model.png" )
	, MAKEUP_MASTER( 3, "UserMembershipType: makeup master", "UserMembershipType: makeup master plural", "makeup.png" )
	;
	private final int id;
	private final String name;
	private final String namePlural;
	private final String icon;

	UserMembershipType( int id, String name, String namePlural, final String icon ) {
		this.id = id;
		this.name = name;
		this.namePlural = namePlural;
		this.icon = icon;
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

	public String getIcon() {
		return icon;
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
		return Lists.transform( ids, UserMembershipType::getById );
		/*return Lists.transform( ids, new Function<Integer, UserMembershipType>() {
			@Override
			public UserMembershipType apply( final Integer membershipTypeId ) {
				return UserMembershipType.getById( membershipTypeId );
			}
		} );*/
	}
}
