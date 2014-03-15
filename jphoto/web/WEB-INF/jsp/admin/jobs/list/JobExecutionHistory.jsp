<%@ page import="admin.jobs.enums.JobExecutionStatus" %>
<%@ page import="admin.jobs.enums.SavedJobType" %>
<%@ page import="admin.controllers.jobs.list.SavedJobListController" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="jobs" tagdir="/WEB-INF/tags/jobs" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="savedJobListModel" type="admin.controllers.jobs.list.SavedJobListModel" scope="request"/>

<c:set var="jobExecutionStatusValues" value="<%=JobExecutionStatus.values()%>"/>
<c:set var="jobExecutionStatusValuesLength" value="<%=JobExecutionStatus.values().length%>"/>

<c:set var="savedJobTypeValues" value="<%=SavedJobType.values()%>"/>
<c:set var="savedJobTypeValuesLength" value="<%=SavedJobType.values().length%>"/>
<c:set var="jobProgressInterval" value="<%=SavedJobListController.JOB_PROGRESS_INTERVAL%>"/>

<tags:page pageModel="${savedJobListModel.pageModel}">

<script type="text/javascript" src="<c:url value="/common/js/jobProgress.js" />"></script>

<script type="text/javascript">
	var interval = ${jobProgressInterval};
</script>

<style type="text/css">
	tr.chainJob td {
		font-weight: bold;
	}

	.jobHistoryToolbar {
		float: left;
		width: 100%;
		height: 44px;
		text-align: center;
		margin: 10px;
		/*border: dotted;*/
	}

	.jobHistoryButton {
		float: left;
		width: 46px;
		text-align: center;
		/*border: dashed;*/
	}

	. wdth {
		width: 20px;
	}
</style>

<admin:jobListHeader jobListTab="${savedJobListModel.jobListTab}"
					 tabJobInfosMap="${savedJobListModel.tabJobInfosMap}"
					 activeJobs="${savedJobListModel.activeJobs}"/>

<c:set var="jobExecutionHistoryDatas" value="${savedJobListModel.jobExecutionHistoryDatas}"/>
<c:set var="activeJobPercentageMap" value="${savedJobListModel.activeJobPercentageMap}"/>

<c:set var="jobExecutionStatusIdFilter" value="${savedJobListModel.jobExecutionStatusIdFilter}"/>
<c:set var="jobExecutionStatusIdUrlFilter" value=""/>
<c:if test="${jobExecutionStatusIdFilter > 0}">
	<c:set var="jobExecutionStatusIdUrlFilter" value="status/${jobExecutionStatusIdFilter}/"/>
</c:if>

<c:set var="jobTypeIdFilter" value="${savedJobListModel.jobTypeIdFilter}"/>
<c:set var="jobTypeIdUrlFilter" value=""/>
<c:if test="${jobTypeIdFilter > 0}">
	<c:set var="jobTypeIdUrlFilter" value="type/${jobTypeIdFilter}/"/>
</c:if>

<c:set var="selectedIconCss" value="block-border block-background block-shadow"/>
<c:set var="colspan" value="8"/>

