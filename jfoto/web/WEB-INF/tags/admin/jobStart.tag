<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="jobModel" type="admin.controllers.jobs.edit.AbstractAdminJobModel" required="true" %>

<tags:inputHintForm/>
<js:confirmAction/>

<admin:jobListHeader jobListTab="${jobModel.jobListTab}"
						 tabJobInfosMap="${jobModel.tabJobInfosMap}"
						 activeJobs="${jobModel.activeJobs}"
						 suppressAutoReloading="true"
		/>

<input type="hidden" id="referrer" name="referrer" value="${jobModel.referrer}">
