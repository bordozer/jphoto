package admin.jobs.runners;

import admin.jobs.entries.AbstractJob;

public abstract class AbstractJobRunner {

	public abstract void runJob( final AbstractJob job );
}
