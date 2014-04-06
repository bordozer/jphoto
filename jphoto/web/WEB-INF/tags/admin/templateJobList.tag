<%@ tag import="admin.jobs.enums.SavedJobType" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ attribute name="activeJobTypes" type="java.util.Set" required="true" %>
<%@ attribute name="jobListTab" type="admin.jobs.enums.JobListTab" required="true" %>

<c:set var="savedJobTypes" value="<%=SavedJobType.values()%>" />

<table:table border="0" width="400">

	<table:separatorInfo colspan="3" title="${eco:translate(jobListTab.name)}" height="50"/>

	<c:forEach var="jobType" items="${savedJobTypes}">

		<c:if test="${jobListTab == jobType.jobListTab}">

			<c:set var="jobTypeId" value="${jobType.id}" />
			<c:set var="isJobTypeActive" value="${eco:contains(activeJobTypes, jobTypeId)}" />

			<table:tr>

			<table:tdicon>
				<c:if test="${isJobTypeActive}">
					<html:spinningWheel16 title="${eco:translate('One or more jobs of this type is active now')}" />
				</c:if>
			</table:tdicon>

			<table:tdicon>
				<html:img32 src="jobtype/${jobType.icon}" />
			</table:tdicon>

			<table:td>
				<admin:jobTemplate savedJobType="${jobType}" />
				<%--<a href="${eco:baseAdminUrlWithPrefix()}/jobs/${jobType.prefix}/" title="${eco:translate(jobType.name)}">${eco:translate(jobType.name)}</a>--%>
			</table:td>

			</table:tr>
		</c:if>

	</c:forEach>

</table:table>



