<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ page import="core.context.ApplicationContextHelper" %>
<%@ page import="admin.services.jobs.JobExecutionService" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="jobs" tagdir="/WEB-INF/tags/jobs" %>

<jsp:useBean id="command" type="admin.controllers.jobs.edit.AbstractAdminJobModel" scope="request"/>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "jobExecutionService", ApplicationContextHelper.<JobExecutionService>getBean( JobExecutionService.BEAN_NAME ) );
%>

<c:set var="jobExecutionHistoryEntry" value="${command.jobExecutionHistoryEntry}" />

<c:set var="current" value="${jobExecutionHistoryEntry.currentJobStep}" />
<c:set var="total" value="${jobExecutionHistoryEntry.totalJobSteps}" />
<c:set var="percentage" value="${eco:floor( 100 * current / total )}" />

<tags:page pageModel="${command.pageModel}">

	<admin:jobListHeader jobListTab="${command.jobListTab}"
						 tabJobInfosMap="${command.tabJobInfosMap}"
						 activeJobs="${command.activeJobs}"
						 suppressAutoReloading="true"
		/>

	<script type="text/javascript">

		$( function () {
			$( "#progressbar" ).progressbar( {
												 value:${percentage}
											 } );
		} );
	</script>

	<c:set var="jobType" value="${jobExecutionHistoryEntry.savedJobType}" />
	<c:set var="jobTypeId" value="${jobType.id}" />

	<c:set var="calculatingText" value="${eco:translate('Calculating...')}" />

	<div style="float: left; font-size: x-large; margin: 20px; width: 90%;">
		<div style="float: left; width: 32px; margin-right: 10px;"><html:img32 src="jobtype/${jobType.icon}" alt="${jobType.nameTranslated}" /></div>
		${jobType.nameTranslated}
	</div>

	<div style="float: left; width: 100%;">
	${eco:translate('Job type:')} ${jobType.nameTranslated}
	<br />
	${eco:translate('Start time:')} <b>${eco:formatDate(jobExecutionHistoryEntry.startTime)} ${eco:formatTime(jobExecutionHistoryEntry.startTime)}</b>

	<br />
	<br />

	<b>${eco:translate('Job progress:')}
		<span id="currentJobProgressId">${current}</span>
		${eco:translate('of')} <span id="totalStepsDivId">${total > 0 ? total : calculatingText}</span>
		- <span id="percentageJobProgressId">${percentage}%, ${eco:formatTime(jobExecutionHistoryEntry.executionDuration)}</span>
	</b>

	<br />
	<br />
	<div id="progressbar" style="width: 400px; height: 7px; text-align: center;"></div>

	<h3>${eco:translate('Job parameters:')}</h3>
	${jobExecutionHistoryEntry.jobParametersDescription}

	<br />

	<html:submitButton id="buttomStopJob" caption_t="Stop the Job" onclick="stopTheJob();" />

	<br />
	<br />

	<jobs:jobExecutionLog jobId="${command.job.jobId}" />

	</div>

	<script type="text/javascript">
		var interval = 2000;
		var updateJobExecutionIFrameUpdateInterval = 6000;

		setTimeout( function() {
			updateProgress( jsonRPC );
		}, interval );

		setTimeout( function() {
			updateJobExecutionIFrame();
		}, updateJobExecutionIFrameUpdateInterval );

		function updateProgress( jsonRPC ) {
			var jobProgressDTO = jsonRPC.jobExecutionService.getJobProgressAjax( ${jobExecutionHistoryEntry.id} );
			var current = jobProgressDTO.current;
			var total = jobProgressDTO.total;
			var jobStatusId = jobProgressDTO.jobStatusId;
			var isJobActive = jobProgressDTO.jobActive;
			var jobExecutionDuration = jobProgressDTO.jobExecutionDuration;

			if ( ! isJobActive ) {
				document.location.reload();
				return;
			}
			var percentage = Math.floor( 100 * parseInt( current ) / parseInt( total ) );

			$( '#totalStepsDivId' ).text( total > 0 ? total : "${calculatingText}" );
			$( '#currentJobProgressId' ).text( current );
			$( '#percentageJobProgressId' ).text( percentage + '%, ' + jobExecutionDuration );

			$( "#progressbar" ).progressbar( {
												 value:percentage
											 } );

			setTimeout( function() {
				updateProgress( jsonRPC );
			}, interval );

		}

		function updateJobExecutionIFrame() {
			var jobExecutionLogIFrame = $( '#jobExecutionLogIFrame' );
			jobExecutionLogIFrame.attr( "src", jobExecutionLogIFrame.attr( "src" ) );

			setTimeout( function() {
				updateJobExecutionIFrame();
			}, updateJobExecutionIFrameUpdateInterval );
		}

		function stopTheJob() {
			if ( confirm( "${eco:translate('Stop the job?')}" )) {
				document.location.href = "${eco:baseAdminUrlWithPrefix()}/jobs/${jobType.prefix}/stop/${jobExecutionHistoryEntry.id}/";
			}
		}

	</script>

</tags:page>