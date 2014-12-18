package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.log.LogHelper;

public class ArchivingJob extends NoParametersAbstractJob {

	public ArchivingJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( ArchivingJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {

	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.ARCHIVING;
	}
}
