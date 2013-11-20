<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="command" type="admin.controllers.jobs.edit.AbstractAdminJobModel" scope="request"/>
<jsp:useBean id="jobExecutionHistoryEntryId" type="java.lang.Integer" scope="request"/>

<tags:page pageModel="${command.pageModel}">

	<admin:jobListHeader jobListTab="${command.jobListTab}"
						 tabJobInfosMap="${command.tabJobInfosMap}"
						 activeJobs="${command.activeJobs}"
						 suppressAutoReloading="true"
		/>

	<h3>${eco:translate1('Job history entry #$1 not found.', jobExecutionHistoryEntryId)}</h3>

	<br />

	${eco:translate('It must have been deleted.')}

</tags:page>