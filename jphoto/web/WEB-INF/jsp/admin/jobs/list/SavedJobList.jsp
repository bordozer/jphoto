<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<jsp:useBean id="savedJobListModel" type="admin.controllers.jobs.list.SavedJobListModel" scope="request"/>

<tags:page pageModel="${savedJobListModel.pageModel}">

	<c:set var="savedJobs" value="${savedJobListModel.savedJobs}" />

	<c:set var="notDeletableJobIds" value="${savedJobListModel.notDeletableJobIds}" />
	<c:set var="activeJobPercentageMap" value="${savedJobListModel.activeJobPercentageMap}" />
	<c:set var="activeSavedJobIds" value="${savedJobListModel.activeSavedJobIds}" />
	<c:set var="jobListTab" value="${savedJobListModel.jobListTab}" />

	<admin:jobListHeader jobListTab="${savedJobListModel.jobListTab}"
						 tabJobInfosMap="${savedJobListModel.tabJobInfosMap}"
						 activeJobs="${savedJobListModel.activeJobs}" />

	<div class="panel">
		<div class="panel-body">

		<js:confirmAction />

		<admin:jobList savedJobs="${savedJobs}"
					   notDeletableJobIds="${notDeletableJobIds}"
					   activeJobPercentageMap="${activeJobPercentageMap}"
					   activeSavedJobIds="${activeSavedJobIds}"
					   jobListTab="${jobListTab}"
					   selectedSavedJobTypeId="${savedJobListModel.savedJobType.id}"
				/>

		</div>
	</div>
</tags:page>