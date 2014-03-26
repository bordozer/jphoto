package core.services.pageTitle;

import admin.jobs.enums.JobListTab;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import admin.services.services.UpgradeState;
import core.general.configuration.ConfigurationTab;
import core.general.configuration.SystemConfiguration;
import core.general.photo.PhotoVotingCategory;
import core.services.translator.TranslatorService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;

public class PageTitleAdminUtilsServiceImpl implements PageTitleAdminUtilsService {
	
	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;
	
	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;
	
	@Autowired
	private TranslatorService translatorService;

	@Override
	public PageTitleData getJobListData( final JobListTab jobListTab ) {
		final String rootTranslated = getJobsRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink()
			, translatorService.translate( jobListTab.getName() )  );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getJobListFilteredByTypeData( final JobListTab jobListTab, final SavedJobType savedJobType ) {
		final String rootTranslated = getJobsRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink()
			, translatorService.translate( jobListTab.getName() ), translatorService.translate( savedJobType.getName() ) );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getJobEditData( final SavedJob savedJob ) {
		final String rootTranslated = getJobsRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink(), savedJob.getName(), translatorService.translate( "Edit" ) );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getJobNewData() {
		final String rootTranslated = getJobsRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink(), translatorService.translate( "new" ) );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminConfigurationNew() {
		final String tran = translatorService.translate( "New" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(), tran );

		return new PageTitleData( title, tran, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminConfigurationInfoData( final SystemConfiguration systemConfiguration ) {
		final String tran = translatorService.translate( "Configuration" );

		final String sysConfigName = String.format( "%s%s%s"
			, systemConfiguration.getName()
			, systemConfiguration.isDefaultConfiguration() ? translatorService.translate( " - default" ) : ""
			, systemConfiguration.isActiveConfiguration() ? translatorService.translate( " - active" ) : ""
		);

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), systemConfiguration.getName() );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(), sysConfigName );

		return new PageTitleData( title, tran, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminConfigurationInfoTbData( final SystemConfiguration systemConfiguration, final String configTabName ) {
		final String tran = translatorService.translate( "Tabs" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), tran, configTabName );
		final String sysConfigName = String.format( "%s%s%s"
			, entityLinkUtilsService.getAdminConfigurationTabsLink( systemConfiguration )
			, systemConfiguration.isDefaultConfiguration() ? translatorService.translate( " - default" ) : ""
			, systemConfiguration.isActiveConfiguration() ? translatorService.translate( " - active" ) : ""
		);

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(), sysConfigName, configTabName );

		return new PageTitleData( title, tran, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminSystemConfigurationListData() {
		final String tran = translatorService.translate( "System configuration" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), tran );

		return new PageTitleData( title, tran, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminConfigurationEditTabData( final int systemConfigurationId, final SystemConfiguration systemConfiguration, final ConfigurationTab configTab ) {
		final String configuration = translatorService.translate( "Configuration" );
		final String edit = translatorService.translate( "Edit" );

		final String sysConfigName = String.format( "%s%s%s"
			, entityLinkUtilsService.getAdminConfigurationTabsLink( systemConfiguration )
			, systemConfiguration.isDefaultConfiguration() ? translatorService.translate( " - default" ) : ""
			, systemConfiguration.isActiveConfiguration() ? translatorService.translate( " - active" ) : ""
		);

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), configuration, edit );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(), sysConfigName, entityLinkUtilsService.getAdminConfigurationLink( systemConfigurationId, configTab ), edit );

