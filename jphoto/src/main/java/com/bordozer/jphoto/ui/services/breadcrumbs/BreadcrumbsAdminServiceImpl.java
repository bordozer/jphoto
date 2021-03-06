package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.admin.jobs.enums.JobListTab;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.admin.services.services.UpgradeState;
import com.bordozer.jphoto.core.general.configuration.ConfigurationTab;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import com.bordozer.jphoto.ui.services.menu.main.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("breadcrumbsAdminService")
public class BreadcrumbsAdminServiceImpl implements BreadcrumbsAdminService {

    @Autowired
    private PageTitleUtilsService pageTitleUtilsService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private Services services;

    @Override
    public PageTitleData getJobListBreadcrumbs(final JobListTab jobListTab) {

        final String jobTabName = jobListTab.getName();

        final String title = title(jobTabName).build();
        final String header = header(jobTabName).build();

        final BreadcrumbsBuilder builder = new BreadcrumbsBuilder(services).adminRoot();

        if (jobListTab == JobListTab.TEMPLATES) {
            builder.adminJobsText();
        } else {
            builder.adminJobsLink();
        }

        builder.adminJobsOnTab(jobListTab);

        return new PageTitleData(title, header, builder.build());
    }

    @Override
    public PageTitleData getSavedJobListFilteredByJobTypeBreadcrumbs(final JobListTab jobListTab, final SavedJobType savedJobType) {

        final String jobTabName = jobListTab.getName();

        final String title = title(jobTabName).build();
        final String header = header(jobTabName).build();

        final String breadcrumbs = new BreadcrumbsBuilder(services)
                .adminRoot()
                .adminJobsLink()
                .adminJobsOnTabLink(jobListTab)
                .adminSavedJobType(savedJobType)
                .build();

        return new PageTitleData(title, header, breadcrumbs);
    }

