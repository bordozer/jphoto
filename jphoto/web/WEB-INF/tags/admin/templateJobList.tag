<%@ tag import="admin.jobs.enums.SavedJobType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ attribute name="activeJobTypes" type="java.util.Set" required="true" %>
<%@ attribute name="jobListTab" type="admin.jobs.enums.JobListTab" required="true" %>

<c:set var="savedJobTypes" value="<%=SavedJobType.values()%>" />

<div class="panel panel-default">

	<div class="panel-heading">
		<h3 class="panel-title">${eco:translate(jobListTab.name)}</h3>
	</div>

	<div class="panel-body">

		<c:forEach var="jobType" items="${savedJobTypes}">

			<c:if test="${jobListTab == jobType.jobListTab}">

				<c:set var="jobTypeId" value="${jobType.id}" />
				<c:set var="isJobTypeActive" value="${eco:contains(activeJobTypes, jobTypeId)}" />

				<div class="row">

					<div class="col-lg-1">
						<c:if test="${isJobTypeActive}">
							<html:spinningWheel16 title="${eco:translate('One or more jobs of this type is active now')}" />
						</c:if>
					</div>

					<div class="col-lg-1">
						<html:img32 src="jobtype/${jobType.icon}" />
					</div>

					<div class="col-lg-10">
						<admin:jobTemplate savedJobType="${jobType}" />
					</div>
				</div>
			</c:if>

		</c:forEach>

	</div>

</div>



