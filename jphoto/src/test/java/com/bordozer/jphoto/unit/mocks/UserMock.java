package mocks;

import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserMock extends User {

    public UserMock() {
        this(111);
    }

    public UserMock(final int id) {
        setId(id);
        setName(String.format("Mock user #%d", id));
        setLogin(String.format("mock_user_%d", id));
        setGender(UserGender.MALE);
        setLanguage(Language.EN);
        setUserStatus(UserStatus.MEMBER);
        setMembershipType(UserMembershipType.AUTHOR);
    }
}
