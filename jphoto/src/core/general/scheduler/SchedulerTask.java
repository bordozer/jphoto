package core.general.scheduler;

import core.general.base.AbstractBaseEntity;
import core.general.executiontasks.AbstractExecutionTask;
import core.general.executiontasks.ExecutionTaskType;
import core.interfaces.Nameable;

public class SchedulerTask extends AbstractBaseEntity implements Nameable {

	private String name;
	private ExecutionTaskType taskType;
	private int savedJobId;

	private AbstractExecutionTask executionTask;

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public ExecutionTaskType getTaskType() {
		return taskType;
	}

	public void setTaskType( final ExecutionTaskType taskType ) {
		this.taskType = taskType;
	}

	public int getSavedJobId() {
		return savedJobId;
	}

	public void setSavedJobId( final int savedJobId ) {
		this.savedJobId = savedJobId;
	}

	public AbstractExecutionTask getExecutionTask() {
		return executionTask;
	}

	public void setExecutionTask( final AbstractExecutionTask executionTask ) {
		this.executionTask = executionTask;
	}

	public String getDescription() {
		final StringBuilder builder = new StringBuilder();

//		builder.append( String.format( "Name: '%s'", name ) ).append( "<br />" );
//		builder.append( String.format( "Saved job Id: '%d'", savedJobId ) ).append( "<br />" );
//		builder.append( "Execution task:" ).append( "<br />" );
		builder.append( executionTask.getDescription() );

		return builder.toString();
	}
}