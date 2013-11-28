<%@ page import="core.general.executiontasks.ExecutionTaskType" %>
<%@ page import="admin.controllers.scheduler.tasks.edit.SchedulerTaskEditModel" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="task" tagdir="/WEB-INF/tags/executionTask" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="schedulerTaskEditModel" type="admin.controllers.scheduler.tasks.edit.SchedulerTaskEditModel" scope="request"/>

<c:set var="schedulerTaskName" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_NAME_CONTROL%>" />
<c:set var="savedJobId" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_JOB_ID_CONTROL%>" />

<tags:page pageModel="${schedulerTaskEditModel.pageModel}">

	<c:set var="executionTaskTypes" value="<%=ExecutionTaskType.values()%>"/>
	<c:set var="executionTaskIdOnce" value="<%=ExecutionTaskType.ONCE.getId()%>"/>
	<c:set var="executionTaskIdDaily" value="<%=ExecutionTaskType.DAILY.getId()%>"/>
	<c:set var="executionTaskIdMonthly" value="<%=ExecutionTaskType.MONTHLY.getId()%>"/>
	<c:set var="executionTaskIdInterval" value="<%=ExecutionTaskType.PERIODICAL.getId()%>"/>

	<c:set var="executionTaskTypeId" value="${schedulerTaskEditModel.executionTaskTypeId}"/>

	<c:set var="startTaskDate" value="${schedulerTaskEditModel.startTaskDate}"/>
	<c:set var="endTaskTime" value="${schedulerTaskEditModel.endTaskTime}"/>
	<c:set var="endTaskDate" value="${schedulerTaskEditModel.endTaskDate}"/>
	<c:set var="taskTime" value="${schedulerTaskEditModel.schedulerTaskTime}"/>
	<c:set var="isSchedulerTaskActive" value="${schedulerTaskEditModel.schedulerTaskActive}"/>

	<form:form method="POST" action="${eco:baseAdminUrlWithPrefix()}/scheduler/tasks/submit/" modelAttribute="schedulerTaskEditModel">

		<form:hidden path="formAction"/>

		<table:table border="0" width="700">

			<table:separatorInfo colspan="2" title="${schedulerTaskEditModel.schedulerTaskName}" />

			<table:tr>
				<table:tdtext text_t="Task name" isMandatory="true"/>
				<table:tddata>
					<form:input path="${schedulerTaskName}" size="47" maxlength="255"/>
				</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Job" isMandatory="true"/>
				<table:tddata>
					<form:select path="${savedJobId}" onchange="setTaskName();" >
						<form:option value="0" label=""/>
    					<form:options itemValue="id" itemLabel="name" items="${schedulerTaskEditModel.savedJobs}" />
					</form:select>
				</table:tddata>
			</table:tr>

			<table:separator colspan="2" />

			<table:tr>
				<table:tdtext text_t="Execute task"/>
				<table:tddata>
					<div style="float: left; width: 200px; font-weight: bold;">
						<form:radiobuttons path="executionTaskTypeId" items="${executionTaskTypes}"  itemValue="id" itemLabel="nameTranslated" onchange="submitFormData();" delimiter="<br />" />
					</div>
					<div style="float: left; width: 32px; padding-top: 25px;">
						<html:img32 src="scheduler/type/${schedulerTaskEditModel.selectedTaskType.icon}" alt="${schedulerTaskEditModel.selectedTaskType.nameTranslated}" />
					</div>
				</table:tddata>
			</table:tr>

			<%--<table:separator colspan="2" />--%>

			<table:tr>
				<table:td colspan="2">
					<c:if test="${executionTaskTypeId == executionTaskIdOnce}">
						<task:oneTime startTaskTime="${taskTime}"
									  startTaskDate="${startTaskDate}"
								/>
					</c:if>

					<c:if test="${executionTaskTypeId == executionTaskIdInterval}">
						<task:periodically startTaskDate="${startTaskDate}"
										   startTaskTime="${taskTime}"
										   endTaskDate="${endTaskDate}"
										   endTaskTime="${endTaskTime}"
										   period="${schedulerTaskEditModel.periodicalTaskPeriod}"
								/>
					</c:if>

					<c:if test="${executionTaskTypeId == executionTaskIdDaily}">
						<task:daily startTaskDate="${startTaskDate}"
									startTaskTime="${taskTime}"
									endTaskDate="${endTaskDate}"
									endTaskTime="${endTaskTime}"
								/>
					</c:if>

					<c:if test="${executionTaskTypeId == executionTaskIdMonthly}">
						<task:monthly startTaskDate="${startTaskDate}"
									  startTaskTime="${taskTime}"
									  endTaskDate="${endTaskDate}"
									  endTaskTime="${endTaskTime}"
								/>
					</c:if>

				</table:td>
			</table:tr>

			<table:separator colspan="2" />

			<table:trok text_t="Save" onclick="saveExecutionTask();" />

		</table:table>

	</form:form>

	<script type="text/javascript">
		function submitForm( formAction ) {
			$( '#formAction' ).val( formAction );
			$( '#schedulerTaskEditModel' ).submit();
		}

		function submitFormData() {
			submitForm( 'reload' );
		}

		function saveExecutionTask() {
			submitForm( 'save' );
		}

		function setTaskName() {
			var selectedJobName = $( '#${savedJobId}' ).val();
			var currentTaskName = $( '#${schedulerTaskName}' ).val();
			$( '#${schedulerTaskName}' ).val( $("#${savedJobId} option[value='" + selectedJobName + "']").text() );
		}
	</script>

	<tags:springErrorHighliting bindingResult="${schedulerTaskEditModel.bindingResult}"/>

	<div class="footerseparator"></div>

</tags:page>