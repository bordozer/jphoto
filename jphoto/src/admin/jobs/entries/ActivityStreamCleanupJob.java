package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;
import ui.activity.ActivityType;
import utils.ListUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class ActivityStreamCleanupJob extends AbstractJob {

	private int leaveActivityForDays;
	private List<ActivityType> activityTypes;

	public ActivityStreamCleanupJob( final JobRuntimeEnvironment jobEnvironment) {
		super( new LogHelper( ActivityStreamCleanupJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {
		final Date timeFrame = services.getDateUtilsService().getFirstSecondOfTheDayNDaysAgo( leaveActivityForDays );

		services.getActivityStreamService().deleteEntriesOlderThen( activityTypes, timeFrame );

		increment();
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.DAYS, new CommonProperty( SavedJobParameterKey.DAYS.getId(), leaveActivityForDays ) );

		final List<Integer> statusIds = ListUtils.convertIdentifiableListToListOfIds( activityTypes );
		parametersMap.put( SavedJobParameterKey.ENTRY_TYPES, CommonProperty.createFromIntegerList( SavedJobParameterKey.ENTRY_TYPES.getId(), statusIds, services.getDateUtilsService() ) );

		return parametersMap;
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		totalJopOperations = 1;
		leaveActivityForDays = jobParameters.get( SavedJobParameterKey.DAYS ).getValueInt();

		final List<Integer> statusIds = jobParameters.get( SavedJobParameterKey.ENTRY_TYPES ).getValueListInt();
		activityTypes = newArrayList();
		for ( final int statusId : statusIds ) {
			activityTypes.add( ActivityType.getById( statusId ) );
		}
	}

	@Override
	public String getJobParametersDescription() {
		final StringBuilder builder = new StringBuilder();

		builder.append( services.getTranslatorService().translate( "Delete activities older then $1 days", getLanguage(), String.valueOf( leaveActivityForDays ) ) );
		builder.append( "<br />" );

		builder.append( services.getTranslatorService().translate( "Activity types to delete", getLanguage() ) ).append( ": " );
		if ( activityTypes.size() == ActivityType.values().length ) {
			builder.append( services.getTranslatorService().translate( "ActivityStreamCleanupJob: All activity types", getLanguage() ) );
		}  else {
			for ( final ActivityType activityType : activityTypes ) {
				builder.append( services.getTranslatorService().translate( activityType.getName(), getLanguage() ) ).append( "<br />" );
			}
		}

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

	public void setActivityTypes( final List<ActivityType> activityTypes ) {
		this.activityTypes = activityTypes;
	}

	public List<ActivityType> getActivityTypes() {
		return activityTypes;
	}
}
