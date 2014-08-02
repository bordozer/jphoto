<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="admin.services.jobs.JobExecutionService" %>
<%@ page import="admin.controllers.jobs.list.SavedJobListController" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
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
<c:set var="jobProgressInterval" value="<%=SavedJobListController.JOB_PROGRESS_INTERVAL%>"/>
<c:set var="id" value="${jobExecutionHistoryEntry.id}"/>

<tags:page pageModel="${command.pageModel}">

	<admin:jobListHeader jobListTab="${command.jobListTab}"
						 tabJobInfosMap="${command.tabJobInfosMap}"
						 activeJobs="${command.activeJobs}"
						 suppressAutoReloading="true"
		/>

	<script type="text/javascript">
		require( [ 'jquery' ], function( $ ) {
			$( function () {
				$( "#progressbar_${id}" ).progressbar( {
					 value:${percentage}
				 } );
			} );
		} );
	</script>

	<c:set var="jobType" value="${jobExecutionHistoryEntry.savedJobType}" />
	<c:set var="jobTypeId" value="${jobType.id}" />

	<c:set var="jobTypeNameTranslated" value="${eco:translate(jobType.name)}"/>

	<div style="display: inline-block; width: 600px; vertical-align: top;">

		<div style="float: left; font-size: x-large; margin: 20px; width: 90%;">
			<div style="float: left; width: 32px; margin-right: 10px;"><html:img32 src="jobtype/${jobType.icon}" alt="${jobTypeNameTranslated}" /></div>
			${jobTypeNameTranslated}
		</div>

		${eco:translate('Job JSP: Job type')} :${jobTypeNameTranslated}
		<br />
		${eco:translate('Job JSP: Start time')}: <b>${eco:formatDate(jobExecutionHistoryEntry.startTime)} ${eco:formatTime(jobExecutionHistoryEntry.startTime)}</b>

		<br />
		<br />

		<b>${eco:translate('Job JSP: Job progress')}:
			<span id="progressStatusFullDescription_${id}"></span>
		</b>

		<br />
		<br />
		<div id="progressbar_${jobExecutionHistoryEntry.id}" style="width: 400px; height: 7px; text-align: center;"></div>

	</div>

	<div style="display: inline-block; width: 600px;">
		<h3>${eco:translate('Job JSP: Job parameters')}:</h3>
		${jobExecutionHistoryEntry.jobParametersDescription}

		<br />

		<html:submitButton id="buttomStopJob" caption_t="Job JSP: Stop the Job button title" onclick="stopTheJob();" />
	</div>

	<div style="padding-top: 20px;">

	<jobs:jobExecutionLog jobId="${command.job.jobId}" />

	</div>

	<script type="text/javascript">

		require( [ 'jquery', '/admin/js/job-execution-progress.js' ], function( $, progress ) {

			var interval = ${jobProgressInterval};
			var updateJobExecutionIFrameUpdateInterval = 6000;

			setTimeout( function () {
				progress.updateProgress( ${jobExecutionHistoryEntry.id}, interval, jsonRPC, updatePageTitle );

			}, interval );

			function updatePageTitle( percentage ) {
				document.title = "${eco:projectName()} / ${jobExecutionHistoryEntry.id} / " + percentage + "%";
			}

			setTimeout( function () {
				updateJobExecutionIFrame();
			}, updateJobExecutionIFrameUpdateInterval );

			function updateJobExecutionIFrame() {
				var jobExecutionLogIFrame = $( '#jobExecutionLogIFrame' );
				jobExecutionLogIFrame.attr( "src", jobExecutionLogIFrame.attr( "src" ) );

				setTimeout( function () {
					updateJobExecutionIFrame();
				}, updateJobExecutionIFrameUpdateInterval );
			}

		});

		function stopTheJob() {
			if ( confirm( "${eco:translate('Job JSP: Stop the job?')}" ) ) {
				document.location.href = "${eco:baseAdminUrl()}/jobs/${jobType.prefix}/stop/${jobExecutionHistoryEntry.id}/";
			}
		}

	</script>

	<div class="footerseparator"></div>

</tags:page>