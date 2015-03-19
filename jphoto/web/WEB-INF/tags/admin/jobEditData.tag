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

<div class="row">

	<div class="col-lg-12">

		<form:form id="FormName" name="FormName" action="${eco:baseAdminUrl()}/jobs/${job.jobType.prefix}/">

			<input type="hidden" id="referrer" name="referrer" value="${jobModel.referrer}">

			<admin:saveJobButton jobModel="${jobModel}"/>

			<div class="panel panel-info job-box">

				<div class="panel panel-heading">
					<h3 class='panel-title'>
						${eco:translate('Job JSP: Job parameters')}
					</h3>
				</div>

				<div class="panel-body">

					<jsp:invoke fragment="jobForm"/>

				</div>

				<div class="panel-footer">

					<div class="row">
						<div class="col-lg-12">
							${eco:translate('Job JSP: total users')}: <b>${jobModel.usersTotal}</b>
						</div>
					</div>

					<div class="row">
						<div class="col-lg-12">
							${eco:translate('Job JSP: total Photos')}: <b>${jobModel.photosTotal}</b>
						</div>
					</div>

				</div>

			</div>

		</form:form>

	</div>

</div>

<tags:springErrorHighliting bindingResult="${jobModel.bindingResult}"/>

<js:confirmAction />
