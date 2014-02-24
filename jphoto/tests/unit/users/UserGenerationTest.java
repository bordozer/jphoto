package users;

import common.AbstractTestCase;
import core.enums.UserGender;
import core.general.user.User;
import core.services.user.FakeUserService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import utils.fakeUser.NameGenerator;

public class UserGenerationTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void tempUserNameTest() {
		final FakeUserService fakeUserService = EasyMock.createMock( FakeUserService.class );
		EasyMock.expect( fakeUserService.getRandomUser() ).andReturn( new User() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( fakeUserService );
	}

	@Test
	public void nameGeneratorTest() {
		final NameGenerator generator = new NameGenerator();
		final String name = generator.getName( UserGender.MALE );
	}
}
