package utils.fakeUser;

import core.enums.UserGender;
import core.general.user.UserMembershipType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FakePhotographer extends AbstractFakeMember {

	private final UserGender[] genders = {UserGender.MALE, UserGender.MALE, UserGender.MALE, UserGender.FEMALE};

	@Override
	public String getName() {
		return getRandomName( "Photograph" );
	}

	@Override
	public UserMembershipType getMembershipType() {
		return UserMembershipType.AUTHOR;
	}

	@Override
	public UserGender getGender() {
		return getRandomGender( genders );
	}

	@Override
	public List<UserMembershipType> getSupportedTeamMemberMembershipType() {
		return newArrayList( UserMembershipType.AUTHOR, UserMembershipType.MODEL, UserMembershipType.MODEL, UserMembershipType.MODEL, UserMembershipType.MODEL, UserMembershipType.MAKEUP_MASTER );
	}

	@Override
	protected String getTeamMemberName( final UserGender gender ) {
		final List<String> names;

		if ( gender == UserGender.MALE ) {
			names = newArrayList( "Jim Phelps", "Denis Saint Clair", "Arni", "John Perri", "Sokolov", "Lobanov", "Lenin", "Salomon", "Macro", "Free Master", "Photo Guru", "Alexander The Great", "Napoleon", "Alf", "David Horton", "Maurizio Melozzi", "Maurizio Moro", "Andrew Piotrowski", "Alfred Weissenegger", "Jim Baab", "Norm Murray", "", "", "", "", "", "", "", "", "", "", "", "" );
		} else {
			names = newArrayList( "Elena Platonova", "Anca Cernoschi", "Elena Vasilyeva", "Cobra", "Sanitarka", "Vedma", "Ada", "PhotoPlenka", "Penka", "Sulico", "Gulchitay", "Pantera", "Agafya", "Norma Desmond", "", "", "", "", "", "", "", "", "", "", "", "" );
		}
		return chooseRandomName( names );
	}
}