<div class="jobHistoryToolbar">
	<div class="jobHistoryButton <c:if test="${jobExecutionStatusIdFilter == 0}">${selectedIconCss}</c:if>">
		<a href="${eco:baseAdminUrlWithPrefix()}/jobs/done/${jobTypeIdUrlFilter}">
			<html:img32 src="jobExecutionStatus/all.png" alt="${eco:translate('Reset filter by execution status')}"/>
		</a>
	</div>

	<c:forEach var="jobExecutionStatus" items="${jobExecutionStatusValues}">
		<div class="jobHistoryButton <c:if test="${jobExecutionStatusIdFilter == jobExecutionStatus.id}">${selectedIconCss}</c:if>">
			<a href="${eco:baseAdminUrlWithPrefix()}/jobs/done/status/${jobExecutionStatus.id}/${jobTypeIdUrlFilter}">
				<html:img32 src="jobExecutionStatus/${jobExecutionStatus.icon}" alt="${eco:translate(jobExecutionStatus.name)}"/>
			</a>
		</div>
	</c:forEach>

	<div class="jobHistoryButton wdth">&nbsp;</div>
	<div class="jobHistoryButton wdth">
		<a href="${eco:baseAdminUrlWithPrefix()}/jobs/done/">
			<html:img32 src="jobExecutionStatus/allEntries.png" alt="${eco:translate('Reset all filters')}"/>
		</a>
	</div>
	<div class="jobHistoryButton wdth">&nbsp;</div>

	<div class="jobHistoryButton <c:if test="${jobTypeIdFilter == 0}">${selectedIconCss}</c:if>">
		<a href="${eco:baseAdminUrlWithPrefix()}/jobs/done/${jobExecutionStatusIdUrlFilter}">
			<html:img32 src="jobExecutionStatus/all.png" alt="${eco:translate('Reset filter by job type')}"/>
		</a>
	</div>

	<c:forEach var="savedJobType" items="${savedJobTypeValues}">
		<div class="jobHistoryButton <c:if test="${jobTypeIdFilter == savedJobType.id}">${selectedIconCss}</c:if>">
			<a href="${eco:baseAdminUrlWithPrefix()}/jobs/done/${jobExecutionStatusIdUrlFilter}type/${savedJobType.id}/" title="${eco:translate(savedJobType.name)}">
				<html:img32 src="jobtype/${savedJobType.icon}"/>
			</a>
		</div>
	</c:forEach>

</div>

<tags:paging showSummary="false"/>

