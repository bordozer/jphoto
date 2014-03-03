package core.services.pageTitle;

import admin.jobs.enums.JobListTab;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import admin.services.services.UpgradeState;
import core.general.configuration.ConfigurationTab;
import core.general.configuration.SystemConfiguration;
import core.general.photo.PhotoVotingCategory;
import elements.PageTitleData;

public interface PageTitleAdminUtilsService {

	PageTitleData getJobListData( JobListTab jobListTab );

	PageTitleData getJobListFilteredByTypeData( JobListTab jobListTab, SavedJobType savedJobType );

	PageTitleData getJobEditData( SavedJob savedJob );

	PageTitleData getJobNewData();

	PageTitleData getAdminConfigurationNew();

	PageTitleData getAdminConfigurationInfoData( SystemConfiguration systemConfiguration );

	PageTitleData getAdminConfigurationInfoTbData( SystemConfiguration systemConfiguration, String configTabName );

	PageTitleData getAdminSystemConfigurationListData();

	PageTitleData getAdminConfigurationEditTabData( int systemConfigurationId, SystemConfiguration systemConfiguration, ConfigurationTab configTab );

	PageTitleData getAdminConfigurationEditData( SystemConfiguration systemConfiguration );

	PageTitleData getAdminJobsData( String nerd );

	PageTitleData getAdminJobsDataTemplate( String nerd );

	PageTitleData getAdminSchedulerTaskListData();

	PageTitleData getAdminSchedulerNewData();

	PageTitleData getAdminSchedulerEditData( String scheduledTaskName );

	PageTitleData getVotingCategoryList();

	PageTitleData getVotingCategoryNewData();

	PageTitleData getVotingCategoryEditData( PhotoVotingCategory photoVotingCategory );

	String getAdminTranslatedRoot();

	PageTitleData setActiveConfigurationData();

	PageTitleData getUpgradeTasksListData();

	PageTitleData getUpgradeData( UpgradeState upgradeState, int doneUpgradeTasks, int totalUpgradeTasks, int errors );

	PageTitleData getAnonymousDaysData();

	PageTitleData getTranslatorTitle();
}
