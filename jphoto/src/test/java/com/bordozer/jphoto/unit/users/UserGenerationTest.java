package users;

import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.user.FakeUserService;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import com.bordozer.jphoto.utils.fakeUser.NameGenerator;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class UserGenerationTest extends AbstractTestCase {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void tempUserNameTest() {
        final FakeUserService fakeUserService = EasyMock.createMock(FakeUserService.class);
        EasyMock.expect(fakeUserService.getRandomUser()).andReturn(new User()).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(fakeUserService);
    }

    @Test
    public void nameGeneratorTest() {
        final NameGenerator generator = new NameGenerator();
        final String name = generator.getName(UserGender.MALE);
    }
}
