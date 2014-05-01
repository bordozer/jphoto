<%@ tag import="core.general.executiontasks.PeriodUnit" %>
<%@ tag import="admin.controllers.scheduler.tasks.edit.SchedulerTaskEditModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="startTaskDate" type="java.lang.String" required="false" %>
<%@ attribute name="startTaskTime" type="java.lang.String" required="true" %>
<%@ attribute name="period" type="java.lang.String" required="true" %>

<%@ attribute name="endTaskTime" type="java.lang.String" required="true" %>
<%@ attribute name="endTaskDate" type="java.lang.String" required="true" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="tasks" tagdir="/WEB-INF/tags/executionTask" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="periodicalTaskPeriod" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_PERIODICAL_TASK_PERIOD_CONTROL%>" />
<c:set var="periodicalTaskPeriodUnitId" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_PERIODICAL_TASK_PERIOD_UNIT_CONTROL%>" />
<c:set var="periodicalTaskHours" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_PERIODICAL_TASK_HOURS_CONTROL%>" />
<c:set var="hours" value="<%=SchedulerTaskEditModel.HOURS%>"/>

<c:set var="periodUnits" value="<%=PeriodUnit.values()%>"/>
<c:set var="hourUnitId" value="<%=PeriodUnit.HOUR.getId()%>"/>

<table:table border="0" width="100%">

	<tasks:common startTaskDate="${startTaskDate}"
				  startTaskTime="${startTaskTime}"
				  endTaskDate="${endTaskDate}"
				  endTaskTime="${endTaskTime}"
			/>

	<table:tr>
		<table:tdtext text_t="Interval" isMandatory="true" />

		<table:tddata>
			<html:input fieldId="${periodicalTaskPeriod}" size="4" fieldValue="${period}"/>
			<form:select path="${periodicalTaskPeriodUnitId}" items="${periodUnits}" itemValue="id" itemLabel="name" onclick="showHideHours();" />
		</table:tddata>
	</table:tr>

	<table:tr>
		<table:tdtext text_t="Time of day" isMandatory="true" /> <%-- TODO: hide this label when hoursDiv is hidden --%>
		<table:tddata>
			<div id="hoursDiv" style="display: none;">
				<js:checkboxMassChecker checkboxClass="${periodicalTaskHours}" initiallyChecked="true" />
				<br />
				<form:checkboxes path="${periodicalTaskHours}" items="${hours}" cssClass="${periodicalTaskHours}" />
			</div>
		</table:tddata>
	</table:tr>

</table:table>

<script type="text/javascript">

	require( [ 'jquery' ], function( $ ) {

		$( document ).ready( function () {
			<%--var unit = $( '#${periodicalTaskPeriodUnitId}' ).val;--%>
			showHideHours();
		} );

		function showHideHours() {
			var unit = $( '#${periodicalTaskPeriodUnitId}' ).val();
			if ( unit == ${hourUnitId} ) {
				$( '#hoursDiv' ).hide();
			} else {
				$( '#hoursDiv' ).show();
			}
		}
	});
</script>