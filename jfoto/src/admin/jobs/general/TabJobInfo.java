package admin.jobs.general;

import admin.jobs.enums.JobListTab;

public class TabJobInfo {

	private JobListTab jobListTab;
	private int tabJobsQty;

	public TabJobInfo( final JobListTab jobListTab, final int tabJobsQty ) {
		this.jobListTab = jobListTab;
		this.tabJobsQty = tabJobsQty;
	}

	public JobListTab getJobListTab() {
		return jobListTab;
	}

	public void setJobListTab( final JobListTab jobListTab ) {
		this.jobListTab = jobListTab;
	}

	public int getTabJobsQty() {
		return tabJobsQty;
	}

	public void setTabJobsQty( final int tabJobsQty ) {
		this.tabJobsQty = tabJobsQty;
	}
}
