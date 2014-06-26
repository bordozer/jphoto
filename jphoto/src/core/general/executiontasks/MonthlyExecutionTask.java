package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class MonthlyExecutionTask extends AbstractPeriodicalExecutionTask {

	private List<Month> months;
	private int dayOfMonth;

	public MonthlyExecutionTask( final Services services ) {
		super( ExecutionTaskType.MONTHLY, services );
	}

	public MonthlyExecutionTask( final Map<SchedulerTaskProperty,CommonProperty> parametersMap, final Services services ) {
		super( ExecutionTaskType.MONTHLY, services );

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
	public TranslatableMessage getDescription() {

		final List<String> cronMonths = newArrayList();
		for ( final Month month : months ) {
			cronMonths.add( String.valueOf( month.getId() + 1 ) );
		}

		final TranslatableMessage translatableMessage = new TranslatableMessage( "ExecutionTask: Start date time", services ).string( ":" ).worldSeparator();
		translatableMessage.dateTimeFormatted( startTaskTime );
		translatableMessage.string( "<br />" );

		final String dayOfMont = dayOfMonth > 0 ? String.valueOf( dayOfMonth ) : "ExecutionTask: Last day";
		final String months = StringUtils.join( cronMonths, "," );

		final TranslatableMessage mess = new TranslatableMessage( "ExecutionTask: Day $1 of $2", services ).string( dayOfMont ).string( months );
		translatableMessage.addTranslatableMessageParameter( mess );
		translatableMessage.string( "<br />" );
		if ( endTaskTime != null ) {
			translatableMessage.translatableString( "ExecutionTask: End time" ).string( ":" ).worldSeparator().dateTimeFormatted( endTaskTime );
		}

		return translatableMessage;
	}
}
