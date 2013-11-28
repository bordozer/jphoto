package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class DailyExecutionTask extends AbstractPeriodicalExecutionTask {

	private List<Weekday> weekdays;

	public DailyExecutionTask( final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.DAILY, dateUtilsService );
	}

	public DailyExecutionTask( final Map<SchedulerTaskProperty,CommonProperty> parametersMap, final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.DAILY, dateUtilsService );

		initTaskParameters( parametersMap );
	}

	@Override
	public void initTaskParameters( final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {

		super.initTaskParameters( parametersMap );

		final CommonProperty property = parametersMap.get( SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS );
		final List<Weekday> weekdayList = newArrayList();
		for ( final int weekday : property.getValueListInt() ) {
			weekdayList.add( Weekday.getById( weekday ) );
		}

		weekdays = weekdayList;
	}

	@Override
	public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
		final Map<SchedulerTaskProperty, CommonProperty> result = super.getParametersMap();

		result.put( SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS, new CommonProperty( SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS.getId(), CommonProperty.convertListToStringProperty( weekdays ) ) );

		return result;
	}

	public List<Weekday> getWeekdays() {
		return weekdays;
	}

	@Override
	public String getDescription() {

		final StringBuilder builder = new StringBuilder();

		builder.append( String.format( "Start time: %s", dateUtilsService.formatDateTime( startTaskTime ) ) ).append( "<br />" );
//		builder.append( String.format( "Skip missed executions: %s", skipMissedExecutions ) ).append( "<br />" );

		builder.append( "Week days: " );
		if ( weekdays.size() == 7 ) {
			builder.append( "Whole week" ).append( "<br />" );
		} else {
			final List<String> cronWeeks = newArrayList();
			for ( final Weekday weekday : weekdays ) {
				cronWeeks.add( weekday.getCroneSchedulerName() );
			}
			builder.append( StringUtils.join( cronWeeks, "," ) ).append( "<br />" );
		}

		if ( endTaskTime != null ) {
			builder.append( String.format( "End time: %s", dateUtilsService.formatDateTime( endTaskTime ) ) );
		}

		return builder.toString();
	}
}
