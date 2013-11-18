package common;

import core.context.ApplicationContextHelper;
import core.general.user.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith( JUnit4ClassRunner.class )
@ContextConfiguration( locations = {"file:springConfigs/tests-context.xml"} )
public class AbstractTestCase {

	@Before
	public void setup() {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext( "file:springConfigs/tests-context.xml" );
		ApplicationContextHelper.setSpringContext( applicationContext );
	}
	public final static User SUPER_MEGA_ADMIN = new User(  ) {
		@Override
		public int getId() {
			return 666;
		}

		@Override
		public String getName() {
			return "Super mega admin";
		}
	};
	public final static User SUPER_ADMIN = new User(  ) {
		@Override
		public int getId() {
			return 777;
		}

		@Override
		public String getName() {
			return "Super admin";
		}
	};

	public static final User TEST_USER = new User() {
		@Override
		public String getName() {
			return "Test user";
		}

		@Override
		public int getId() {
			return 1111;
		}
	};
}
