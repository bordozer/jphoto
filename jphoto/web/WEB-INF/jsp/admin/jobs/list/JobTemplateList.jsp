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

	<c:set var="activeJobTypes" value="${savedJobListModel.activeJobTypes}"/>

	<div class="panel">
		<div class="panel-body">
			<div class="row row-bottom-padding-10">
				<admin:jobListHeader jobListTab="${savedJobListModel.jobListTab}"
									 tabJobInfosMap="${savedJobListModel.tabJobInfosMap}"
									 activeJobs="${savedJobListModel.activeJobs}"/>
			</div>


			<div class="row"> <%-- panel row 1 --%>

				<js:confirmAction/>

				<div class="col-lg-12"> <%-- panel row 1 column --%>

					<div class="row">
						<c:forEach var="savedJobTab" items="${savedJobTabs}">

							<div class="col-lg-4">
								<admin:templateJobList activeJobTypes="${activeJobTypes}" jobListTab="${savedJobTab}"/>
							</div>

						</c:forEach>
					</div>

				</div> <%-- / panel row 1 column --%>

			</div> <%-- / panel row 1 --%>
		</div> <%-- / panel body --%>
	</div> <%-- / panel --%>

</tags:page>