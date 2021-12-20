<%@ tag import="com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus" %>
<%@ tag import="com.bordozer.jphoto.admin.jobs.enums.JobListTab" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="jobListTab" type="com.bordozer.jphoto.admin.jobs.enums.JobListTab" required="true" %>
<%@ attribute name="tabJobInfosMap" type="java.util.Map" required="true" %>
<%@ attribute name="activeJobs" type="java.util.List" required="true" %>
<%@ attribute name="suppressAutoReloading" type="java.lang.Boolean" required="false" %>

<c:set var="jobListTabValues" value="<%=JobListTab.values()%>"/>
<c:set var="activeStatusId" value="<%=JobExecutionStatus.IN_PROGRESS.getId()%>"/>

<c:set var="tabWidthPercent" value="<%=( 100 / (float) JobListTab.values().length - 0.5f )%>"/>

<c:set var="activeJobsQty" value="${fn:length(activeJobs)}"/>

<ul class="nav nav-tabs">

    <c:forEach var="jobListTabValue" items="${jobListTabValues}">

        <c:set var="isSelectedTab" value="${jobListTab == jobListTabValue}"/>

        <li class="${isSelectedTab ? "active" : ""} text-center">

            <c:set var="activeTabAndThereAreRunningJobs" value="${jobListTabValue == 'JOB_EXECUTION_HISTORY' and activeJobsQty > 0}"/>

            <c:if test="${activeTabAndThereAreRunningJobs}">
                <html:spinningWheel16 title="${eco:translate('There are active jobs')}"/>
            </c:if>

            <c:set var="jobListTabValueName" value="${eco:translate(jobListTabValue.name)}"/>
            <a href="${eco:baseAdminUrl()}/jobs/${jobListTabValue.key}/" title="${jobListTabValueName}">${jobListTabValueName}<br/>
                (${tabJobInfosMap[jobListTabValue].tabJobsQty})</a>

            <c:if test="${activeTabAndThereAreRunningJobs}">
                <a href="${eco:baseAdminUrl()}/jobs/${jobListTabValue.key}/status/${activeStatusId}/"
                   title="${eco:translate('jobListHeader: Show active jobs')}">&nbsp;${activeJobsQty}&nbsp;</a>
            </c:if>

        </li>

    </c:forEach>

</ul>

<c:set var="interval" value="60000"/>
<c:if test="${activeJobsQty == 0}">
    <c:set var="interval" value="60000"/>
</c:if>
