package common;

import core.general.user.User;
import core.services.security.ServicesImpl;
import core.services.translator.TranslatorServiceImpl;
import core.services.utils.*;
import core.services.utils.sql.BaseSqlUtilsServiceImpl;
import core.services.utils.sql.PhotoCriteriasSqlServiceImpl;
import core.services.utils.sql.PhotoSqlFilterServiceImpl;
import mocks.SystemVarsServiceMock;
import org.junit.Before;

public class AbstractTestCase {

	public static final String TRANSLATION_SIGN = "(t)";

	protected static final String EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT = "Expected and actual results are different";

	protected final DateUtilsServiceImpl dateUtilsService;
	protected final TranslatorServiceImpl translatorService;
	protected final SystemVarsService systemVarsServiceMock;
	protected final PhotoCriteriasSqlServiceImpl photoCriteriasSqlService;
	protected final UrlUtilsServiceImpl urlUtilsService;
	protected final UserPhotoFilePathUtilsServiceImpl userPhotoFilePathUtilsService;
	protected final EntityLinkUtilsServiceImpl entityLinkUtilsService;
	protected final PhotoSqlFilterServiceImpl photoSqlFilterService;
	protected final BaseSqlUtilsServiceImpl baseSqlUtilsService;

	public AbstractTestCase() {

		systemVarsServiceMock = new SystemVarsServiceMock();

		translatorService = new TranslatorServiceImpl();
		translatorService.setSystemVarsService( systemVarsServiceMock );

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

		userPhotoFilePathUtilsService.setDateUtilsService( dateUtilsService );

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

	protected ServicesImpl getServices() {
		final ServicesImpl services = new ServicesImpl();
		services.setTranslatorService( translatorService );

		return services;
	}

	public static String translated( final String nerd ) {
		return String.format( "%s%s", nerd, TRANSLATION_SIGN );
	}

	public final static User SUPER_ADMIN_1 = new User() {
		@Override
		public int getId() {
			return 666;
		}

		@Override
		public String getName() {
			return "Super mega admin";
		}
	};

	public final static User SUPER_ADMIN_2 = new User() {
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
