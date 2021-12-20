package com.bordozer.jphoto.admin.services.jobs;

import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SavedJobService extends BaseEntityService<SavedJob> {

    List<SavedJob> loadAll();

    boolean activate(final int savedJobId);

    boolean deactivate(final int savedJobId);

    SavedJob loadByName(final String jobName);

    CommonProperty getJobParameter(final int savedJobId, final SavedJobParameterKey key);

    List<SavedJob> loadAllActive();

    Map<SavedJobParameterKey, CommonProperty> getSavedJobParametersMap(final int savedJobId);

    List<SavedJob> getJobsByType(final SavedJobType savedJobType);

    Set<Integer> getNotDeletableJobIds();

    boolean isJobCanBeDeleted(final int savedJobId);
}
