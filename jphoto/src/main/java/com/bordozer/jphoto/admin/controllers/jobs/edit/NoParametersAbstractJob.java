package com.bordozer.jphoto.admin.controllers.jobs.edit;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.log.LogHelper;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public abstract class NoParametersAbstractJob extends AbstractJob {

    public NoParametersAbstractJob(final LogHelper log, final JobRuntimeEnvironment jobEnvironment) {
        super(log, jobEnvironment);
    }

    @Override
    final public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
        return newHashMap();
    }

    @Override
    final public String getJobParametersDescription() {
        return services.getTranslatorService().translate("NoParametersAbstractJob: No parameters", getLanguage());
    }

    @Override
    public void initJobParameters(final Map<SavedJobParameterKey, CommonProperty> jobParameters) {
    }
}
