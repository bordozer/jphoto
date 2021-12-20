<%@ tag import="com.bordozer.jphoto.admin.controllers.jobs.list.SavedJobListController" %>
<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="jobs" tagdir="/WEB-INF/tags/jobs" %>

<%@ attribute name="jobHistoryEntryDTO" type="com.bordozer.jphoto.admin.controllers.jobs.list.JobHistoryEntryDTO" required="true" %>
<%@ attribute name="callback" type="java.lang.String" required="false" %>

<c:set var="jobHistoryEntryId" value="${jobHistoryEntryDTO.jobHistoryEntryId}"/>
<c:set var="percentage" value="${jobHistoryEntryDTO.percentage}"/>

<c:set var="jobProgressInterval" value="<%=SavedJobListController.JOB_PROGRESS_INTERVAL%>"/>

<c:if test="${empty jobHistoryEntryDTO.savedJob}">
    <c:set var="linkToJob" value="${eco:baseAdminUrl()}/jobs/${jobHistoryEntryDTO.jobType.prefix}/progress/${jobHistoryEntryDTO.jobHistoryEntryId}/"/>
</c:if>

<c:if test="${not empty jobHistoryEntryDTO.savedJob}">
    <c:set var="linkToJob"
           value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminSavedJobProgressLink( jobHistoryEntryDTO.getJobType(), jobHistoryEntryDTO.getJobHistoryEntryId() )%>"/>
</c:if>

<script type="text/javascript">
    function emptyCallback() {
    }
</script>

<c:if test="${empty callback}">
    <c:set var="callback" value="emptyCallback"/>
</c:if>

<tags:progressSimple progressBarId="progressbar_${jobHistoryEntryId}" percentage="${percentage}" width="300" height="7"/>

<div class="row">
    <a href="${linkToJob}">
        <div class="col-lg-12" id="progressStatusFullDescription_${jobHistoryEntryId}" style="font-size: 10px;"></div>
    </a>
</div>

<script type="text/javascript">
    require(['jquery', '/admin/js/job-execution-progress.js'], function ($, progress) {
        var interval = ${jobProgressInterval};
        setTimeout(function () {
            progress.updateProgress(${jobHistoryEntryId}, interval, ${callback});

        }, interval);
    });
</script>
