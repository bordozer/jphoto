package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class PhotoRatingJob extends AbstractDateRangeableJob {

	public PhotoRatingJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( PhotoRatingJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {
		services.getPhotoRatingService().recalculatePhotoRatingForPeriodInDB( jobDateRange.getDateFrom(), jobDateRange.getDateTo() );
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		getDateRangeParametersMap( parametersMap );

		return parametersMap;
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		totalJopOperations = 1; // TODO: !!!

		setDateRangeParameters( jobParameters );
	}

	@Override
	public String getJobParametersDescription() {
		final StringBuilder builder = new StringBuilder();

		addDateRangeParameters( builder );

		return builder.toString();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.PHOTO_RATING;
	}
}
