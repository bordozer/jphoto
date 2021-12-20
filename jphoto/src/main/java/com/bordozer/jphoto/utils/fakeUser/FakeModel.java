package com.bordozer.jphoto.utils.fakeUser;

import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.user.UserMembershipType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FakeModel extends AbstractFakeMember {

    private final UserGender[] genders = {
            UserGender.FEMALE, UserGender.FEMALE, UserGender.FEMALE, UserGender.FEMALE, UserGender.MALE
    };

    @Override
    public String getName() {
        return getRandomName("Model");
    }

    @Override
    public UserMembershipType getMembershipType() {
        return UserMembershipType.MODEL;
    }

    @Override
    public UserGender getGender() {
        return getRandomGender(genders);
    }

    @Override
    public List<UserMembershipType> getSupportedTeamMemberMembershipType() {
        return newArrayList(UserMembershipType.AUTHOR, UserMembershipType.AUTHOR, UserMembershipType.AUTHOR, UserMembershipType.AUTHOR, UserMembershipType.MODEL, UserMembershipType.MAKEUP_MASTER);
    }

    @Override
    protected String getTeamMemberName(final UserGender gender) {
        final List<String> names;

        if (gender == UserGender.MALE) {
            names = newArrayList("I Model", "The Men", "Muiscer", "Kalel", "Issash", "Yerver", "Delin", "Skelent", "Swais", "Weroco", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        } else {
            names = newArrayList("Roddyna", "Asada", "Ariannona", "Akara", "Celoa", "Cyelena", "Brana", "Awnia", "Ayne", "Corda", "Dylena", "Dalavesta", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        }

        return chooseRandomName(names);
    }
}
