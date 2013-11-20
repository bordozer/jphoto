<%@ page import="admin.controllers.jobs.edit.activityStream.ActivityStreamCleanupJobModel" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="activityStreamCleanupJobModel" type="admin.controllers.jobs.edit.activityStream.ActivityStreamCleanupJobModel" scope="request"/>

<c:set var="leave_activity_for_days_control" value="<%=ActivityStreamCleanupJobModel.LEAVE_ACTIVITY_FOR_DAYS_CONTROL%>"/>
<c:set var="savedJob" value="${activityStreamCleanupJobModel.job}"/>

<tags:page pageModel="${activityStreamCleanupJobModel.pageModel}">

	<admin:jobStart jobModel="${activityStreamCleanupJobModel}"/>

	<form:form modelAttribute="activityStreamCleanupJobModel" method="POST" action="${eco:baseAdminUrlWithPrefix()}/jobs/${savedJob.jobType.prefix}/" id="FormName" name="FormName">

		<table:table border="0" width="700">

			<table:tr>
				<table:td colspan="2">
					<admin:saveJobButton jobModel="${activityStreamCleanupJobModel}"/>
				</table:td>
			</table:tr>

			<table:separatorInfo colspan="2" title="${eco:translate('Delete activities older then')}"/>

			<table:tr>
				<table:tdtext text_t="Delete activities older then" isMandatory="true" />

				<table:tddata>
					<tags:inputHint inputId="${leave_activity_for_days_control}" focused="true" hintTitle_t="Leave Activity For Days" hint="${eco:translate('Delete activities older then')}">
						<jsp:attribute name="inputField">
							<form:input path="${leave_activity_for_days_control}" cssErrorClass="invalid" size="4" /> ${eco:translate('days')}
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>

			</table:tr>

		</table:table>

		<admin:jobFinish jobModel="${activityStreamCleanupJobModel}" />

	</form:form>

	<tags:springErrorHighliting bindingResult="${activityStreamCleanupJobModel.bindingResult}"/>

</tags:page>