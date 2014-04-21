<%@ page import="admin.controllers.scheduler.tasks.list.SchedulerTaskListController" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="schedulerTaskListModel" type="admin.controllers.scheduler.tasks.list.SchedulerTaskListModel" scope="request"/>

<c:set var="activateGroupAction" value="<%=SchedulerTaskListController.SCHEDULER_TASK_LIST_GROUP_ACTION_ACTIVATE%>"/>
<c:set var="deactivateGroupAction" value="<%=SchedulerTaskListController.SCHEDULER_TASK_LIST_GROUP_ACTION_DEACTIVATE%>"/>
<c:set var="deleteGroupAction" value="<%=SchedulerTaskListController.SCHEDULER_TASK_LIST_GROUP_ACTION_DELETE%>"/>

<c:set var="separator" value="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"/>

<tags:page pageModel="${schedulerTaskListModel.pageModel}">

	<style type="text/css">
		.inactiveTasks {
			background: #E8E8E8;
		}

		.imageOpacity {
			opacity: 0.2;
			filter: alpha(opacity = 40); /* For IE8 and earlier */
		}
	</style>

	<form:form action="" method="POST" modelAttribute="schedulerTaskListModel" >
		
		<form:hidden id="schedulerTasksFormAction" path="schedulerTasksFormAction" />

		<div style="width: 95%; float: left; height: 100%; margin: 10px;">

			<links:schedulerTaskNew>
				<html:img32 src="add32.png" alt="${eco:translate('SchedulerTaskList: Create new scheduler task')}" />
			</links:schedulerTaskNew>

			<html:img32 src="scheduler/schedulerDeteteTask.png" alt="${eco:translate('SchedulerTaskList: Delete selected scheduler tasks')}" onclick="submitSchedulerTaskListForm( '${deleteGroupAction}' )" />

			${separator}

			<c:set var="schedulerRunning" value="${schedulerTaskListModel.schedulerRunning}"/>

			<c:if test="${schedulerRunning}">
				<links:schedulerStop>
					<html:img32 src="scheduler/SchedulerIsRunning.png" alt="${eco:translate('SchedulerTaskList: The Scheduler is running. Click to stop scheduler.')}" />
				</links:schedulerStop>
			</c:if>

			<c:if test="${not schedulerRunning}">
				<links:schedulerRun>
					<html:img32 src="scheduler/SchedulerIsStopped.png" alt="${eco:translate('SchedulerTaskList: The Scheduler is stopped. Click to run scheduler.')}" />
				</links:schedulerRun>
			</c:if>

			${separator}

			<html:img32 src="scheduler/schedulerTaskActivate.png" alt="${eco:translate('SchedulerTaskList: Activate selected scheduler tasks')}" onclick="submitSchedulerTaskListForm( '${activateGroupAction}' )" />
			<html:img32 src="scheduler/schedulerTaskDeactivate.png" alt="${eco:translate('SchedulerTaskList: Deactivate selected scheduler tasks')}" onclick="submitSchedulerTaskListForm( '${deactivateGroupAction}' )" />

			<script type="text/javascript">
				function submitSchedulerTaskListForm( action ) {
					var confirmation = "${eco:translate('SchedulerTaskList: Activate selected tasks?')}";
					if ( action == '${deactivateGroupAction}' ) {
						confirmation = "${eco:translate('SchedulerTaskList: Deactivate selected tasks?')}";
					} else if ( action == '${deleteGroupAction}' ) {
						confirmation = "${eco:translate('SchedulerTaskList: Delete selected tasks?')}";
					}

					if ( confirm( confirmation ) ) {
						$( '#schedulerTasksFormAction' ).val( action );
						$( '#schedulerTaskListModel' ).submit();
					}
				}
			</script>

			<br/>
			<br/>

			<js:confirmAction/>

			<c:set var="colspan" value="14"/>

			<table:table border="0" width="100%">

				<jsp:attribute name="thead">
					<table:tdicon />

					<table:tdicon >
						<js:checkBoxChecker namePrefix="schedulerTaskCheckbox" />
					</table:tdicon>

					<table:th text_t="id" title_t="Scheduled task id" width="16" />
					<table:tdicon />
					<table:tdicon />
					<table:th text_t="Task type" title_t="Scheduled task type" width="32"/>
					<table:th text_t="Task name" title_t="Scheduled task name" />
					<table:th text_t="Active"  title_t="Scheduled task is active" width="30" />
					<table:th text_t="Scheduled" title_t="Scheduled task is scheduled" width="30" />
					<table:th text_t="Task parameters" title_t="Scheduled task' parameters" />
					<table:th text_t="Job type" width="32" title_t="Scheduled task job type" />
					<table:th text_t="Job id" width="32" title_t="Scheduled task job id"/>
					<table:th text_t="Job" width="250" title_t="Scheduled task job"/>
				</jsp:attribute>

				<jsp:body>

					<c:forEach var="scheduledTasksData" items="${schedulerTaskListModel.scheduledTasksData}">

						<c:set var="schedulerTask" value="${scheduledTasksData.schedulerTasks}"/>
						<c:set var="isTaskActive" value="${schedulerTask.executionTask.executionTaskActive}"/>
						<c:set var="savedJobId" value="${schedulerTask.savedJobId}"/>
						<c:set var="schedulerTaskType" value="${schedulerTask.taskType}"/>

						<c:set var="css" value=""/>
						<c:set var="taskIcon" value="schedulerTaskActivate.png"/>
						<c:set var="taskIconTitle" value="${eco:translate('SchedulerTaskList: The task is active')}"/>
						<c:if test="${not isTaskActive}">
							<c:set var="css" value="inactiveTasks"/>
							<c:set var="taskIcon" value="schedulerTaskDeactivate.png"/>
							<c:set var="taskIconTitle" value="${eco:translate('SchedulerTaskList: The task is inactive')}"/>
						</c:if>

						<c:set var="imgCss" value=""/>
						<c:set var="imgTitle" value=""/>
						<c:if test="${not schedulerRunning}">
							<c:set var="imgCss" value="imageOpacity"/>
							<c:set var="imgTitle" value="${eco:translate('SchedulerTaskList: The scheduler is stopped')}"/>
						</c:if>

						<c:set var="schedulerTaskId" value="${schedulerTask.id}"/>

						<table:tr>

							<table:tdicon cssClass="${css}">
								<html:img32 src="scheduler/${taskIcon}" alt="${taskIconTitle} ${imgTitle}" cssClass="${imgCss}" />
							</table:tdicon>

							<table:tdicon cssClass="${css}">
								<form:checkbox path="schedulerTaskCheckbox" value="${schedulerTaskId}" />
							</table:tdicon>

							<table:tdicon cssClass="${css}">${schedulerTaskId}</table:tdicon>

							<table:tdicon cssClass="${css}">
								<admin:schedulerTaskEdit schedulerTask="${schedulerTask}">
									<html:img16 src="edit16.png" />
								</admin:schedulerTaskEdit>
							</table:tdicon>

							<table:tdicon cssClass="${css}">
								<a href="${eco:baseAdminUrl()}/scheduler/tasks/${schedulerTaskId}/delete/" title="${eco:translate1('SchedulerTaskList: Delete task \'$1\'', schedulerTask.name)}">
									<html:img16 src="delete16.png" onclick="return confirmDeletion( 'Delete \\'${schedulerTask.name}\\'?' );"/>
								</a>
							</table:tdicon>

							<table:td cssClass="${css} text-centered">
								<html:img32 src="scheduler/type/${schedulerTaskType.icon}" alt="${eco:translate(schedulerTaskType.name)}"/>
							</table:td>

							<table:td cssClass="${css}">
								<a href="${eco:baseAdminUrl()}/scheduler/tasks/${schedulerTaskId}/info/" title="${eco:translate1('SchedulerTaskList: View info \'$1\'', schedulerTask.name)}">
									${schedulerTask.name}
								</a>
							</table:td>

							<table:td width="30" cssClass="text-centered ${css}"><c:if test="${isTaskActive}">Yes</c:if></table:td>
							<table:td width="30" cssClass="text-centered ${css}"><c:if test="${scheduledTasksData.scheduled}">Yes</c:if></table:td>

							<table:td cssClass="${css}">${eco:translatableMessage(schedulerTask.description)}</table:td>

							<c:set var="savedJob" value="${schedulerTaskListModel.savedJobMap[savedJobId]}"/>
							<table:td cssClass="${css}">
								<html:img32 src="jobtype/${savedJob.jobType.icon}" alt="${eco:translate(savedJob.jobType.name)}"/>
							</table:td>

							<table:td cssClass="${css}">
								<span title="${eco:translate1('SchedulerTaskList: Job: #$1', savedJobId)}">#${savedJobId}</span>
							</table:td>

							<table:td cssClass="${css}">
								<links:savedJobEdit savedJob="${savedJob}" />
							</table:td>

						</table:tr>

						<table:separator colspan="${colspan}" />

					</c:forEach>

				</jsp:body>

			</table:table>

		</div>

	</form:form>

	<tags:springErrorHighliting bindingResult="${schedulerTaskListModel.bindingResult}"/>

	<div class="footerseparator"></div>

</tags:page>