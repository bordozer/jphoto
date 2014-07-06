<%@ tag import="admin.jobs.enums.SavedJobType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="jobs" tagdir="/WEB-INF/tags/jobs" %>

<%@ attribute name="savedJobs" type="java.util.List" required="true" %>
<%@ attribute name="notDeletableJobIds" type="java.util.Set" required="true" %>
<%@ attribute name="activeJobPercentageMap" type="java.util.Map" required="true" %>
<%@ attribute name="activeSavedJobIds" type="java.util.Set" required="true" %>
<%@ attribute name="jobListTab" type="admin.jobs.enums.JobListTab" required="true" %>
<%@ attribute name="selectedSavedJobTypeId" type="java.lang.Integer" required="true" %>

<c:set var="savedJobTypeValues" value="<%=SavedJobType.values()%>" />
<c:set var="colspan" value="6" />

<style type="text/css">
	.selectedJobType {

	}
</style>

<table:table width="90%" border="0">

	<table:tr>
		<table:tdunderlined colspan="${colspan}" cssClass="text-centered">
			<c:forEach var="savedJobType" items="${savedJobTypeValues}">

				<c:set var="cssClass" value=""/>
				<c:if test="${selectedSavedJobTypeId == savedJobType.id}">
					<c:set var="cssClass" value="block-border block-background block-shadow"/>
				</c:if>

				<a href="${eco:baseAdminUrl()}/jobs/${jobListTab.key}/${savedJobType.id}/" title="${eco:translate(savedJobType.name)}">
					<html:img32 src="jobtype/${savedJobType.icon}" cssClass="${cssClass}" />
				</a> &nbsp;&nbsp;&nbsp;&nbsp;
			</c:forEach>
		</table:tdunderlined>
	</table:tr>

	<c:forEach var="savedJob" items="${savedJobs}">

		<c:set var="savedJobId" value="${savedJob.id}" />

		<c:set var="job" value="${savedJob.job}"/>
		<c:set var="jobType" value="${job.jobType}"/>
		<c:set var="jobTypeId" value="${jobType.id}" />

		<c:set var="isActiveSavedJob" value="${eco:contains(activeSavedJobIds, savedJobId)}" />

		<table:tr>

			<table:tdicon>

				<c:if test="${isActiveSavedJob}">
					<html:spinningWheel16 title="${eco:translate('The job is executing')}" />
				</c:if>
			</table:tdicon>

			<table:tdicon>
				<c:set var="isUsedInScheduler" value="${eco:contains(notDeletableJobIds, savedJobId)}" />

				<c:if test="${not isUsedInScheduler and not isActiveSavedJob}">
					<a href="${eco:baseAdminUrl()}/jobs/${job.jobType.prefix}/${savedJobId}/delete/" title="${eco:translate1('Delete job \'$1\'', savedJob.name)}">
						<html:img16 src="delete16.png" onclick="return confirmDeletion( 'Delete \\'${savedJob.name}\\'?' );"/>
					</a>
				</c:if>

				<c:if test="${isUsedInScheduler or isActiveSavedJob}">
					<icons:canNotDelete hint="${eco:translate1('Job \\\'$1\\\' can not be deleted. It mush have been assigned to a scheduler task or is a parameter of another job', savedJob.name)}"/>
				</c:if>
			</table:tdicon>

			<table:td width="36">
				<html:img32 src="jobtype/${jobType.icon}" />
			</table:td>

			<table:td cssClass="${cssClass}">
				<c:if test="${not isActiveSavedJob}">
					<links:savedJobEdit savedJob="${savedJob}" />
				</c:if>

				<c:if test="${isActiveSavedJob}">
					<jobs:savedJobProgress savedJob="${savedJob}" jobId="${savedJob.job.jobId}" /> <%-- TODO: .Job.jobId is 0--%>
				</c:if>

				<br />
				${eco:translate(savedJob.jobType.name)}

				<c:if test="${isActiveSavedJob}">
					<c:set var="percentage" value="${activeJobPercentageMap[jobTypeId]}" />
					<br />
					<tags:progressSimple progressBarId="saved_job_progressbar_${savedJobId}" percentage="${percentage}" width="200" height="7" />
					<%--<tags:progress current="${percentage}" total="100" size="3" />--%>
				</c:if>

				<br />
				<c:if test="${not savedJob.active}">
					[ <span style="color: #AA0000">${eco:translate('Inactive')}</span> ]
				</c:if>
				<%--<tags:progress current="${100}" total="100" size="3" />--%>
			</table:td>

			<table:td cssClass="jobInactive" width="400">
				${job.jobParametersDescription}
			</table:td>

			<table:separator colspan="${colspan}" />

		</table:tr>

	</c:forEach>

</table:table>