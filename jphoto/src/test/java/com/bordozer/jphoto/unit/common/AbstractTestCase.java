package com.bordozer.jphoto.unit.common;

import com.bordozer.jphoto.admin.controllers.translator.custom.TranslationEntryType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.dao.TranslationsDao;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.NerdKey;
import com.bordozer.jphoto.core.services.translator.TranslationData;
import com.bordozer.jphoto.core.services.translator.Translator;
import com.bordozer.jphoto.core.services.translator.TranslatorServiceImpl;
import com.bordozer.jphoto.core.services.utils.DateUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.SystemFilePathUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.sql.BaseSqlUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.sql.PhotoQueryServiceImpl;
import com.google.common.collect.Maps;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractTestCase {

    public static final Language MENU_LANGUAGE = Language.EN;

    protected static final String EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT = "Expected and actual results are different";
    public static final String TRANSLATED_ENTRY = "Translated entry";

    protected final DateUtilsServiceImpl dateUtilsService;
    protected final TranslatorServiceImpl translatorService;
    protected final UrlUtilsServiceImpl urlUtilsService;
    protected final UserPhotoFilePathUtilsServiceImpl userPhotoFilePathUtilsService;
    protected final EntityLinkUtilsServiceImpl entityLinkUtilsService;
    protected final BaseSqlUtilsServiceImpl baseSqlUtilsService;
    protected final PhotoQueryServiceImpl photoSqlHelperService;

    public AbstractTestCase() {


        translatorService = new TranslatorServiceImpl();
        translatorService.setTranslator(new Translator(Maps.<NerdKey, TranslationData>newHashMap()));
        translatorService.setTranslationsDao(getTranslationsDao());

        baseSqlUtilsService = new BaseSqlUtilsServiceImpl();

        // dateUtilsService -->
        dateUtilsService = new DateUtilsServiceImpl();
        // dateUtilsService <--

        // photoSqlHelperService -->
        photoSqlHelperService = new PhotoQueryServiceImpl();
        photoSqlHelperService.setDateUtilsService(dateUtilsService);
        photoSqlHelperService.setBaseSqlUtilsService(baseSqlUtilsService);
        // photoSqlHelperService <--

        // urlUtilsService -->
        urlUtilsService = new UrlUtilsServiceImpl();
        urlUtilsService.setDateUtilsService(dateUtilsService);
        // urlUtilsService <--

        // userPhotoFilePathUtilsService -->
        userPhotoFilePathUtilsService = new UserPhotoFilePathUtilsServiceImpl();

        userPhotoFilePathUtilsService.setDateUtilsService(dateUtilsService);

        final SystemFilePathUtilsServiceImpl systemFilePathUtilsService = new SystemFilePathUtilsServiceImpl();
        userPhotoFilePathUtilsService.setSystemFilePathUtilsService(systemFilePathUtilsService);

        userPhotoFilePathUtilsService.setUrlUtilsService(urlUtilsService);
        // userPhotoFilePathUtilsService <--

        // entityLinkUtilsService -->
        entityLinkUtilsService = new EntityLinkUtilsServiceImpl();

        entityLinkUtilsService.setDateUtilsService(dateUtilsService);
        entityLinkUtilsService.setUrlUtilsService(urlUtilsService);
        entityLinkUtilsService.setTranslatorService(translatorService);
        // entityLinkUtilsService <--
    }

    @Before
    public void setup() {
    }

    private TranslationsDao getTranslationsDao() {

        final TranslationsDao translationsDao = EasyMock.createMock(TranslationsDao.class);

        EasyMock.expect(translationsDao.translateCustom(EasyMock.<TranslationEntryType>anyObject(), EasyMock.anyInt(), EasyMock.<Language>anyObject())).andReturn(TRANSLATED_ENTRY).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(translationsDao);

        return translationsDao;
    }

    protected ServicesImpl getServices() {
        final ServicesImpl services = new ServicesImpl();

        services.setTranslatorService(translatorService);
        services.setDateUtilsService(dateUtilsService);
        services.setEntityLinkUtilsService(entityLinkUtilsService);

        return services;
    }

    public static String translated(final String nerd) {
        return nerd;
    }

    public final static User SUPER_ADMIN_1 = new User() {
        @Override
        public int getId() {
            return 666;
        }

        @Override
        public String getName() {
            return "Super mega com.bordozer.jphoto.admin";
        }
    };

    public final static User SUPER_ADMIN_2 = new User() {
        @Override
        public int getId() {
            return 777;
        }

        @Override
        public String getName() {
            return "Super com.bordozer.jphoto.admin";
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

    protected boolean isSuperAdmin(final User user) {
        return user == SUPER_ADMIN_1 || user == SUPER_ADMIN_2;
    }
}
