package admin.controllers.jobs.edit;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.entries.AbstractJob;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public abstract class NoParametersAbstractJob extends AbstractJob {

	public NoParametersAbstractJob(  final LogHelper log, final JobRuntimeEnvironment jobEnvironment ) {
		super( log, jobEnvironment );
	}

	@Override
	final public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		return newHashMap();
	}

	@Override
	final public String getJobParametersDescription() {
		return services.getTranslatorService().translate( "No parameters" );
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
	}
}
