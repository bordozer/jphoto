package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class PeriodicalExecutionTask extends AbstractPeriodicalExecutionTask {

	private int period;
	private PeriodUnit periodUnit;
	private List<String> executionHours;

	protected PeriodicalExecutionTask( final Services services ) {
		super( ExecutionTaskType.PERIODICAL, services );
	}

	public PeriodicalExecutionTask( final Map<SchedulerTaskProperty, CommonProperty> parametersMap, final Services services ) {
		super( ExecutionTaskType.PERIODICAL, services );

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
	public TranslatableMessage getDescription() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "ExecutionTask: Start time:", services ).worldSeparator();
		translatableMessage.dateTimeFormatted( startTaskTime );
		translatableMessage.string( "<br />" );

		final TranslatableMessage mess = new TranslatableMessage( "ExecutionTask: Interval: $1 $2", services )
			.addIntegerParameter( period )
			.translatableString( periodUnit.getName() )
			;
		translatableMessage.addTranslatableMessageParameter( mess );

		if ( endTaskTime != null ) {
			translatableMessage.string( "<br />" );
			translatableMessage.translatableString( "ExecutionTask: End time:" ).worldSeparator().dateTimeFormatted( endTaskTime );
		}

		if ( periodUnit != PeriodUnit.HOUR ) {
			translatableMessage.string( "<br />" );
			translatableMessage.translatableString( "ExecutionTask: Hours:" ).worldSeparator();
			if ( executionHours.size() == 24 ) {
				translatableMessage.translatableString( "ExecutionTask: Every hour during the day" );
			} else {
				translatableMessage.string( StringUtils.join( executionHours, "," ) ); // TODO: translate executionHours
			}
		}

		return translatableMessage;
	}
}
