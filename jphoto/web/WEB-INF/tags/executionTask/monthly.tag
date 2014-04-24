<%@ tag import="core.general.executiontasks.Month" %>
<%@ tag import="java.util.List" %>
<%@ tag import="static com.google.common.collect.Lists.newArrayList" %>
<%@ tag import="admin.controllers.scheduler.tasks.edit.SchedulerTaskEditModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="startTaskDate" type="java.lang.String" required="false" %>
<%@ attribute name="startTaskTime" type="java.lang.String" required="true" %>

<%@ attribute name="endTaskDate" type="java.lang.String" required="true" %>
<%@ attribute name="endTaskTime" type="java.lang.String" required="true" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="tasks" tagdir="/WEB-INF/tags/executionTask" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="months" value="<%=Month.values()%>"/>
<c:set var="monthlyTaskMonthIds" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_MONTHLY_TASK_WEEKDAY_IDS_CONTROL%>" />
<c:set var="monthlyDayOfMonth" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_MONTHLY_TASK_DAY_OF_MONTH_CONTROL%>" />
<c:set var="schedulerTaskTime" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_TIME_CONTROL%>" />

<%
	final List<Integer> daysOfMonth = newArrayList();
	for ( int i = 1; i <= 31; i++ ) {
		daysOfMonth.add( i );
	}
%>
<c:set var="daysOfMonth" value="<%=daysOfMonth%>" />

<table:table border="0" width="100%">

	<tasks:common startTaskDate="${startTaskDate}"
				  endTaskDate="${endTaskDate}"
				  hideStartTaskTime="true"
				  hideEndTaskTime="true"
			/>

	<table:tr>
		<table:tdtext text_t="Execution time" isMandatory="true" />

		<table:tddata>
			<html:input fieldId="${schedulerTaskTime}" size="6" fieldValue="${startTaskTime}"/> <tasks:executionTaskTime />
		</table:tddata>
	</table:tr>

	<table:tr>
		<table:tdtext text_t="Day of month" isMandatory="true" />

		<table:tddata>
			<form:select path="${monthlyDayOfMonth}" >
				<form:option value="0" label=""/>
				<form:options items="${daysOfMonth}"/>
				<form:option value="-1" label="${eco:translate('Last day of month')}"/>
			</form:select>
		</table:tddata>
	</table:tr>

	<table:tr>
		<table:tdtext text_t="Months" isMandatory="true" />

		<table:tddata>
			<js:checkboxMassChecker checkboxClass="monthlyTaskMonthIds" />
			<br />
			<form:checkboxes path="${monthlyTaskMonthIds}" items="${months}" itemValue="id" itemLabel="name" delimiter="<br />" cssClass="monthlyTaskMonthIds" />
		</table:tddata>
	</table:tr>

</table:table>