<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="jobModel" type="admin.controllers.jobs.edit.AbstractAdminJobModel" required="true" %>
<%@ attribute name="jobForm" fragment="true" required="true" %>

<c:set var="job" value="${jobModel.job}"/>

<tags:inputHintForm/>
<js:confirmAction/>

<admin:jobListHeader jobListTab="${jobModel.jobListTab}"
					 tabJobInfosMap="${jobModel.tabJobInfosMap}"
					 activeJobs="${jobModel.activeJobs}"
					 suppressAutoReloading="true"
		/>

<div style="float: left; width: 100%;">

	<form:form id="FormName" name="FormName" action="${eco:baseAdminUrl()}/jobs/${job.jobType.prefix}/">

		<input type="hidden" id="referrer" name="referrer" value="${jobModel.referrer}">

		<jsp:invoke fragment="jobForm"/>

	</form:form>

	&nbsp;&nbsp;${eco:translate('Job: total users')}: <b>${jobModel.usersTotal}</b>
	<br/>
	&nbsp;&nbsp;${eco:translate('Job: total Photos')}: <b>${jobModel.photosTotal}</b>

</div>

<tags:springErrorHighliting bindingResult="${jobModel.bindingResult}"/>

<js:confirmAction />
