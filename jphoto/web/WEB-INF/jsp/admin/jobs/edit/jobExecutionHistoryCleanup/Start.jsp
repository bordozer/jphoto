<%@ page import="admin.controllers.jobs.edit.jobExecutionHistoryCleanup.JobExecutionHistoryCleanupJobModel" %>
<%@ page import="admin.jobs.enums.JobExecutionStatus" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="jobExecutionHistoryCleanupJobModel" type="admin.controllers.jobs.edit.jobExecutionHistoryCleanup.JobExecutionHistoryCleanupJobModel" scope="request"/>

<c:set var="leave_activity_for_days_control" value="<%=JobExecutionHistoryCleanupJobModel.DELETE_ENTRIES_OLDER_THE_N_DAYS_CONTROL%>"/>
<c:set var="job_execution_status_ids_to_delete_control" value="<%=JobExecutionHistoryCleanupJobModel.JOB_EXECUTION_STATUS_IDS_TO_DELETE_CONTROL%>"/>

<c:set var="jobExecutionStatuses" value="<%=JobExecutionStatus.values()%>"/>

<c:set var="savedJob" value="${jobExecutionHistoryCleanupJobModel.job}"/>

<tags:page pageModel="${jobExecutionHistoryCleanupJobModel.pageModel}">

	<admin:jobEditData jobModel="${jobExecutionHistoryCleanupJobModel}">

		<jsp:attribute name="jobForm">

			<table:table border="0" width="1000">

				<table:tr>
					<table:td colspan="2">
						<admin:saveJobButton jobModel="${jobExecutionHistoryCleanupJobModel}"/>
					</table:td>
				</table:tr>

				<table:separatorInfo colspan="2" title="${eco:translate('Delete activities older then')}"/>

				<table:tr>
					<table:tdtext text_t="Delete entries that finished early then" isMandatory="true"/>

					<table:tddata>
						<tags:inputHint inputId="${leave_activity_for_days_control}" focused="true" hintTitle_t="Leave entries older then"
										hint="${eco:translate('Delete entries older then')}">
						<jsp:attribute name="inputField">
							<form:input path="${leave_activity_for_days_control}" cssErrorClass="invalid" size="4"/> ${eco:translate('day(s) ago')}
						</jsp:attribute>
						</tags:inputHint>
					</table:tddata>

				</table:tr>

				<table:tr>
					<table:tdtext text_t="Delete entries with statuses"/>

					<table:tddata>
						<js:checkboxMassChecker checkboxClass="${job_execution_status_ids_to_delete_control}"/>
						<br/>
						<br/>
						<form:checkboxes path="${job_execution_status_ids_to_delete_control}" items="${jobExecutionStatuses}" itemValue="id" itemLabel="name"
										 delimiter="<br />" cssErrorClass="invalid"/>
					</table:tddata>

				</table:tr>

			</table:table>

		</jsp:attribute>

	</admin:jobEditData>

</tags:page>