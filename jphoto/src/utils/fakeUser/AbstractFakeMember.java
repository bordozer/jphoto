package utils.fakeUser;

import core.enums.UserGender;
import core.enums.UserTeamMemberType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userTeam.UserTeamMember;
import core.services.user.FakeUserService;
import core.services.utils.RandomUtilsService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractFakeMember {

	public abstract String getName();

	public abstract UserMembershipType getMembershipType();

	public abstract UserGender getGender();

	public abstract List<UserMembershipType> getSupportedTeamMemberMembershipType();

	protected RandomUtilsService randomUtilsService;
	protected FakeUserService fakeUserService;

	protected abstract String getTeamMemberName( final UserGender gender );

	public UserGender getRandomGender( UserGender[] genders ) {
		final int lastIndex = genders.length - 1;
		return genders[randomUtilsService.getRandomInt( 0, lastIndex )];
	}

	public UserTeamMember getRandomUserTeamMember( final User user, final List<User> users ) {

		final UserTeamMember userTeamMember = new UserTeamMember();

		userTeamMember.setUser( user );

		final User randomTeamMemberUser = getRandomTeamMemberUser( user, users );
		userTeamMember.setTeamMemberUser( randomTeamMemberUser );

		if ( randomTeamMemberUser == null ) {
			return null;
		}

		final UserMembershipType membershipType = randomTeamMemberUser.getMembershipType();
		final AbstractFakeMember fakeMember = fakeUserService.getRandomFakeMember( membershipType );
		fakeMember.setRandomUtilsService( randomUtilsService );
		fakeMember.setFakeUserService( fakeUserService );

		userTeamMember.setName( fakeMember.getTeamMemberName( fakeMember.getGender() ) );

		final UserTeamMemberType userMembershipType = getCorrespondTeamMemberType( membershipType );
		userTeamMember.setTeamMemberType( userMembershipType );

		return userTeamMember;
	}

	final protected String getRandomName( final String name ) {
		return new NameGenerator().getName( this.getGender() );
	}

	final protected String chooseRandomName( final List<String> names ) {
		return randomUtilsService.getRandomGenericListElement( names );
	}

	final protected UserTeamMemberType getCorrespondTeamMemberType( final UserMembershipType membershipType ) {
		switch ( membershipType ) {
			case AUTHOR:
				return UserTeamMemberType.PHOTOGRAPH;
			case MODEL:
				return UserTeamMemberType.MAKEUP_MASTER;
			case MAKEUP_MASTER:
				return randomUtilsService.getRandomGenericListElement( newArrayList( UserTeamMemberType.MAKEUP_MASTER, UserTeamMemberType.HAIR_DRESSER ) );
		}

		throw new IllegalArgumentException( String.format( "Wrong membershipType: %s", membershipType ) );
	}

	private User getRandomTeamMemberUser( final User user, final List<User> users ) {
		final List<UserMembershipType> teamMemberSupportedMemberships = getSupportedTeamMemberMembershipType();
		final UserMembershipType teamMemberMembershipType = randomUtilsService.getRandomGenericListElement( teamMemberSupportedMemberships );
		return randomUtilsService.getRandomUserButNotThisOne( user, teamMemberMembershipType, users );
	}

	public void setRandomUtilsService( final RandomUtilsService randomUtilsService ) {
		this.randomUtilsService = randomUtilsService;
	}

	public void setFakeUserService( final FakeUserService fakeUserService ) {
		this.fakeUserService = fakeUserService;
	}
}
