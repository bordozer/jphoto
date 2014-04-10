<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="jobExecutionLogModel" type="admin.controllers.jobs.executionLog.JobExecutionLogModel" scope="request"/>

<tags:pageLight title="${eco:translate('Job execution log')}">

	<c:set var="job" value="${jobExecutionLogModel.job}" />
	<c:set var="jobNotFoundError" value="${jobExecutionLogModel.jobNotFoundError}" />
	<c:set var="jobRuntimeLogs" value="${jobExecutionLogModel.jobRuntimeLogsMessages}" />

	<div style="float: left;; width: 100%; padding: 10px;">

		<c:if test="${jobNotFoundError}">
			<h3>${eco:translate('Job not found. It must be finished.')}</h3>
		</c:if>

		<c:if test="${not empty jobRuntimeLogs}">
			<c:forEach var="jobRuntimeLogEntry" items="${jobRuntimeLogs}">

				${jobRuntimeLogEntry}

				<br/>

			</c:forEach>
		</c:if>

		<c:if test="${not jobNotFoundError and empty jobRuntimeLogs}">
			<h3>${eco:translate('No messages found')}</h3>
		</c:if>

	</div>

</tags:pageLight>