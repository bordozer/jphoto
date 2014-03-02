package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class PeriodicalExecutionTask extends AbstractPeriodicalExecutionTask {

	private int period;
	private PeriodUnit periodUnit;
	private List<String> executionHours;

	protected PeriodicalExecutionTask( final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.PERIODICAL, dateUtilsService );
	}

	public PeriodicalExecutionTask( final Map<SchedulerTaskProperty, CommonProperty> parametersMap, final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.PERIODICAL, dateUtilsService );

		initTaskParameters( parametersMap );
	}

	@Override
	public void initTaskParameters( final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {

		super.initTaskParameters( parametersMap );

		period = parametersMap.get( SchedulerTaskProperty.PROPERTY_TASK_PERIOD ).getValueInt();
		periodUnit = PeriodUnit.getById( parametersMap.get( SchedulerTaskProperty.PROPERTY_TASK_PERIOD_UNIT ).getValueInt() );
		final CommonProperty hoursProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_TASK_HOURS );
		if ( hoursProperty != null ) {
			executionHours = hoursProperty.getValueListString();
		}
	}

	@Override
	public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {

		final Map<SchedulerTaskProperty, CommonProperty> result = super.getParametersMap();

		result.put( SchedulerTaskProperty.PROPERTY_TASK_PERIOD, new CommonProperty( SchedulerTaskProperty.PROPERTY_TASK_PERIOD.getId(), period ) );
		result.put( SchedulerTaskProperty.PROPERTY_TASK_PERIOD_UNIT, new CommonProperty( SchedulerTaskProperty.PROPERTY_TASK_PERIOD_UNIT.getId(), periodUnit.getId() ) );
		result.put( SchedulerTaskProperty.PROPERTY_TASK_HOURS, new CommonProperty( SchedulerTaskProperty.PROPERTY_TASK_HOURS.getId(), executionHours ) );

		return result;
	}

	public int getPeriod() {
		return period;
	}

	public PeriodUnit getPeriodUnit() {
		return periodUnit;
	}

	public List<String> getExecutionHours() {
		return executionHours;
	}

	@Override
	public String getDescription() {
		final StringBuilder builder = new StringBuilder();

		builder.append( String.format( "Start time: %s", dateUtilsService.formatDateTime( startTaskTime ) ) ).append( "<br />" );
//		builder.append( String.format( "Skip missed executions: %s", skipMissedExecutions ) ).append( "<br />" );
		builder.append( String.format( "Interval: %d %s(s)", period, periodUnit.getNameTranslated() ) );
		if ( endTaskTime != null ) {
			builder.append( "<br />" ).append( String.format( "End time: %s", dateUtilsService.formatDateTime( endTaskTime ) ) );
		}

		if ( periodUnit != PeriodUnit.HOUR ) {
			builder.append( "<br />" ).append( "Hours: " );
			if ( executionHours.size() == 24 ) {
				builder.append( "Whole day" );
			} else {
				builder.append( StringUtils.join( executionHours, "," ) );
			}
		}

		return builder.toString();
	}
}
