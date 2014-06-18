package mocks;

import core.enums.UserGender;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.UserStatus;
import core.services.translator.Language;

public class UserMock extends User {

	public UserMock() {
		this( 111 );
	}

	public UserMock( final int id ) {
		setId( id );
		setName( String.format( "Mock user #%d", id ) );
		setLogin( String.format( "mock_user_%d", id ) );
		setGender( UserGender.MALE );
		setLanguage( Language.EN );
		setUserStatus( UserStatus.MEMBER );
		setMembershipType( UserMembershipType.AUTHOR );
	}
}
