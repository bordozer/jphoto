package core.services.user;

import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userTeam.UserTeam;
import utils.fakeUser.AbstractFakeMember;

public interface FakeUserService {

	User getRandomUser();

	User getRandomUser( final AbstractFakeMember fakeMember );

	AbstractFakeMember getRandomFakeMember();

	AbstractFakeMember getRandomFakeMember( final UserMembershipType membershipType );

	UserTeam getRandomUserTeam( final User user );

	UserTeam getRandomUserTeam( final User user, final AbstractFakeMember fakeMember );
}
