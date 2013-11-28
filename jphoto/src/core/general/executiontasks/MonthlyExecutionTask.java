package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class MonthlyExecutionTask extends AbstractPeriodicalExecutionTask {

	private List<Month> months;
	private int dayOfMonth;

	public MonthlyExecutionTask( final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.MONTHLY, dateUtilsService );
	}

	public MonthlyExecutionTask( final Map<SchedulerTaskProperty,CommonProperty> parametersMap, final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.MONTHLY, dateUtilsService );

		initTaskParameters( parametersMap );
	}

	@Override
	public void initTaskParameters( final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {

		super.initTaskParameters( parametersMap );

		final CommonProperty property = parametersMap.get( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_MONTH_IDS );
		final List<Month> monthList = newArrayList();
		for ( final int monthId : property.getValueListInt() ) {
			monthList.add( Month.getById( monthId ) );
		}
		this.months = monthList;

		dayOfMonth = parametersMap.get( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_DAY_OF_MONTH ).getValueInt();
	}

	@Override
	public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
		final Map<SchedulerTaskProperty, CommonProperty> result = super.getParametersMap();

		result.put( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_MONTH_IDS, new CommonProperty( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_MONTH_IDS.getId(), CommonProperty.convertListToStringProperty( months ) ) );
		result.put( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_DAY_OF_MONTH, new CommonProperty( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_DAY_OF_MONTH.getId(), dayOfMonth ) );

		return result;
	}

	public List<Month> getMonths() {
		return months;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public Date getStartTaskDate() {
		return startTaskTime;
	}

	@Override
	public String getDescription() {

		final List<String> cronMonths = newArrayList();
		for ( final Month month : months ) {
			cronMonths.add( String.valueOf( month.getId() + 1 ) );
		}

		final StringBuilder builder = new StringBuilder();

		builder.append( String.format( "Start time: %s", dateUtilsService.formatDateTime( startTaskTime ) ) ).append( "<br />" );
//		builder.append( String.format( "Skip missed executions: %s", skipMissedExecutions ) ).append( "<br />" );
		builder.append( String.format( "Day %s of %s", ( dayOfMonth > 0 ? String.valueOf( dayOfMonth ) : "Last day" ), StringUtils.join( cronMonths, "," ) ) ).append( "<br />" );
		if ( endTaskTime != null ) {
			builder.append( String.format( "End time: %s", dateUtilsService.formatDateTime( endTaskTime ) ) );
		}

		return builder.toString();
	}
}
