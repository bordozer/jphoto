<%@ page import="com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus" %>
<%@ page import="com.bordozer.jphoto.admin.jobs.enums.SavedJobType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="jobs" tagdir="/WEB-INF/tags/jobs" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="savedJobListModel" type="com.bordozer.jphoto.admin.controllers.jobs.list.SavedJobListModel" scope="request"/>

<c:set var="jobExecutionStatusValues" value="<%=JobExecutionStatus.values()%>"/>
<c:set var="jobExecutionStatusValuesLength" value="<%=JobExecutionStatus.values().length%>"/>

<c:set var="savedJobTypeValues" value="<%=SavedJobType.values()%>"/>
<c:set var="savedJobTypeValuesLength" value="<%=SavedJobType.values().length%>"/>

<tags:page pageModel="${savedJobListModel.pageModel}">

    <admin:jobListHeader jobListTab="${savedJobListModel.jobListTab}"
                         tabJobInfosMap="${savedJobListModel.tabJobInfosMap}"
                         activeJobs="${savedJobListModel.activeJobs}"/>

    <c:set var="jobExecutionHistoryDatas" value="${savedJobListModel.jobExecutionHistoryDatas}"/>
    <c:set var="activeJobHistoryMap" value="${savedJobListModel.activeJobHistoryMap}"/>

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

    <div class="row row-bottom-padding-10">

        <div class="col-lg-12">

            <ul class="nav nav-tabs">

                <c:set var="isSelectedTab" value="${jobExecutionStatusIdFilter == 0}"/>

                <li class="${isSelectedTab ? "active" : ""}">
                    <a href="${eco:baseAdminUrl()}/jobs/done/${jobTypeIdUrlFilter}">
                        <html:img32 src="jobExecutionStatus/all.png" alt="${eco:translate('JobExecutionHistory: Reset filter by execution status')}"/>
                    </a>
                </li>

                <c:forEach var="jobExecutionStatus" items="${jobExecutionStatusValues}">

                    <c:set var="isSelectedTab" value="${jobExecutionStatusIdFilter == jobExecutionStatus.id}"/>

                    <li class="${isSelectedTab ? "active" : ""}">
                        <a href="${eco:baseAdminUrl()}/jobs/done/status/${jobExecutionStatus.id}/${jobTypeIdUrlFilter}">
                            <c:set var="jobExecutionStatusNameTranslated" value="${eco:translate(jobExecutionStatus.name)}"/>
                            <html:img32 src="jobExecutionStatus/${jobExecutionStatus.icon}"
                                        alt="${eco:translate1('JobExecutionHistory: Filter by job status: $1', jobExecutionStatusNameTranslated)}"/>
                        </a>
                    </li>

                </c:forEach>

            </ul>
        </div>

    </div>

    <div class="row row-bottom-padding-10">

        <div class="col-lg-12">

            <ul class="nav nav-tabs">

                    <%--<div class="jobHistoryButton wdth">&nbsp;</div>
                    <div class="jobHistoryButton wdth">
                        <a href="${eco:baseAdminUrl()}/jobs/done/">
                            <html:img32 src="jobExecutionStatus/allEntries.png" alt="${eco:translate('JobExecutionHistory: Reset all filters')}"/>
                        </a>
                    </div>
                    <div class="jobHistoryButton wdth">&nbsp;</div>--%>

                <c:set var="isSelectedTab" value="${jobTypeIdFilter == 0}"/>

                <li class="${isSelectedTab ? "active" : ""}">
                    <a href="${eco:baseAdminUrl()}/jobs/done/${jobExecutionStatusIdUrlFilter}">
                        <html:img32 src="jobExecutionStatus/all.png" alt="${eco:translate('JobExecutionHistory: Reset filter by job type')}"/>
                    </a>
                </li>

                <c:forEach var="savedJobType" items="${savedJobTypeValues}">

                    <c:set var="isSelectedTab" value="${jobTypeIdFilter == savedJobType.id}"/>
                    <c:set var="savedJobTypeNameTranslated" value="${eco:translate(savedJobType.name)}"/>

                    <li class="${isSelectedTab ? "active" : ""}">
                        <c:set var="savedJobTypeNameTranslated" value="${eco:translate(savedJobType.name)}"/>
                        <a href="${eco:baseAdminUrl()}/jobs/done/${jobExecutionStatusIdUrlFilter}type/${savedJobType.id}/"
                           title="${eco:translate1('JobExecutionHistory: Filter by job type: $1', savedJobTypeNameTranslated)}">
                            <html:img32 src="jobtype/${savedJobType.icon}"/>
                        </a>
                    </li>

                </c:forEach>

            </ul>

        </div>

    </div>

    <tags:paging showSummary="false"/>

    <form:form modelAttribute="savedJobListModel" action="${eco:baseAdminUrl()}/jobs/delete/" method="POST">

        <form:hidden path="formAction"/>

        <div class="row row-bottom-padding-10">
            <div class="col-lg-12">
                    ${eco:translate('JobExecutionHistory: Filter by scheduler task')}:
                <form:select path="schedulerTaskId" onchange="filterBySchedulerTask();">
                    <form:option value="0" label=""/>
                    <form:options itemValue="id" itemLabel="name" items="${savedJobListModel.schedulerTasks}"/>
                </form:select>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">

                <table:table width="90%">

			<jsp:attribute name="thead">
				<table:tdicon>
					<js:checkboxMassChecker checkboxClass="selectedJobsIds"/>
				</table:tdicon>

				<table:tdicon/>
				<table:tdicon>${eco:translate( "id" )}</table:tdicon>
				<table:td width="50">${eco:translate( "JobExecutionHistory: Saved job id" )}</table:td>
				<table:tdicon/>
				<table:td>${eco:translate( "JobExecutionHistory: Start time" )}</table:td>
				<table:td>${eco:translate( "JobExecutionHistory: Finish time" )}</table:td>
				<table:td>${eco:translate( "JobExecutionHistory: Job name" )}</table:td>
				<table:th text_t="JobExecutionHistory: State" width="20"/>
				<table:th text_t="JobExecutionHistory: Scheduler task" width="20"/>

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
                                    <form:checkbox path="selectedJobsIds" value="${jobEntryId}" cssClass="selectedJobsIds"/>
                                </table:tdicon>

                                <table:tdicon>
                                    <html:img16 src="delete16.png" onclick="deleteJobExecutionHistoryEntry( ${jobEntryId} ); return false;"/>
                                </table:tdicon>

                                <table:tdicon>${jobEntryId}</table:tdicon>

                                <table:td cssClass="text-centered">
                                    <c:if test="${not empty jobExecutionHistoryEntry.savedJob}">
                                        ${jobExecutionHistoryEntry.savedJob.id}
                                    </c:if>
                                </table:td>

                                <table:tdicon>
                                    <html:img16 src="jobtype/${jobType.icon}" alt="${eco:translate(jobType.name)}"/>
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
                                            <a href="${eco:baseAdminUrl()}/jobs/${jobType.prefix}/progress/${jobEntryId}/">${eco:translate(jobType.name)}</a>
                                        </c:if>
                                        <c:if test="${not empty jobExecutionHistoryEntry.savedJob}">
                                            <jobs:savedJobProgress savedJob="${jobExecutionHistoryEntry.savedJob}" jobId="${jobEntryId}"/>
                                        </c:if>
                                    </c:if>

                                    <c:if test="${isJobFinishedWithAnyResult}">
                                        <c:if test="${empty jobExecutionHistoryEntry.savedJob}">
                                            <%--${eco:translate(jobType.name)}--%>
                                            <admin:jobTemplate savedJobType="${jobType}"/>
                                        </c:if>
                                        <c:if test="${not empty jobExecutionHistoryEntry.savedJob}">
                                            <links:savedJobEdit savedJob="${jobExecutionHistoryEntry.savedJob}"/>
                                        </c:if>
                                    </c:if>
                                </table:tdunderlined>
                                <%-- / Job reference  --%>

                                <%-- Job state icon --%>
                                <c:set var="resultCssClass" value="text-centered"/>
                                <c:if test="${jobExecutionStatus == 'IN_PROGRESS'}">
                                    <c:set var="resultCssClass" value=""/>
                                </c:if>
                                <table:tdunderlined cssClass="${resultCssClass}">

                                    <c:if test="${jobExecutionStatus == 'WAITING_FOR_START'}">
                                        <c:set var="watingMessage" value="${eco:translate('JobExecutionHistory: The job is waiting for start')}"/>
                                        <html:img16 src="jobExecutionStatus/hourglass.png"
                                                    alt="${eco:translate('JobExecutionHistory: Is waiting for notification from the parent job to run')}"
                                                    onclick="alert( '${watingMessage}' );"/>
                                    </c:if>

                                    <c:if test="${jobExecutionStatus == 'IN_PROGRESS'}">
                                        <admin:jobExecutionProgress jobHistoryEntryDTO="${activeJobHistoryMap[jobEntryId]}"/>
                                    </c:if>

                                    <c:if test="${isJobFinishedWithAnyResult}">

                                        <c:set var="jobInfoTitle" value="<b>${eco:translate(jobExecutionHistoryEntry.savedJobType.name)}</b><br /><br />"/>
                                        <c:set var="parametersDescription" value="${jobExecutionHistoryEntry.parametersDescription}<br /><br />"/>
                                        <c:if test="${not empty jobExecutionHistoryEntry.jobMessage}">
                                            <c:set var="jobMessage" value="${jobExecutionHistoryEntry.jobMessage}<br /><br />"/>
                                        </c:if>
                                        <c:set var="performedText"
                                               value="${eco:translate2('Job JSP: Performed $1 from $2', jobExecutionHistoryEntry.currentJobStep, jobExecutionHistoryEntry.totalJobSteps)}"/>

                                        <c:set var="jobInformation" value="${jobInfoTitle}${parametersDescription}${jobMessage}${performedText}"/>
                                        <html:img16 src="jobExecutionStatus/${jobExecutionStatus.icon}" alt="${eco:translate(jobExecutionStatus.name)}"
                                                    onclick="showJobMessage( ${jobEntryId} );"/> <%-- TODO: pass ${jobInformation} --%>
                                        <div id="job-execution-log-entry-${jobEntryId}" style="display: none;">${jobInformation}</div>

                                        <c:set var="bgColor" value="#CCE6FF"/>
                                        <c:set var="fontColor" value="navy"/>
                                        <c:set var="icon" value="info32.png"/>

                                        <c:if test="${jobExecutionStatus == 'ERROR'}">
                                            <c:set var="bgColor" value="#ffe4e1"/>
                                            <c:set var="fontColor" value="#AA0000"/>
                                            <c:set var="icon" value="error32.png"/>
                                        </c:if>

                                    </c:if>
                                </table:tdunderlined>
                                <%-- / Job state icon --%>

                                <table:tdunderlined cssClass="text-centered">
                                    <c:set var="scheduledTaskId" value="${jobExecutionHistoryEntry.scheduledTaskId}"/>
                                    <c:set var="schedulerTask" value="${jobExecutionHistoryData.schedulerTask}"/>
                                    <c:if test="${scheduledTaskId > 0 and not empty schedulerTask}">
                                        <admin:schedulerTaskEdit schedulerTask="${schedulerTask}">
                                            <html:img16 src="scheduler/type/${schedulerTask.taskType.icon}"/>
                                        </admin:schedulerTaskEdit>
                                    </c:if>
                                    <c:if test="${scheduledTaskId > 0 and empty schedulerTask}">
                                        <html:img16 src="error16x16.png"
                                                    alt="${eco:translate1('JobExecutionHistory: Scheduled task $1 is deleted', scheduledTaskId)}"
                                                    onclick="showDeletedTaskInfo( ${scheduledTaskId} );"/>
                                    </c:if>
                                </table:tdunderlined>

                            </table:tr>

                        </c:forEach>

                        <c:if test="${not empty jobExecutionHistoryDatas}">
                            <table:tr>
                                <table:td colspan="${colspan}">
                                    <html:submitButton id="deleteEntries" caption_t="JobExecutionHistory: Delete selected history entries"
                                                       onclick="return deleteSelectedHistoryEntries();"/>
                                </table:td>
                            </table:tr>
                        </c:if>

                    </jsp:body>

                </table:table>
            </div>

        </div>

    </form:form>

    <tags:paging showSummary="true"/>

    <script type="text/javascript">
        function showJobMessage(divId) {
            require(['jquery'], function ($) {
                showUIMessage_FromCustomDiv($("#job-execution-log-entry-" + divId));
            });
        }
    </script>

    <script type="text/javascript">
        function deleteJobExecutionHistoryEntry(jobEntryId) {
            if (confirm("${eco:translate('JobExecutionHistory: Delete job execution history entry?')}")) {
                document.location.href = "${eco:baseAdminUrl()}/jobs/delete/" + jobEntryId + "/";
            }
        }

        function deleteSelectedHistoryEntries() {
            $('#savedJobListModel').attr('action', '${eco:baseAdminUrl()}/jobs/delete/');
            return confirm("${eco:translate('JobExecutionHistory: Delete selected history entries?')}");
        }

        function filterBySchedulerTask() {
            $('#formAction').val('filter');
            $('#savedJobListModel').attr('action', '${eco:baseAdminUrl()}/jobs/done/${jobExecutionStatusIdUrlFilter}${jobTypeIdUrlFilter}'); // TODO: icon click ignore this filter
            $('#savedJobListModel').submit();
        }

        function showDeletedTaskInfo(scheduledTaskId) {
            showUIMessage_InformationMessage_ManualClosing("${eco:translate('JobExecutionHistory: Scheduler task is not found. It mush have been deleted.')}");
        }
    </script>

    <div class="footerseparator"></div>

</tags:page>
