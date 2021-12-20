package com.bordozer.jphoto.ui.services.page;

import com.bordozer.jphoto.admin.services.scheduler.ScheduledTasksExecutionService;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UsersSecurity;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.users.login.UserLoginModel;
import com.bordozer.jphoto.ui.elements.MenuItem;
import com.bordozer.jphoto.ui.elements.PageModel;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.ajax.AjaxService;
import com.bordozer.jphoto.ui.services.menu.main.MenuService;
import com.bordozer.jphoto.ui.services.page.icons.TitleIconLoader;
import com.bordozer.jphoto.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.jabsorb.JSONRPCBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@Service("pageTemplateService")
public class PageTemplateServiceImpl implements PageTemplateService {

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private ScheduledTasksExecutionService scheduledTasksExecutionService;

    @Autowired
    private Services services;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private AjaxService ajaxService;

    @Value("${app.projectName}")
    private String projectName;

    @Override
    public String renderPageHeader(final PageModel pageModel) {

        JSONRPCBridge.getGlobalBridge().registerObject("ajaxService", ajaxService);

        final User currentUser = EnvironmentContext.getCurrentUser();
        final Language language = currentUser.getLanguage();

        final PageTitleData dataPage = pageModel.getPageTitleData();

        final String pageTitle = dataPage.getTitle();
        final String pageHeader = dataPage.getHeader();
        final String breadcrumbs = dataPage.getBreadcrumbs();

        final ToolManager manager = new ToolManager(true);
        final Context context = manager.createContext();

        final VelocityContext model = new VelocityContext(context);
        StringWriter writer = new StringWriter();

        model.put("title", pageTitle);
        model.put("pageHeader", pageHeader);

        model.put("baseUrl", urlUtilsService.getBaseURL());
        model.put("imagePath", urlUtilsService.getSiteImagesPath());

        model.put("StringUtils", StringUtils.class);

        model.put("portalpageurl", urlUtilsService.getPortalPageURL());
        model.put("registerurl", urlUtilsService.getUserNewLink());

        model.put("progectname", projectName);
        model.put("slogan", translatorService.translate("System slogan: Post your soul here", language));

        final Map<MenuItem, List<MenuItem>> menuElements = menuService.getMenuElements(currentUser);
        final int menuElementsQty = menuElements.size();
        model.put("menuElements", menuElements);
        model.put("menuElementsQty", menuElementsQty);
        model.put("menuWidth", menuElementsQty * 15);
        model.put("breadcrumbs", breadcrumbs);

        model.put("loginFormAction", urlUtilsService.getUserLoginLink());
        model.put("showLoginForm", pageModel.isShowLoginForm());
        model.put("loginFormLogin", translatorService.translate("System login form: Login", language));
        model.put("rememberMeText", translatorService.translate("System login form: Remember me", language));

        model.put("loginFormPassword", translatorService.translate("System login form: Password", language));
        model.put("defaultDebugPassword", UsersSecurity.DEFAULT_DEBUG_PASSWORD);
        model.put("loginFormLoginControl", UserLoginModel.LOGIN_FORM_LOGIN_CONTROL);
        model.put("loginFormPasswordControl", UserLoginModel.LOGIN_FORM_PASSWORD_CONTROL);
        model.put("loginUserAutomaticallyControl", UserLoginModel.LOGIN_FORM_LOGIN_USER_AUTOMATICALLY_CONTROL);

        model.put("logoutText", translatorService.translate("System login form: Logout", language));

        model.put("loggedUserId", currentUser.getId());
        model.put("loggedUserName", StringUtilities.escapeHtml(currentUser.getName()));
        model.put("loggedUserCardUrl", urlUtilsService.getUserCardLink(currentUser.getId()));
        model.put("loggedUserCardUrlTitle", translatorService.translate("Login info: Your card", language));

        model.put("uploadPhotoText", translatorService.translate("Main menu: Upload photo", language));
        model.put("baseAdminPrefix", urlUtilsService.getBaseAdminURL());

        model.put("titleIcons", TitleIconLoader.getTitleIcons(services, scheduledTasksExecutionService));

        final String hiMessage = EnvironmentContext.getHiMessage();
        if (StringUtils.isNotEmpty(hiMessage)) {
            model.put("hiMessage", StringUtilities.escapeHtml(hiMessage));
            EnvironmentContext.getEnvironment().setHiMessage(StringUtils.EMPTY);
        }

        // TODO: TEMP -->
        addLoginSelectBox(model);
        // TODO: TEMP <--

        final List<Genre> genres = genreService.loadAllSortedByNameForLanguage(language);
        final List<String> genreLinks = newArrayList();
        for (Genre genre : genres) {
            genreLinks.add(entityLinkUtilsService.getPhotosByGenreLink(genre, language));
        }
        model.put("genres", genres);
        model.put("genreLinks", genreLinks);

        model.put("showPhotoCategoriesToolbar", configurationService.getBoolean(ConfigurationKey.SYSTEM_UI_SHOW_PHOTO_CATEGORIES_TOOLBAR));

        //		model.put( "pageHatMaxWidth", EnvironmentContext.getDeviceType() == DeviceType.MOBILE ? "400px" : "950px"  ); // TODO: should be a separate view for mobile devices

        fillUiLanguages(language, model);

        final Template template = velocityEngine.getTemplate("/velocity/pageheader.vm");
        template.merge(model, writer);

        return writer.toString();
    }

    private void addLoginSelectBox(final VelocityContext model) {
        final List<User> users = userService.loadAll();
        final List<UserLogin> userLogins = newArrayList();
        for (final User user : users) {
            final String role = securityService.isSuperAdminUser(user.getId()) ? "SA" : translatorService.translate(user.getUserStatus().getName(), EnvironmentContext.getLanguage()).substring(0, 1);
            userLogins.add(new UserLogin(String.format("%s (%s)", StringUtilities.escapeHtml(StringUtilities.truncateString(user.getName(), 13)), role), user.getLogin()));
        }
        Collections.sort(userLogins, new Comparator<UserLogin>() {
            @Override
            public int compare(final UserLogin o1, final UserLogin o2) {
                return StringUtilities.unescapeHtml(o1.getName()).compareToIgnoreCase(StringUtilities.unescapeHtml(o2.getName()));
            }
        });
        model.put("userLogins", userLogins);
    }

    private void fillUiLanguages(final Language language, final VelocityContext model) {
        // TODO: consider using GenericTranslatableList.languageTranslatableList( systemVarsService.getActiveLanguages(), translatorService )
        final List<LanguageWrapper> uiLanguages = newArrayList();
        for (final Language lang : newArrayList(Language.RU, Language.UA)) {
            final LanguageWrapper wrapper = new LanguageWrapper(lang);
            wrapper.setTitle(translatorService.translate(lang.getName(), lang));
            if (language == lang) {
                wrapper.setStyle("block-shadow");
                wrapper.setTitle(translatorService.translate("$1 - selected UI language", lang, translatorService.translate(lang.getName(), lang)));
            }
            uiLanguages.add(wrapper);
        }
        model.put("uiLanguages", uiLanguages);
    }

    @Override
    public String renderPageFooter() {
        Template template = velocityEngine.getTemplate("/velocity/pagefooter.vm");

        VelocityContext model = new VelocityContext();
        StringWriter writer = new StringWriter();

        //		model.put( "title", pageTitle );

        template.merge(model, writer);

        return writer.toString();
    }
}
