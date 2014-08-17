package common;

import admin.controllers.translator.custom.TranslationEntryType;
import com.google.common.collect.Maps;
import core.general.user.User;
import core.services.dao.TranslationsDao;
import core.services.system.ServicesImpl;
import core.services.translator.*;
import core.services.utils.*;
import core.services.utils.sql.BaseSqlUtilsServiceImpl;
import core.services.utils.sql.PhotoCriteriasSqlServiceImpl;
import core.services.utils.sql.PhotoSqlFilterServiceImpl;
import mocks.SystemVarsServiceMock;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractTestCase {

	public static final Language MENU_LANGUAGE = Language.EN;

	protected static final String EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT = "Expected and actual results are different";
	public static final String TRANSLATED_ENTRY = "Translated entry";

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
		translatorService.setTranslator( new Translator( Maps.<NerdKey, TranslationData>newHashMap() ) );
		translatorService.setTranslationsDao( getTranslationsDao() );

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
		entityLinkUtilsService.setTranslatorService( translatorService );
		// entityLinkUtilsService <--
	}

	@Before
	public void setup() {
	}

	private TranslationsDao getTranslationsDao() {

		final TranslationsDao translationsDao = EasyMock.createMock( TranslationsDao.class );

		EasyMock.expect( translationsDao.translateCustom( EasyMock.<TranslationEntryType>anyObject(), EasyMock.anyInt(),EasyMock.<Language>anyObject() ) ).andReturn( TRANSLATED_ENTRY ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( translationsDao );

		return translationsDao;
	}

	protected ServicesImpl getServices() {
		final ServicesImpl services = new ServicesImpl();

		services.setTranslatorService( translatorService );
		services.setDateUtilsService( dateUtilsService );
		services.setEntityLinkUtilsService( entityLinkUtilsService );
		services.setSystemVarsService( systemVarsServiceMock );

		return services;
	}

	public static String translated( final String nerd ) {
		return nerd;
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

	public final static User NOT_LOGGED_USER = new User() {
		@Override
		public int getId() {
			return -1024; // MUST have negative ID
		}

		@Override
		public String getName() {
			return "NOT LOGGED TEST USER";
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

	protected boolean isSuperAdmin( final User user ) {
		return user == SUPER_ADMIN_1 || user == SUPER_ADMIN_2;
	}
}
