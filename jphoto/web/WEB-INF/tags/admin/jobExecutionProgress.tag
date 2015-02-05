<%@ tag import="admin.controllers.jobs.list.SavedJobListController" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="jobHistoryEntryDTO" type="admin.controllers.jobs.list.JobHistoryEntryDTO" required="true" %>
<%@ attribute name="callback" type="java.lang.String" required="false" %>

<c:set var="jobHistoryEntryId" value="${jobHistoryEntryDTO.jobHistoryEntryId}" />
<c:set var="percentage" value="${jobHistoryEntryDTO.percentage}" />

<c:set var="jobProgressInterval" value="<%=SavedJobListController.JOB_PROGRESS_INTERVAL%>"/>

<script type="text/javascript">
	function emptyCallback() {}
</script>

<c:if test="${empty callback}">
	<c:set var="callback" value="emptyCallback" />
</c:if>

<tags:progressSimple progressBarId="progressbar_${jobHistoryEntryId}" percentage="${percentage}" width="300" height="7"/>

<div class="row">
	<div class="col-lg-12" id="progressStatusFullDescription_${jobHistoryEntryId}" style="font-size: 10px;"></div>
</div>

<script type="text/javascript">
	require( [ 'jquery', '/admin/js/job-execution-progress.js' ], function ( $, progress ) {



		var interval = ${jobProgressInterval};
		setTimeout( function () {
			progress.updateProgress( ${jobHistoryEntryId}, interval, ${callback} );

		}, interval );
	} );
</script>