package common;

import core.general.user.User;
import core.services.utils.*;
import core.services.utils.sql.BaseSqlUtilsServiceImpl;
import core.services.utils.sql.PhotoCriteriasSqlServiceImpl;
import core.services.utils.sql.PhotoSqlFilterServiceImpl;
import mocks.PhotoServiceMock;
import mocks.SecurityServiceMock;
import mocks.SystemVarsServiceMock;
import org.junit.Before;

public class AbstractTestCase {

	protected final DateUtilsServiceImpl dateUtilsService;
	protected final SystemVarsService systemVarsServiceMock;
	protected final PhotoCriteriasSqlServiceImpl photoCriteriasSqlService;
	protected final UrlUtilsServiceImpl urlUtilsService;
	protected final UserPhotoFilePathUtilsServiceImpl userPhotoFilePathUtilsService;
	protected final EntityLinkUtilsServiceImpl entityLinkUtilsService;
	protected final PhotoSqlFilterServiceImpl photoSqlFilterService;
	protected final BaseSqlUtilsServiceImpl baseSqlUtilsService;

	public AbstractTestCase() {

		systemVarsServiceMock = new SystemVarsServiceMock();

		baseSqlUtilsService = new BaseSqlUtilsServiceImpl();

		// dateUtilsService -->
		dateUtilsService = new DateUtilsServiceImpl();
		dateUtilsService.setSystemVarsService( systemVarsServiceMock );
		// dateUtilsService <--

		// photoSqlFilterService -->
		photoSqlFilterService = new PhotoSqlFilterServiceImpl();
		photoSqlFilterService.setDateUtilsService( dateUtilsService );
		photoSqlFilterService.setBaseSqlUtilsService( baseSqlUtilsService );
		// photoSqlFilterService <--

		// photoCriteriasSqlService -->
		photoCriteriasSqlService = new PhotoCriteriasSqlServiceImpl();
		photoCriteriasSqlService.setDateUtilsService( dateUtilsService );
		photoCriteriasSqlService.setBaseSqlUtilsService( baseSqlUtilsService );
		photoCriteriasSqlService.setPhotoSqlFilterService( photoSqlFilterService );
		// photoCriteriasSqlService <--

		// urlUtilsService -->
		urlUtilsService = new UrlUtilsServiceImpl();
		urlUtilsService.setDateUtilsService( dateUtilsService );
		urlUtilsService.setSystemVarsService( systemVarsServiceMock );
		// urlUtilsService <--

		// userPhotoFilePathUtilsService -->
		userPhotoFilePathUtilsService = new UserPhotoFilePathUtilsServiceImpl();
		userPhotoFilePathUtilsService.setSystemVarsService( systemVarsServiceMock );

		final RandomUtilsServiceImpl randomUtilsService = new RandomUtilsServiceImpl();
		randomUtilsService.setPhotoService( new PhotoServiceMock() );
		randomUtilsService.setSecurityService( new SecurityServiceMock() );
		userPhotoFilePathUtilsService.setRandomUtilsService( randomUtilsService );

		final SystemFilePathUtilsServiceImpl systemFilePathUtilsService = new SystemFilePathUtilsServiceImpl();
		systemFilePathUtilsService.setSystemVarsService( systemVarsServiceMock );
		userPhotoFilePathUtilsService.setSystemFilePathUtilsService( systemFilePathUtilsService );

		userPhotoFilePathUtilsService.setUrlUtilsService( urlUtilsService );
		// userPhotoFilePathUtilsService <--

		// entityLinkUtilsService -->
		entityLinkUtilsService = new EntityLinkUtilsServiceImpl();

		entityLinkUtilsService.setDateUtilsService( dateUtilsService );
		entityLinkUtilsService.setSystemVarsService( systemVarsServiceMock );
		entityLinkUtilsService.setUrlUtilsService( urlUtilsService );
		// entityLinkUtilsService <--
	}

	@Before
	public void setup() {
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
