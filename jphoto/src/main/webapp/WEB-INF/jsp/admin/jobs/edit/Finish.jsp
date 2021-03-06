<%@ page import="com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus" %>
<%@ page import="com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryEntry" %>
<%@ page import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="jobs" tagdir="/WEB-INF/tags/jobs" %>

<jsp:useBean id="command" type="com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel" scope="request"/>

<%
    JobExecutionHistoryEntry jobExecutionHistoryEntry = command.getJobExecutionHistoryEntry();

    final JobExecutionStatus jobStatus = jobExecutionHistoryEntry.getJobExecutionStatus();
    final Date executionTime = ApplicationContextHelper.getDateUtilsService().getTimeBetween(jobExecutionHistoryEntry.getStartTime(), jobExecutionHistoryEntry.getEndTime());
%>
<c:set var="jobExecutionHistoryEntry" value="<%=jobExecutionHistoryEntry%>"/>

<c:set var="jobStatus" value="<%=jobStatus%>"/>
<c:set var="isSuccessful" value="<%=jobStatus.equals( JobExecutionStatus.DONE )%>"/>
<c:set var="isStopped" value="<%=jobStatus.equals( JobExecutionStatus.STOPPED_BY_USER )%>"/>
<c:set var="isStoppedOrErrorOrCancelled" value="<%=jobStatus.isStoppedOrErrorOrCancelled()%>"/>
<c:set var="executionTime" value="<%=executionTime%>"/>

<c:set var="errorMessage" value="${jobExecutionHistoryEntry.jobMessage}"/>

<c:set var="jobType" value="${jobExecutionHistoryEntry.savedJobType}"/>
<c:set var="jobTypeId" value="${jobType.id}"/>

<c:set var="current" value="${jobExecutionHistoryEntry.currentJobStep}"/>
<c:set var="total" value="${jobExecutionHistoryEntry.totalJobSteps}"/>

<c:set var="jobStatusNameTranslated" value="${eco:translate(jobStatus.name)}"/>

<tags:page pageModel="${command.pageModel}">

    <c:set var="job" value="${command.job}"/>

    <admin:jobListHeader jobListTab="${command.jobListTab}"
                         tabJobInfosMap="${command.tabJobInfosMap}"
                         activeJobs="${command.activeJobs}"
                         suppressAutoReloading="true"
    />

    <div style="display: inline-block; width: 500px; vertical-align: top; text-align: center">

        <h3>${eco:translate(jobType.name)}</h3>

        <a href="${eco:baseAdminUrl()}/jobs/${jobType.prefix}/">
            <html:img id="testDataGenerationFinishedImg" src="jobExecutionStatus/big/${jobStatus.icon}" width="128" height="128"
                      alt="${jobStatusNameTranslated}"/>
        </a>

        <br/>
        <br/>
        <h3>${jobStatusNameTranslated}</h3>
        <br/>
        <b>${eco:translate2('Job JSP: Performed $1 from $2', current, total)}</b>
        <br/>
        <br/>
        <b>${eco:formatDate(jobExecutionHistoryEntry.startTime)} &nbsp; ${eco:formatTime(jobExecutionHistoryEntry.startTime)}</b>
        &nbsp;&nbsp; - &nbsp;&nbsp; <b>${eco:formatDate(jobExecutionHistoryEntry.endTime)} &nbsp; ${eco:formatTime(jobExecutionHistoryEntry.endTime)}</b>
        <br/>
        <br/>
            ${eco:translate('Job JSP: Execution time')}: <b>${eco:formatTime(executionTime)}</b>

        <br/>
        <br/>


        <c:if test="${isStoppedOrErrorOrCancelled}">
            <br/>
            <br/>
            ${errorMessage}
        </c:if>

    </div>

    <div style="display: inline-block; width: 900px;">

        <h3>${eco:translate('Job JSP: Job parameters')}:</h3>
            ${jobExecutionHistoryEntry.jobParametersDescription}

    </div>

    <br/>
    <br/>

    <%--<jobs:jobExecutionLog jobId="${command.job.jobId}" />--%> <%-- TODO: it is empty now. Implement an uncomment --%>

</tags:page>
