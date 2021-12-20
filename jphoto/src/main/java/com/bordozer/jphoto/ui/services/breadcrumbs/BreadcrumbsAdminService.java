package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.admin.jobs.enums.JobListTab;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.admin.services.services.UpgradeState;
import com.bordozer.jphoto.core.general.configuration.ConfigurationTab;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.ui.elements.PageTitleData;

public interface BreadcrumbsAdminService {

    PageTitleData getJobListBreadcrumbs(final JobListTab jobListTab);

    PageTitleData getSavedJobListFilteredByJobTypeBreadcrumbs(final JobListTab jobListTab, final SavedJobType savedJobType);

    PageTitleData getJobEditBreadcrumbs(final SavedJob savedJob);

    PageTitleData getAdminConfigurationNewBreadcrumbs();

    PageTitleData getAdminConfigurationInfoBreadcrumbs(final SystemConfiguration systemConfiguration);

    PageTitleData getAdminConfigurationInfoTbBreadcrumbs(final SystemConfiguration systemConfiguration, final String configTabName);

    PageTitleData getAdminSystemConfigurationListBreadcrumbs();

    PageTitleData getAdminConfigurationEditTabBreadcrumbs(final int systemConfigurationId, final SystemConfiguration systemConfiguration, final ConfigurationTab configTab);

    PageTitleData getAdminConfigurationEditDataBreadcrumbs(final SystemConfiguration systemConfiguration);

    PageTitleData getAdminJobsBreadcrumbs(final String nerd);

    PageTitleData getAdminJobsDataTemplateBreadcrumbs(final String nerd);

    PageTitleData getAdminSchedulerTaskListBreadcrumbs();

    PageTitleData getAdminSchedulerNewBreadcrumbs();

    PageTitleData getAdminSchedulerEditBreadcrumbs(final String scheduledTaskName);

    PageTitleData getVotingCategoryListBreadcrumbs();

    PageTitleData getVotingCategoryNewBreadcrumbs();

    PageTitleData getVotingCategoryEditDataBreadcrumbs(final PhotoVotingCategory photoVotingCategory);

    PageTitleData setActiveConfigurationBreadcrumbs();

    PageTitleData getUpgradeTasksListBreadcrumbs();

    PageTitleData getUpgradeBreadcrumbs(final UpgradeState upgradeState, final int doneUpgradeTasks, final int totalUpgradeTasks, final int errors);

    PageTitleData getAnonymousDaysBreadcrumbs();

    PageTitleData getTranslatorBreadcrumbs();

    PageTitleData getControlPanelTitleBreadcrumbs();

    PageTitleData getUsersReportBreadcrumbs();

    PageTitleData getGenresTranslationsBreadcrumbs();

    PageTitleData getVotingCategoriesTranslationsBreadcrumbs();

    PageTitleData getRestrictionListBreadcrumbs();
}
