<%@ page import="admin.jobs.enums.JobExecutionStatus" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="upgradeModel" type="admin.controllers.upgrade.UpgradeModel" scope="request"/>

<tags:pageLight title="${eco:translate('System update')}">

	<style type="text/css">

		table {
			margin-left:auto;
			margin-right: auto;
		}

		table thead tr td {
			border: solid 1px #113311;
			border-top: solid 1px #C4C4C4;
			border-left: solid 1px #C4C4C4;
			padding: 10px;
			text-align: center;
			background: #C4C4C4;
		}

		table tr td {
			border-bottom:1px solid #d3d3d3;
			padding: 5px;
		}
	</style>

	<c:set var="upgradeMonitor" value="${upgradeModel.upgradeMonitor}"/>

	<br />
	<br />

	<table width="900" border="1" cellpadding="0" cellspacing="0">

		<thead class="block-background base-font-color">
			<td colspan="5" align="center">
				${eco:translate('Have done upgrade tasks')}
			</td>
		</thead>

		<c:forEach var="upgradeTasksToPerform" items="${upgradeMonitor.upgradeTasksToPerform}">
			<tr>

				<td width="20">
					<c:if test="${not empty upgradeTasksToPerform.upgradeTaskResult}">
						<c:if test="${upgradeTasksToPerform.upgradeTaskResult == 'SUCCESSFUL'}">
							<html:img id="" src="activeSysConfig.png" width="16" height="16" />
						</c:if>

						<c:if test="${upgradeTasksToPerform.upgradeTaskResult == 'ERROR'}">
							<html:img id="" src="inactiveSysConfig.png" width="16" height="16" />
						</c:if>
					</c:if>

					<c:if test="${empty upgradeTasksToPerform.upgradeTaskResult}">
						<c:if test="${upgradeMonitor.currentTask.upgradeTaskName == upgradeTasksToPerform.upgradeTaskName}">
							<html:img id="" src="forward16x16.png" width="16" height="16" />
						</c:if>
					</c:if>
				</td>

				<td width="600">
					${upgradeTasksToPerform.upgradeTaskName}
					<br />
					<c:if test="${not empty upgradeTasksToPerform.upgradeTaskResult && upgradeTasksToPerform.upgradeTaskResult == 'SUCCESSFUL'}">
						<tags:progress current="100" total="100" size="6" />
					</c:if>

					<c:if test="${empty upgradeTasksToPerform.upgradeTaskResult}">
						<c:if test="${upgradeMonitor.currentTask.upgradeTaskName == upgradeTasksToPerform.upgradeTaskName}">
							<tags:progress current="${upgradeMonitor.currentTaskProgress}" total="${upgradeMonitor.currentTaskTotal}" size="6" />
						</c:if>
					</c:if>

				</td>

				<td width="200">
					${eco:formatDate(upgradeTasksToPerform.startTime)} ${eco:formatTime(upgradeTasksToPerform.startTime)}
				</td>

				<td width="200">
					${eco:formatDate(upgradeTasksToPerform.endTimeTime)} ${eco:formatTime(upgradeTasksToPerform.endTimeTime)}
				</td>

				<td width="100">
					${upgradeTasksToPerform.upgradeTaskResult.nameTranslated}
				</td>

			</tr>
		</c:forEach>

	</table>

	<br />
	<br />
	<br />
	<br />

	<table width="900">

	<c:set var="taskMessageMap" value="${upgradeMonitor.taskMessageMap}" />
	<c:forEach var="entry" items="${taskMessageMap}">
		<c:set var="taskName" value="${entry.key}" />
		<c:set var="taskMessages" value="${entry.value}" />

		<thead class="block-background base-font-color">
			<td>${taskName}</td>
		</thead>

		<c:forEach var="taskMessage" items="${taskMessages}">
			<tr>
				<td>&nbsp;&nbsp;${taskMessage}</td>
			</tr>
		</c:forEach>

		<tr>
			<td>&nbsp;</td>
		</tr>
	</c:forEach>

	</table>

	<br />
	<br />

	<c:if test="${upgradeMonitor.upgradeState.active}">
		<script type="text/javascript">
			setTimeout( function () {
				document.location.reload();
			}, 1000 );
		</script>
	</c:if>

	<div style="text-align: center">

		<c:if test="${upgradeMonitor.upgradeState == 'STOPPED_BY_USER'}">
			<html:img id="done" src="ok_128x128.png" width="128" height="128" />
			<h3>All upgrade tasks have been performed successfully</h3>
		</c:if>

		<c:if test="${upgradeMonitor.upgradeState == 'ERROR'}">
			<html:img id="done" src="error_128x128.png" width="128" height="128" />
			<h3>There is an error</h3>
		</c:if>

	</div>

</tags:pageLight>