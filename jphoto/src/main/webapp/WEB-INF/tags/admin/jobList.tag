<%@ tag import="com.bordozer.jphoto.admin.jobs.enums.SavedJobType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="jobs" tagdir="/WEB-INF/tags/jobs" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ attribute name="savedJobs" type="java.util.List" required="true" %>
<%@ attribute name="notDeletableJobIds" type="java.util.Set" required="true" %>
<%@ attribute name="activeJobHistoryMap" type="java.util.Map" required="true" %>
<%@ attribute name="activeSavedJobIds" type="java.util.Set" required="true" %>
<%@ attribute name="jobListTab" type="com.bordozer.jphoto.admin.jobs.enums.JobListTab" required="true" %>
<%@ attribute name="selectedSavedJobTypeId" type="java.lang.Integer" required="true" %>

<c:set var="savedJobTypeValues" value="<%=SavedJobType.values()%>"/>
<c:set var="colspan" value="6"/>

<div class="row  row-bottom-padding-10">
    <div class="col-lg-12">
        <ul class="nav nav-tabs">
            <c:forEach var="savedJobType" items="${savedJobTypeValues}">

                <c:set var="isSelectedTab" value="${selectedSavedJobTypeId == savedJobType.id}"/>
                <li class="${isSelectedTab ? "active" : ""}">
                    <a href="${eco:baseAdminUrl()}/jobs/${jobListTab.key}/${savedJobType.id}/">
                        <html:img32 src="jobtype/${savedJobType.icon}"
                                    alt="${eco:translate( 'Job list: filter by task type')} '${eco:translate(savedJobType.name)}'"/>
                    </a>
                </li>

            </c:forEach>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">

        <c:set var="counter" value="1"/>

        <div class="row">

            <c:forEach var="savedJob" items="${savedJobs}">

            <c:set var="savedJobId" value="${savedJob.id}"/>

            <c:set var="job" value="${savedJob.job}"/>
            <c:set var="jobType" value="${job.jobType}"/>
            <c:set var="jobTypeId" value="${jobType.id}"/>

            <c:set var="isActiveSavedJob" value="${eco:contains(activeSavedJobIds, savedJobId)}"/>

            <c:if test="${counter == 4}">
        </div>
        <div class="row">

            <c:set var="counter" value="1"/>
            </c:if>

            <div class="col-lg-4">

                <div class="panel panel-${isActiveSavedJob ? 'warning' : 'info'}">

                    <div class="panel-heading" style="height: 50px;">

                        <div class="col-lg-1" style="margin-right: 5px;">
                            <html:img32 src="jobtype/${jobType.icon}"/>
                        </div>

                        <div class="col-lg-9">

                            <div class="row">
                                <div class="col-lg-12">
                                    <h3 class="panel-title">
                                        <c:if test="${not isActiveSavedJob}">
                                            <links:savedJobEdit savedJob="${savedJob}"/>
                                        </c:if>

                                        <c:if test="${isActiveSavedJob}">
                                            <jobs:savedJobProgress savedJob="${savedJob}" jobId="${savedJob.job.jobId}"/> <%-- TODO: .Job.jobId is 0--%>
                                        </c:if>
                                    </h3>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12 panel-title">
                                    <small>${eco:translate(savedJob.jobType.name)}</small>
                                </div>
                            </div>

                        </div>

                        <div class="col-lg-1">
                            <c:set var="isUsedInScheduler" value="${eco:contains(notDeletableJobIds, savedJobId)}"/>

                            <c:if test="${not isUsedInScheduler and not isActiveSavedJob}">
                                <a href="${eco:baseAdminUrl()}/jobs/${job.jobType.prefix}/${savedJobId}/delete/"
                                   title="${eco:translate1('Delete job \'$1\'', savedJob.name)}">
                                    <html:img16 src="delete16.png" onclick="return confirmDeletion( 'Delete \\'${savedJob.name}\\'?' );"/>
                                </a>
                            </c:if>

                            <c:if test="${isUsedInScheduler or isActiveSavedJob}">
                                <icons:canNotDelete
                                        hint="${eco:translate1('Job \\\'$1\\\' can not be deleted. It mush have been assigned to a scheduler task or is a parameter of another job', savedJob.name)}"/>
                            </c:if>
                        </div>
                    </div>

                    <div class="panel-body">

                        <div class="col-lg-12">

                            <div class="row">

                                <div class="col-lg-12">
                                    <small>${job.jobParametersDescription}</small>
                                </div>

                                <c:if test="${not savedJob.active}">
                                    <div class="col-lg-12">
                                        <span style="color: #AA0000">${eco:translate('Inactive')}</span>
                                    </div>
                                </c:if>

                            </div>

                            <c:if test="${isActiveSavedJob}">

                                <div class="row">
                                    <div class="col-lg-12">
                                        <admin:jobExecutionProgress jobHistoryEntryDTO="${activeJobHistoryMap[savedJob.job.jobId]}"/>
                                    </div>
                                </div>
                            </c:if>

                        </div>
                    </div>
                </div>

            </div>

            <c:set var="counter" value="${counter + 1}"/>

            </c:forEach>
        </div>
    </div>
</div>
