<%@ tag import="admin.jobs.enums.JobListTab" %>
<%@ tag import="admin.jobs.enums.JobExecutionStatus" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="jobListTab" type="admin.jobs.enums.JobListTab" required="true" %>
<%@ attribute name="tabJobInfosMap" type="java.util.Map" required="true" %>
<%@ attribute name="activeJobs" type="java.util.List" required="true" %>
<%@ attribute name="suppressAutoReloading" type="java.lang.Boolean" required="false" %>

<c:set var="jobListTabValues" value="<%=JobListTab.values()%>" />
<c:set var="activeStatusId" value="<%=JobExecutionStatus.IN_PROGRESS.getId()%>"/>

<c:set var="tabWidthPercent" value="<%=( 100 / (float) JobListTab.values().length - 0.5f )%>"/>

<c:set var="activeJobsQty" value="${fn:length(activeJobs)}" />

<style type="text/css">
	.jobListHeaderTab {
		float: left;
		width: ${tabWidthPercent}%;
		padding-top: 5px;
		padding-bottom: 5px;
		text-align: center;
		height: 29px;
		border-radius: 12px 12px 0 0;
		font-size: 11px;
		/*border: dotted;*/
	}

</style>

<div class="tabHeader">

	<c:forEach var="jobListTabValue" items="${jobListTabValues}">

		<c:set var="isSelectedTab" value="${jobListTab == jobListTabValue}" />

		<div class="jobListHeaderTab block-border${isSelectedTab ? " block-background" : ""}">

			<c:set var="activeTabAndThereAreRunningJobs" value="${jobListTabValue == 'JOB_EXECUTION_HISTORY' and activeJobsQty > 0}" />

			<c:if test="${activeTabAndThereAreRunningJobs}">
				<html:spinningWheel16 title="${eco:translate('There are active jobs')}" />
			</c:if>

			<c:set var="jobListTabValueName" value="${eco:translate(jobListTabValue.name)}"/>
			<a href="${eco:baseAdminUrl()}/jobs/${jobListTabValue.key}/" title="${jobListTabValueName}">${jobListTabValueName}</a>

			<br />

			<c:if test="${activeTabAndThereAreRunningJobs}">
				<a href="${eco:baseAdminUrl()}/jobs/${jobListTabValue.key}/status/${activeStatusId}/" title="${eco:translate('Show active jobs')}">&nbsp;${activeJobsQty}&nbsp;</a> /
			</c:if>
			${tabJobInfosMap[jobListTabValue].tabJobsQty}

		</div>

	</c:forEach>

</div>

<c:set var="interval" value="60000" />
<c:if test="${activeJobsQty == 0}">
	<c:set var="interval" value="60000" />
</c:if>

<%--<c:if test="${not suppressAutoReloading}">
	<script type="text/javascript">
		require( [ 'jquery' ], function( $ ) {
			$( document ).ready( function() {
				setTimeout( function() {
					document.location.reload();
				}, ${interval} );
			});
		});
	</script>
</c:if>--%>