    @Override
    public PageTitleData getJobEditBreadcrumbs(final SavedJob savedJob) {
        final String rootTranslated = getJobsRootTranslated();

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), rootTranslated);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink(EnvironmentContext.getLanguage()), savedJob.getName(), translatorService.translate("Breadcrumbs: Edit", EnvironmentContext.getLanguage()));

        return new PageTitleData(title, rootTranslated, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminConfigurationNewBreadcrumbs() {
        final String tran = translatorService.translate("Breadcrumbs: New system configuration", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), tran);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(EnvironmentContext.getLanguage()), tran);

        return new PageTitleData(title, tran, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminConfigurationInfoBreadcrumbs(final SystemConfiguration systemConfiguration) {
        final String tran = translatorService.translate("Breadcrumbs: Configuration", EnvironmentContext.getLanguage());

        final String sysConfigName = String.format("%s%s%s"
                , systemConfiguration.getName()
                , systemConfiguration.isDefaultConfiguration() ? getDefaultConfigurationSign() : ""
                , systemConfiguration.isActiveConfiguration() ? getConfigurationActiveSign() : ""
        );

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), systemConfiguration.getName());
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(EnvironmentContext.getLanguage()), sysConfigName);

        return new PageTitleData(title, tran, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminConfigurationInfoTbBreadcrumbs(final SystemConfiguration systemConfiguration, final String configTabName) {
        final String tran = translatorService.translate("Breadcrumbs: System configuration Tabs", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), tran, configTabName);
        final String sysConfigName = String.format("%s%s%s"
                , entityLinkUtilsService.getAdminConfigurationTabsLink(systemConfiguration)
                , systemConfiguration.isDefaultConfiguration() ? getDefaultConfigurationSign() : ""
                , systemConfiguration.isActiveConfiguration() ? getConfigurationActiveSign() : ""
        );

        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(EnvironmentContext.getLanguage()), sysConfigName, configTabName);

        return new PageTitleData(title, tran, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminSystemConfigurationListBreadcrumbs() {
        final String tran = translatorService.translate("Breadcrumbs: System configuration", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), tran);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), tran);

        return new PageTitleData(title, tran, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminConfigurationEditTabBreadcrumbs(final int systemConfigurationId, final SystemConfiguration systemConfiguration, final ConfigurationTab configTab) {
        final String configuration = translatorService.translate("Breadcrumbs: Configuration", EnvironmentContext.getLanguage());
        final String edit = translatorService.translate("Breadcrumbs: Edit", EnvironmentContext.getLanguage());

        final String sysConfigName = String.format("%s%s%s"
                , entityLinkUtilsService.getAdminConfigurationTabsLink(systemConfiguration)
                , systemConfiguration.isDefaultConfiguration() ? getDefaultConfigurationSign() : ""
                , systemConfiguration.isActiveConfiguration() ? getConfigurationActiveSign() : ""
        );

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), configuration, edit);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(EnvironmentContext.getLanguage()), sysConfigName, entityLinkUtilsService.getAdminConfigurationLink(systemConfigurationId, configTab, EnvironmentContext.getLanguage()), edit);

        return new PageTitleData(title, configuration, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminConfigurationEditDataBreadcrumbs(final SystemConfiguration systemConfiguration) {
        final String configuration = translatorService.translate("Breadcrumbs: Configuration", EnvironmentContext.getLanguage());
        final String edit = translatorService.translate("Breadcrumbs: Edit", EnvironmentContext.getLanguage());

        final String sysConfigName = String.format("%s%s%s"
                , entityLinkUtilsService.getAdminConfigurationTabsLink(systemConfiguration)
                , systemConfiguration.isDefaultConfiguration() ? getDefaultConfigurationSign() : ""
                , systemConfiguration.isActiveConfiguration() ? getConfigurationActiveSign() : ""
        );

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), configuration, edit);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(EnvironmentContext.getLanguage()), sysConfigName, edit);

        return new PageTitleData(title, configuration, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminJobsBreadcrumbs(final String nerd) {
        final String rootTranslated = getJobsRootTranslated();

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), rootTranslated, nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink(EnvironmentContext.getLanguage()), nerd);

        return new PageTitleData(title, rootTranslated, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminJobsDataTemplateBreadcrumbs(final String nerd) {
        final String rootTranslated = getJobsRootTranslated();

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), rootTranslated, nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink(EnvironmentContext.getLanguage()), nerd, translatorService.translate("Breadcrumbs: Job template", EnvironmentContext.getLanguage()));

        return new PageTitleData(title, rootTranslated, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminSchedulerTaskListBreadcrumbs() {
        final String nerd = translatorService.translate("Breadcrumbs: Scheduler tasks", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminSchedulerNewBreadcrumbs() {
        final String scheduler = translatorService.translate("Breadcrumbs: Scheduler tasks", EnvironmentContext.getLanguage());
        final String aNew = translatorService.translate("Breadcrumbs: New Scheduler tasks", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), scheduler, aNew);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSchedulerTaskListLink(EnvironmentContext.getLanguage()), aNew);

        return new PageTitleData(title, scheduler, breadcrumbs);
    }

    @Override
    public PageTitleData getAdminSchedulerEditBreadcrumbs(final String scheduledTaskName) {
        final String scheduler = translatorService.translate("Breadcrumbs: Scheduler tasks", EnvironmentContext.getLanguage());
        final String edit = translatorService.translate("Breadcrumbs: Edit", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), scheduledTaskName, scheduler);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSchedulerTaskListLink(EnvironmentContext.getLanguage()), scheduledTaskName, edit);

        return new PageTitleData(title, scheduler, breadcrumbs);
    }

    @Override
    public PageTitleData getVotingCategoryListBreadcrumbs() {
        final String nerd = translatorService.translate("Breadcrumbs: Voting categories", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getVotingCategoryNewBreadcrumbs() {
        final String nerd = translatorService.translate("Breadcrumbs: Voting categories", EnvironmentContext.getLanguage());
        final String aNew = translatorService.translate("Breadcrumbs: New Voting category", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd, aNew);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminVotingCategoriesRootLink(EnvironmentContext.getLanguage()), aNew);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getVotingCategoryEditDataBreadcrumbs(final PhotoVotingCategory photoVotingCategory) {
        final String nerd = translatorService.translate("Breadcrumbs: Voting categories", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd, photoVotingCategory.getName());
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminVotingCategoriesRootLink(EnvironmentContext.getLanguage()), photoVotingCategory.getName());

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData setActiveConfigurationBreadcrumbs() {
        final String nerd = translatorService.translate("Set active configuration", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(EnvironmentContext.getLanguage()), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getUpgradeTasksListBreadcrumbs() {
        final String nerd = translatorService.translate("Upgrade tasks", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminUpgradeLink(EnvironmentContext.getLanguage()), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getUpgradeBreadcrumbs(final UpgradeState upgradeState, final int doneUpgradeTasks, final int totalUpgradeTasks, final int errors) {
        final String header = translatorService.translate("Upgrade tasks", EnvironmentContext.getLanguage());

        final String nerd = translatorService.translate("Upgrade tasks - $1 ( done $2 of $3, errors: $4 )", EnvironmentContext.getLanguage()
                , upgradeState.getName(), String.valueOf(doneUpgradeTasks), String.valueOf(totalUpgradeTasks), String.valueOf(errors));

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), header);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminUpgradeLink(EnvironmentContext.getLanguage()), nerd);

        return new PageTitleData(title, header, breadcrumbs);
    }

    @Override
    public PageTitleData getAnonymousDaysBreadcrumbs() {
        final String nerd = translatorService.translate("Breadcrumbs: Anonymous Posting Days", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getTranslatorBreadcrumbs() {
        final String nerd = translatorService.translate("Breadcrumbs: Translator", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getControlPanelTitleBreadcrumbs() {
        final String nerd = translatorService.translate("Breadcrumbs: Control panel", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getUsersReportBreadcrumbs() {
        final String root = translatorService.translate("Breadcrumbs: Reports", EnvironmentContext.getLanguage());
        final String nerd = translatorService.translate("Breadcrumbs: Users", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), root, nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), root, nerd);

        return new PageTitleData(title, root, breadcrumbs);
    }

    @Override
    public PageTitleData getGenresTranslationsBreadcrumbs() {
        final String nerd = translatorService.translate("Breadcrumbs: Genres translations", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminGenresRootLink(EnvironmentContext.getLanguage()), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getVotingCategoriesTranslationsBreadcrumbs() {
        final String nerd = translatorService.translate("Voting categories translations", EnvironmentContext.getLanguage());

        final String title = pageTitleUtilsService.getTitleDataString(getAdminTranslatedRoot(), nerd);
        final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(getAdminTranslatedRoot(), entityLinkUtilsService.getAdminVotingCategoriesRootLink(EnvironmentContext.getLanguage()), nerd);

        return new PageTitleData(title, nerd, breadcrumbs);
    }

    @Override
    public PageTitleData getRestrictionListBreadcrumbs() {

        final String title = title(MenuService.MAIN_MENU_RESTRICTION_LIST).build();
        final String header = header(MenuService.MAIN_MENU_RESTRICTION_LIST).build();

        final String breadcrumbs = new BreadcrumbsBuilder(services)
                .adminRoot()
                .translatableString(MenuService.MAIN_MENU_RESTRICTION_LIST)
                .build();

        return new PageTitleData(title, header, breadcrumbs);
    }

    @Deprecated
    private String getAdminTranslatedRoot() {
        return translatorService.translate("Breadcrumbs: Administration", EnvironmentContext.getLanguage());
    }

    @Deprecated
    private String getJobsRootTranslated() {
        return translatorService.translate("Breadcrumbs: Jobs", EnvironmentContext.getLanguage());
    }

    private String getDefaultConfigurationSign() {
        return String.format(" - %s", translatorService.translate("Configuration: default configuration sign", EnvironmentContext.getLanguage()));
    }

    private String getConfigurationActiveSign() {
        return String.format(" - %s", translatorService.translate("Configuration: active configuration sign", EnvironmentContext.getLanguage()));
    }

    private BreadcrumbsBuilder title(final String text) {
        return new BreadcrumbsBuilder(services)
                .projectName()
                .translatableString(text)
                ;
    }

    private BreadcrumbsBuilder header(final String text) {
        return new BreadcrumbsBuilder(services)
                .translatableString(text)
                ;
    }
}