<form:form modelAttribute="savedJobListModel" action="${eco:baseAdminUrlWithPrefix()}/jobs/delete/" method="POST">

	<form:hidden path="formAction"/>

	<div style="float: left; width: 100%; text-align: center; margin: 10px;">
			${eco:translate('Filter by scheduler task')}:
		<form:select path="schedulerTaskId" onchange="filterBySchedulerTask();">
			<form:option value="0" label=""/>
			<form:options itemValue="id" itemLabel="name" items="${savedJobListModel.schedulerTasks}"/>
		</form:select>
	</div>

	<div style="float: left; width: 100%;">

		<table:table width="90%">

			<jsp:attribute name="thead">
				<table:tdicon>
					<js:checkBoxChecker namePrefix="selectedJobsIds"/>
				</table:tdicon>

				<table:tdicon/>
				<table:tdicon>${eco:translate( "id" )}</table:tdicon>
				<table:td width="50">${eco:translate( "Saved job id" )}</table:td>
				<table:tdicon/>
				<table:td>${eco:translate( "Start time" )}</table:td>
				<table:td>${eco:translate( "Finish time" )}</table:td>
				<table:td>${eco:translate( "Job" )}</table:td>
				<table:th text_t="State" width="20"/>
				<table:th text_t="Scheduler task" width="20"/>

			</jsp:attribute>

			<jsp:body>

				<c:forEach var="jobExecutionHistoryData" items="${jobExecutionHistoryDatas}">

					<c:set var="jobExecutionHistoryEntry" value="${jobExecutionHistoryData.jobExecutionHistoryEntry}"/>
					<c:set var="jobMessage" value=""/>

					<c:set var="jobEntryId" value="${jobExecutionHistoryEntry.id}"/>
					<c:set var="jobExecutionStatus" value="${jobExecutionHistoryEntry.jobExecutionStatus}"/>
					<c:set var="isJobWatingForStartOrInProgress" value="${jobExecutionStatus.active}"/>
					<c:set var="isJobFinishedWithAnyResult" value="${jobExecutionStatus.notActive}"/>
					<c:set var="jobType" value="${jobExecutionHistoryEntry.savedJobType}"/>
					<c:set var="jobTypeId" value="${jobType.id}"/>

					<table:tr cssClass="${jobExecutionHistoryEntry.chainJob ? 'chainJob' : ''}">
						<table:tdicon>
							<form:checkbox path="selectedJobsIds" value="${jobEntryId}"/>
						</table:tdicon>

						<table:tdicon>
							<html:img16 src="delete16.png" onclick="deleteJobExecutionHistoryEntry( ${jobEntryId} ); return false;"/>
						</table:tdicon>

						<table:tdicon>${jobEntryId}</table:tdicon>

						<table:td cssClass="textcentered">
							<c:if test="${not empty jobExecutionHistoryEntry.savedJob}">
								${jobExecutionHistoryEntry.savedJob.id}
							</c:if>
						</table:td>

						<table:tdicon>
							<html:img16 src="jobtype/${jobExecutionHistoryEntry.savedJobType.icon}" alt="${eco:translate(jobExecutionHistoryEntry.savedJobType.name)}"/>
						</table:tdicon>

						<table:tdunderlined width="150">
							${eco:formatDate(jobExecutionHistoryEntry.startTime)} ${eco:formatTime(jobExecutionHistoryEntry.startTime)}
						</table:tdunderlined>

						<table:tdunderlined width="150">
							<c:if test="${jobExecutionHistoryEntry.endTime.time > 0}">
								${eco:formatDate(jobExecutionHistoryEntry.endTime)} ${eco:formatTime(jobExecutionHistoryEntry.endTime)}
							</c:if>
						</table:tdunderlined>

						<%-- Job reference  --%>
						<table:tdunderlined>
							<c:if test="${isJobWatingForStartOrInProgress}">
								<c:if test="${empty jobExecutionHistoryEntry.savedJob}">
									<a href="${eco:baseAdminUrlWithPrefix()}/jobs/${jobType.prefix}/progress/${jobEntryId}/">${eco:translate(jobExecutionHistoryEntry.savedJobType.name)}</a>
								</c:if>
								<c:if test="${not empty jobExecutionHistoryEntry.savedJob}">
									<jobs:savedJobProgress savedJob="${jobExecutionHistoryEntry.savedJob}" jobId="${jobEntryId}"/>
								</c:if>
							</c:if>

							<c:if test="${isJobFinishedWithAnyResult}">
								<c:if test="${empty jobExecutionHistoryEntry.savedJob}">
									${eco:translate(jobExecutionHistoryEntry.savedJobType.name)}
								</c:if>
								<c:if test="${not empty jobExecutionHistoryEntry.savedJob}">
									<links:savedJobEdit savedJob="${jobExecutionHistoryEntry.savedJob}"/>
								</c:if>
							</c:if>
						</table:tdunderlined>
						<%-- / Job reference  --%>

						<%-- Job state icon --%>
						<c:set var="resultCssClass" value="textcentered"/>
						<c:if test="${jobExecutionStatus == 'IN_PROGRESS'}">
							<c:set var="resultCssClass" value=""/>
						</c:if>
						<table:tdunderlined cssClass="${resultCssClass}">

							<c:if test="${jobExecutionStatus == 'WAITING_FOR_START'}">
								<c:set var="watingMessage" value="${eco:translate('The job is waiting for start')}"/>
								<html:img16 src="jobExecutionStatus/hourglass.png" alt="${eco:translate('Is waiting for notification from the parent job to run')}"
											onclick="alert( '${watingMessage}' );"/>
							</c:if>

							<c:if test="${jobExecutionStatus == 'IN_PROGRESS'}">

								<c:set var="percentage" value="${activeJobPercentageMap[jobEntryId]}"/>

								<c:set var="progressColor" value=""/>
								<c:if test="${jobType == 'JOB_CHAIN'}">
									<c:set var="progressColor" value="#669966"/>
								</c:if>

								<tags:progressSimple progressBarId="progressbar_${jobEntryId}" percentage="${percentage}" width="200" height="7" color="${progressColor}"/>

								<span id="progressStatusFullDescription_${jobEntryId}" style="font-size: 10px;"></span>

								<script type="text/javascript">
									setTimeout( function () {
										updateProgress( ${jobExecutionHistoryEntry.id} );
									}, interval );
								</script>
							</c:if>

							<c:if test="${isJobFinishedWithAnyResult}">
								<html:img16 src="jobExecutionStatus/${jobExecutionStatus.icon}" alt="${eco:translate(jobExecutionStatus.name)}" onclick="showJobMessage( ${jobEntryId} );"/>

								<c:set var="bgColor" value="#CCE6FF"/>
								<c:set var="fontColor" value="navy"/>
								<c:set var="icon" value="info32.png"/>

								<c:if test="${jobExecutionStatus == 'ERROR'}">
									<c:set var="bgColor" value="#ffe4e1"/>
									<c:set var="fontColor" value="#AA0000"/>
									<c:set var="icon" value="error32.png"/>
								</c:if>

								<c:set var="jobInfoTitle" value="<b>${eco:translate(jobExecutionHistoryEntry.savedJobType.name)}</b><br /><br />"/>
								<c:set var="parametersDescription" value="${jobExecutionHistoryEntry.parametersDescription}<br /><br />"/>
								<c:if test="${not empty jobExecutionHistoryEntry.jobMessage}">
									<c:set var="jobMessage" value="${jobExecutionHistoryEntry.jobMessage}<br /><br />"/>
								</c:if>
								<c:set var="performedText"
									   value="${eco:translate2('Performed $1 from $2', jobExecutionHistoryEntry.currentJobStep, jobExecutionHistoryEntry.totalJobSteps)}"/>

								<c:set var="jobInformation" value="${jobInfoTitle}${parametersDescription}${jobMessage}${performedText}"/>

								<tags:message id="jobInfoDiv_${jobEntryId}" title_t="Job execution info" bgColor="${bgColor}" fontColor="${fontColor}" icon="${icon}"
											  messageText="${jobInformation}"/>

							</c:if>
						</table:tdunderlined>
						<%-- / Job state icon --%>

						<table:tdunderlined cssClass="textcentered">
							<c:set var="scheduledTaskId" value="${jobExecutionHistoryEntry.scheduledTaskId}"/>
							<c:set var="schedulerTask" value="${jobExecutionHistoryData.schedulerTask}"/>
							<c:if test="${scheduledTaskId > 0 and not empty schedulerTask}">
								<admin:schedulerTaskEdit schedulerTask="${schedulerTask}">
									<html:img16 src="scheduler/type/${schedulerTask.taskType.icon}"/>
								</admin:schedulerTaskEdit>
							</c:if>
							<c:if test="${scheduledTaskId > 0 and empty schedulerTask}">
								<html:img16 src="error16x16.png" alt="${eco:translate1('Scheduled task $1 is deleted', scheduledTaskId)}"
											onclick="showDeletedTaskInfo( ${scheduledTaskId} );"/>
							</c:if>
						</table:tdunderlined>

					</table:tr>

				</c:forEach>

				<c:if test="${not empty jobExecutionHistoryDatas}">
					<table:tr>
						<table:td colspan="${colspan}">
							<html:submitButton id="deleteEntries" caption_t="Delete selected history entries" onclick="return deleteSelectedHistoryEntries();"/>
						</table:td>
					</table:tr>
				</c:if>

			</jsp:body>

		</table:table>

	</div>

