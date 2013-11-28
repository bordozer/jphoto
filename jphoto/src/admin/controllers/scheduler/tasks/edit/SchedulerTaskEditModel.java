package admin.controllers.scheduler.tasks.edit;

import admin.jobs.general.SavedJob;
import core.general.base.AbstractGeneralModel;
import core.general.executiontasks.ExecutionTaskType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SchedulerTaskEditModel extends AbstractGeneralModel {

	public static final List<String> HOURS = newArrayList( "00","01","02","03","04","05","06","07","08","09","10","11", "12","13","14","15","16","17","18","19","20","21","22","23" );

	public static final String SCHEDULER_TASK_NAME_CONTROL = "schedulerTaskName";
	public static final String SCHEDULER_TASK_JOB_ID_CONTROL = "savedJobId";

	public static final String SCHEDULER_TASK_TIME_CONTROL = "schedulerTaskTime";

	public static final String START_TASK_DATE_CONTROL = "startTaskDate";
	public static final String END_TASK_TIME_CONTROL = "endTaskTime";
	public static final String END_TASK_DATE_CONTROL = "endTaskDate";

	public static final String SCHEDULER_TASK_SKIP_MISSED_EXECUTIONS_CONTROL = "skipMissedExecutions";

	public static final String SCHEDULER_TASK_PERIODICAL_TASK_PERIOD_CONTROL = "periodicalTaskPeriod";
	public static final String SCHEDULER_TASK_PERIODICAL_TASK_HOURS_CONTROL = "periodicalTaskHours";
	public static final String SCHEDULER_TASK_PERIODICAL_TASK_PERIOD_UNIT_CONTROL = "periodicalTaskPeriodUnitId";
	public static final String SCHEDULER_TASK_DAILY_TASK_WEEKDAY_IDS_CONTROL = "dailyTaskWeekdayIds";
	public static final String SCHEDULER_TASK_MONTHLY_TASK_WEEKDAY_IDS_CONTROL = "monthlyTaskMonthIds";
	public static final String SCHEDULER_TASK_MONTHLY_TASK_DAY_OF_MONTH_CONTROL = "monthlyDayOfMonth";
	public static final String SCHEDULER_TASK_IS_ACTIVE_CONTROL = "schedulerTaskActive";

	private String schedulerTaskName;
	private int executionTaskTypeId;
	private List<SavedJob> savedJobs;
	private int savedJobId;

	private String schedulerTaskTime;

	private String startTaskDate;
	private String endTaskTime;
	private String endTaskDate;

	private boolean skipMissedExecutions;
	private String periodicalTaskPeriod;
	private int periodicalTaskPeriodUnitId;
	private List<String> periodicalTaskHours;

	private List<String> dailyTaskWeekdayIds = newArrayList();

	private List<String> monthlyTaskMonthIds = newArrayList();
	private String monthlyDayOfMonth;

	private String formAction;
	private int schedulerTaskId;

	private boolean schedulerTaskActive;
	private ExecutionTaskType selectedTaskType;

	private String schedulerTaskSavedParameters;

	public void setSchedulerTaskId( final int schedulerTaskId ) {
		this.schedulerTaskId = schedulerTaskId;
	}

	public int getSchedulerTaskId() {
		return schedulerTaskId;
	}

	public String getSchedulerTaskName() {
		return schedulerTaskName;
	}

	public void setSchedulerTaskName( final String schedulerTaskName ) {
		this.schedulerTaskName = schedulerTaskName;
	}

	public int getExecutionTaskTypeId() {
		return executionTaskTypeId;
	}

	public void setExecutionTaskTypeId( final int executionTaskTypeId ) {
		this.executionTaskTypeId = executionTaskTypeId;
	}

	public List<SavedJob> getSavedJobs() {
		return savedJobs;
	}

	public void setSavedJobs( final List<SavedJob> savedJobs ) {
		this.savedJobs = savedJobs;
	}

	public int getSavedJobId() {
		return savedJobId;
	}

	public void setSavedJobId( final int savedJobId ) {
		this.savedJobId = savedJobId;
	}

	public String getSchedulerTaskTime() {
		return schedulerTaskTime;
	}

	public void setSchedulerTaskTime( final String schedulerTaskTime ) {
		String taskTime = schedulerTaskTime;

		/*if ( StringUtils.isEmpty( taskTime ) ) {
			taskTime = "00:00:00";
		}*/

		if ( taskTime.length() == 5 ) {
			taskTime = String.format( "%s:00", taskTime );
		}
		this.schedulerTaskTime = taskTime;
	}

	public String getStartTaskDate() {
		return startTaskDate;
	}

	public void setStartTaskDate( final String startTaskDate ) {
		this.startTaskDate = startTaskDate;
	}

	public String getEndTaskTime() {
		return endTaskTime;
	}

	public void setEndTaskTime( final String endTaskTime ) {
		String taskTime = endTaskTime;

		/*if ( StringUtils.isEmpty( taskTime ) ) {
			taskTime = "23:59:59";
		}*/

		if ( taskTime.length() == 5 ) {
			taskTime = String.format( "%s:00", taskTime );
		}
		this.endTaskTime = taskTime;
	}

	public String getEndTaskDate() {
		return endTaskDate;
	}

	public void setEndTaskDate( final String endTaskDate ) {
		this.endTaskDate = endTaskDate;
	}

	public boolean isSkipMissedExecutions() {
		return skipMissedExecutions;
	}

	public void setSkipMissedExecutions( final boolean skipMissedExecutions ) {
		this.skipMissedExecutions = skipMissedExecutions;
	}

	public String getPeriodicalTaskPeriod() {
		return periodicalTaskPeriod;
	}

	public void setPeriodicalTaskPeriod( final String periodicalTaskPeriod ) {
		this.periodicalTaskPeriod = periodicalTaskPeriod;
	}

	public int getPeriodicalTaskPeriodUnitId() {
		return periodicalTaskPeriodUnitId;
	}

	public void setPeriodicalTaskPeriodUnitId( final int periodicalTaskPeriodUnitId ) {
		this.periodicalTaskPeriodUnitId = periodicalTaskPeriodUnitId;
	}

	public List<String> getDailyTaskWeekdayIds() {
		return dailyTaskWeekdayIds;
	}

	public void setDailyTaskWeekdayIds( final List<String> dailyTaskWeekdayIds ) {
		this.dailyTaskWeekdayIds = dailyTaskWeekdayIds;
	}

	public List<String> getMonthlyTaskMonthIds() {
		return monthlyTaskMonthIds;
	}

	public void setMonthlyTaskMonthIds( final List<String> monthlyTaskMonthIds ) {
		this.monthlyTaskMonthIds = monthlyTaskMonthIds;
	}

	public String getMonthlyDayOfMonth() {
		return monthlyDayOfMonth;
	}

	public void setMonthlyDayOfMonth( final String monthlyDayOfMonth ) {
		this.monthlyDayOfMonth = monthlyDayOfMonth;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction( final String formAction ) {
		this.formAction = formAction;
	}

	public ExecutionTaskType getSelectedTaskType() {
		return selectedTaskType;
	}

	public void setSelectedTaskType( final ExecutionTaskType selectedTaskType ) {
		this.selectedTaskType = selectedTaskType;
	}

	public List<String> getPeriodicalTaskHours() {
		return periodicalTaskHours;
	}

	public void setPeriodicalTaskHours( final List<String> periodicalTaskHours ) {
		this.periodicalTaskHours = periodicalTaskHours;
	}

	@Override
	public void clear() {
		super.clear();

		schedulerTaskTime = null;
		startTaskDate = null;
		schedulerTaskId = 0;
		savedJobId = 0;

		endTaskDate = null;
		endTaskTime = null;

		schedulerTaskActive = true;
	}

	public boolean isSchedulerTaskActive() {
		return schedulerTaskActive;
	}

	public void setSchedulerTaskActive( final boolean schedulerTaskActive ) {
		this.schedulerTaskActive = schedulerTaskActive;
	}

	public String getSchedulerTaskSavedParameters() {
		return schedulerTaskSavedParameters;
	}

	public void setSchedulerTaskSavedParameters( final String schedulerTaskSavedParameters ) {
		this.schedulerTaskSavedParameters = schedulerTaskSavedParameters;
	}
}