		return new PageTitleData( title, configuration, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminConfigurationEditData( final SystemConfiguration systemConfiguration ) {
		final String configuration = translatorService.translate( "Configuration" );
		final String edit = translatorService.translate( "Edit" );

		final String sysConfigName = String.format( "%s%s%s"
			, entityLinkUtilsService.getAdminConfigurationTabsLink( systemConfiguration )
			, systemConfiguration.isDefaultConfiguration() ? translatorService.translate( " - default" ) : ""
			, systemConfiguration.isActiveConfiguration() ? translatorService.translate( " - active" ) : ""
		);

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), configuration, edit );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(), sysConfigName, edit );

		return new PageTitleData( title, configuration, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminJobsData( final String nerd ) {
		final String rootTranslated = getJobsRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), rootTranslated, nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink(), nerd );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminJobsDataTemplate( final String nerd ) {
		final String rootTranslated = getJobsRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), rootTranslated, nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminJobsRootLink(), nerd, translatorService.translate( "Template" ) );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminSchedulerTaskListData() {
		final String nerd = translatorService.translate( "Scheduler tasks" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminSchedulerNewData() {
		final String scheduler = translatorService.translate( "Scheduler tasks" );
		final String aNew = translatorService.translate( "New" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), scheduler, aNew );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSchedulerTaskListLink(), aNew );

		return new PageTitleData( title, scheduler, breadcrumbs );
	}

	@Override
	public PageTitleData getAdminSchedulerEditData( final String scheduledTaskName ) {
		final String scheduler = translatorService.translate( "Scheduler tasks" );
		final String edit = translatorService.translate( "Edit" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), scheduledTaskName, scheduler );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSchedulerTaskListLink(), scheduledTaskName, edit );

		return new PageTitleData( title, scheduler, breadcrumbs );
	}

	@Override
	public PageTitleData getVotingCategoryList() {
		final String nerd = translatorService.translate( "Voting categories" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getVotingCategoryNewData() {
		final String nerd = translatorService.translate( "Voting categories" );
		final String aNew = translatorService.translate( "New" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd, aNew );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminVotingCategoriesRootLink(), aNew );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getVotingCategoryEditData( final PhotoVotingCategory photoVotingCategory ) {
		final String nerd = translatorService.translate( "Voting categories" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd, photoVotingCategory.getName() );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminVotingCategoriesRootLink(), photoVotingCategory.getName() );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public String getAdminTranslatedRoot() {
		return translatorService.translate( "Administration" );
	}

	private String getJobsRootTranslated() {
		return translatorService.translate( "Jobs" );
	}

	@Override
	public PageTitleData setActiveConfigurationData() {
		final String nerd = translatorService.translate( "Set active configuration" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminSystemConfigurationListLink(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getUpgradeTasksListData() {
		final String nerd = translatorService.translate( "Upgrade tasks" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminUpgradeLink(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getUpgradeData( final UpgradeState upgradeState, final int doneUpgradeTasks, final int totalUpgradeTasks, final int errors ) {
		final String header = translatorService.translate( "Upgrade tasks" );

		final String nerd = translatorService.translateWithParameters( "Upgrade tasks - $1 ( done $2 of $3, errors: $4 )"
			, upgradeState.getNameTranslated(), String.valueOf( doneUpgradeTasks ), String.valueOf( totalUpgradeTasks ), String.valueOf( errors ) );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), header );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), entityLinkUtilsService.getAdminUpgradeLink(), nerd );

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getAnonymousDaysData() {
		final String nerd = translatorService.translate( "Anonymous Posting Days" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getTranslatorTitle() {
		final String nerd = translatorService.translate( "Translator" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getControlPanelTitleData() {
		final String nerd = translatorService.translate( "Control panel" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}

	@Override
	public PageTitleData getUsersReportTitleData() {
		final String root = translatorService.translate( "Reports" );
		final String nerd = translatorService.translate( "Users" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), root, nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), root, nerd );

		return new PageTitleData( title, root, breadcrumbs );
	}

	@Override
	public PageTitleData getGenresTranslationsTitleData() {
		final String nerd = translatorService.translate( "Genres translations" );

		final String title = pageTitleUtilsService.getTitleDataString( getAdminTranslatedRoot(), nerd );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( getAdminTranslatedRoot(), nerd );

		return new PageTitleData( title, nerd, breadcrumbs );
	}
}
