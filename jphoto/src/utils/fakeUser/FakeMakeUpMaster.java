package utils.fakeUser;

import core.enums.UserGender;
import core.general.user.UserMembershipType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FakeMakeUpMaster extends AbstractFakeMember {

	private final UserGender[] genders = {
		UserGender.FEMALE, UserGender.FEMALE, UserGender.FEMALE, UserGender.FEMALE, UserGender.FEMALE,
		UserGender.FEMALE, UserGender.MALE
	};

	@Override
	public String getName() {
		return getRandomName( "Makeup Master" );
	}

	@Override
	public UserMembershipType getMembershipType() {
		return UserMembershipType.MAKEUP_MASTER;
	}

	@Override
	public UserGender getGender() {
		return getRandomGender( genders );
	}

	@Override
	public List<UserMembershipType> getSupportedTeamMemberMembershipType() {
		return newArrayList( UserMembershipType.AUTHOR, UserMembershipType.AUTHOR, UserMembershipType.MODEL, UserMembershipType.MODEL, UserMembershipType.MODEL );
	}

	@Override
	protected String getTeamMemberName( final UserGender gender ) {
		final List<String> names;

		if ( gender == UserGender.MALE ) {
			names = newArrayList( "Hildandi", "Dale", "Christoper", "Howard", "Marcelo", "Damion", "Sherwood", "Wilton", "Dudley", "Wilson", "Filiberto", "Wes", "Ricardo", "Chas", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" );
		} else {
			names = newArrayList( "Enoka", "Jessika", "Jezzine", "Kilia", "Kilyne", "Laela", "Lenala", "Lolinda", "Lyna", "Mylene", "Olivia", "Quasee", "Senira", "Tressa", "Treka", "Tonica", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" );
		}

		return chooseRandomName( names );
	}
}
