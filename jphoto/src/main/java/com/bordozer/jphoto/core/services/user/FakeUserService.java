package com.bordozer.jphoto.core.services.user;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeam;
import com.bordozer.jphoto.utils.fakeUser.AbstractFakeMember;

public interface FakeUserService {

    User getRandomUser();

    User getRandomUser(final AbstractFakeMember fakeMember);

    AbstractFakeMember getRandomFakeMember();

    AbstractFakeMember getRandomFakeMember(final UserMembershipType membershipType);

    UserTeam getRandomUserTeam(final User user);

    UserTeam getRandomUserTeam(final User user, final AbstractFakeMember fakeMember);
}
