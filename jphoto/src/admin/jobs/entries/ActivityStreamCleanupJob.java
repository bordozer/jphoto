package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;

import java.util.Date;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ActivityStreamCleanupJob extends AbstractJob {

	private int leaveActivityForDays;

	public ActivityStreamCleanupJob( final JobRuntimeEnvironment jobEnvironment) {
		super( new LogHelper( ActivityStreamCleanupJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {
		final Date timeFrame = services.getDateUtilsService().getFirstSecondOfTheDayNDaysAgo( leaveActivityForDays );

		services.getActivityStreamService().deleteEntriesOlderThen( timeFrame );
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.DAYS, new CommonProperty( SavedJobParameterKey.DAYS.getId(), leaveActivityForDays ) );

		return parametersMap;
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		totalJopOperations = 1;
		leaveActivityForDays = jobParameters.get( SavedJobParameterKey.DAYS ).getValueInt();
	}

	@Override
	public String getJobParametersDescription() {
		final StringBuilder builder = new StringBuilder();

		builder.append( services.getTranslatorService().translate( "Delete activities older then $1 days", leaveActivityForDays ) );

		return builder.toString();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.ACTIVITY_STREAM_CLEAN_UP;
	}

	public int getLeaveActivityForDays() {
		return leaveActivityForDays;
	}

	public void setLeaveActivityForDays( final int leaveActivityForDays ) {
		this.leaveActivityForDays = leaveActivityForDays;
	}
}
