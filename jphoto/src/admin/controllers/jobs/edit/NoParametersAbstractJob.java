package admin.controllers.jobs.edit;

import admin.jobs.entries.AbstractJob;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;
import utils.TranslatorUtils;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public abstract class NoParametersAbstractJob extends AbstractJob {

	public NoParametersAbstractJob(  final LogHelper log ) {
		super( log );
	}

	@Override
	final public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		return newHashMap();
	}

	@Override
	final public String getJobParametersDescription() {
		return TranslatorUtils.translate( "No parameters" );
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
	}
}
