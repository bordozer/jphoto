<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="upgradeModel" type="admin.controllers.upgrade.UpgradeModel" scope="request"/>

<tags:pageLight title="${eco:translate('System update')}">

	<c:set var="upgradeTasksToPerform" value="${upgradeModel.upgradeTasksToPerform}"/>

	<html:img id="run" src="/icons32/start32.png" width="32" height="32" onclick="runUpgrade();" alt="${eco:translate('Run upgrade tasks')}"/>
	<script type="text/javascript">
		function runUpgrade() {
			<c:if test="${not empty upgradeTasksToPerform}">
				if ( confirm( "${eco:translate('Run upgrade tasks?')}" ) ) {
					$( '#FormName' ).submit();
				}
			</c:if>

			<c:if test="${ empty upgradeTasksToPerform}">
				alert( '${eco:translate('There are no unperformed upgrade tasks')}' );
			</c:if>
		}
	</script>

	<style type="text/css">
		.notasks {
			float: left;
			width: 100%;
			padding-left: 10px;
			padding-top: 10px;
		}

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

	<c:set var="upgradeTaskLogEntries" value="${upgradeModel.upgradeTaskLogEntries}"/>

	<form:form modelAttribute="upgradeModel" action="${eco:baseAdminUrlWithPrefix()}/upgrade/" method="POST" id="FormName">
	</form:form>
	
	<table width="900" border="1" cellpadding="0" cellspacing="0">

		<thead class="block-background base-font-color">
			<td colspan="2">
				${eco:translate('Upgrade task to perform')}
			</td>
		</thead>

		<c:forEach var="upgradeTaskToPerform" items="${upgradeTasksToPerform}">
			<tr>
				<td width="50">${upgradeTaskToPerform.version}</td>
				<td>${upgradeTaskToPerform.upgradeTaskName}</td>
			</tr>
		</c:forEach>

		<c:if test="${empty upgradeTasksToPerform}">
			<tr>
				<td class="notasks">
					${eco:translate('There are no unperformed upgrade tasks')}
				</td>
			</tr>
		</c:if>

	</table>

	<br />
	<br />
	<br />
	<br />

	<table width="900" border="1" cellpadding="0" cellspacing="0">

		<thead class="block-background base-font-color">
			<td colspan="2">
				${eco:translate('Performed upgrade tasks')}
			</td>
		</thead>

		<c:forEach var="upgradeLogTask" items="${upgradeTaskLogEntries}">
			<tr>
				<td width="50">${upgradeLogTask.upgradeTaskName}</td>
				<td>${eco:formatDate(upgradeLogTask.performanceTime)} ${eco:formatTime(upgradeLogTask.performanceTime)}</td>
			</tr>
		</c:forEach>

		<c:if test="${empty upgradeTaskLogEntries}">
			<tr>
				<td class="notasks">
					${eco:translate('Upgrade log is empty')}
				</td>
			</tr>
		</c:if>

	</table>

	<tags:springErrorHighliting bindingResult="${upgradeModel.bindingResult}" />

</tags:pageLight>