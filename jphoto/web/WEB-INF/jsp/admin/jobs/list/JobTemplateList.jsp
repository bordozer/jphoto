<%@ page import="admin.jobs.enums.JobListTab" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<jsp:useBean id="savedJobListModel" type="admin.controllers.jobs.list.SavedJobListModel" scope="request"/>

<c:set var="savedJobTabs" value="<%=JobListTab.SAVED_JOB_TABS%>"/>

<tags:page pageModel="${savedJobListModel.pageModel}">

	<c:set var="activeJobTypes" value="${savedJobListModel.activeJobTypes}" />

	<admin:jobListHeader jobListTab="${savedJobListModel.jobListTab}"
						 tabJobInfosMap="${savedJobListModel.tabJobInfosMap}"
						 activeJobs="${savedJobListModel.activeJobs}" />

	<div style="width: 95%; float: left; height: 100%; margin: 10px;">

		<js:confirmAction />

		<table:table border="0" width="90%">

			<table:tr>

				<c:set var="counter" value="1"/>

				<c:forEach var="savedJobTab" items="${savedJobTabs}">

					<c:if test="${counter > 3}">
						</tr><tr>
						<c:set var="counter" value="1"/>
					</c:if>

					<table:td valign="top" width="30%">
						<admin:templateJobList activeJobTypes="${activeJobTypes}" jobListTab="${savedJobTab}" />
					</table:td>

					<table:td width="20"/>

					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>

			</table:tr>

		</table:table>

	</div>

</tags:page>