</form:form>

<tags:paging showSummary="true"/>

<script type="text/javascript">
	function showJobMessage( jobId ) {
		var divId = 'jobInfoDiv_' + jobId;
		var parameters = { closeClick:true, closeEsc:true, centered:true, showOverlay:true, onLoad:function () {
		} };
		showDiv( divId, parameters );
	}
</script>

<script type="text/javascript">
	function deleteJobExecutionHistoryEntry( jobEntryId ) {
		if ( confirm( "${eco:translate('Delete job execution history entry?')}" ) ) {
			document.location.href = "${eco:baseAdminUrlWithPrefix()}/jobs/delete/" + jobEntryId + "/";
		}
	}

	function deleteSelectedHistoryEntries() {
		$( '#savedJobListModel' ).attr( 'action', '${eco:baseAdminUrlWithPrefix()}/jobs/delete/' );
		return confirm( "${eco:translate('Delete selected history entries?')}" );
	}

	function filterBySchedulerTask() {
		$( '#formAction' ).val( 'filter' );
		$( '#savedJobListModel' ).attr( 'action', '${eco:baseAdminUrlWithPrefix()}/jobs/done/${jobExecutionStatusIdUrlFilter}${jobTypeIdUrlFilter}' ); // TODO: icon click ignore this filter
		$( '#savedJobListModel' ).submit();
	}

	function showDeletedTaskInfo( scheduledTaskId ) {
		showInformationMessageNoAutoClose( "${eco:translate('Scheduler task is not found. It mush have been deleted.')}" );
	}
</script>

<div class="footerseparator"></div>

</tags:page>