<%@ page import="admin.controllers.jobs.edit.activityStream.ActivityStreamCleanupJobModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="activityStreamCleanupJobModel" type="admin.controllers.jobs.edit.activityStream.ActivityStreamCleanupJobModel" scope="request"/>

<c:set var="leave_activity_for_days_control" value="<%=ActivityStreamCleanupJobModel.LEAVE_ACTIVITY_FOR_DAYS_CONTROL%>"/>
<c:set var="activity_stream_type_ids_to_delete_control" value="<%=ActivityStreamCleanupJobModel.ACTIVITY_STREAM_TYPE_IDS_TO_DELETE_CONTROL%>"/>

<c:set var="savedJob" value="${activityStreamCleanupJobModel.job}"/>
<c:set var="activityTypes" value="${activityStreamCleanupJobModel.activityTypes}"/>

<tags:page pageModel="${activityStreamCleanupJobModel.pageModel}">

	<admin:jobEditData jobModel="${activityStreamCleanupJobModel}">

		<jsp:attribute name="jobForm">

			<table:table border="0" width="700">

				<table:tr>
					<table:td colspan="2">
						<admin:saveJobButton jobModel="${activityStreamCleanupJobModel}"/>
					</table:td>
				</table:tr>

				<table:separatorInfo colspan="2" title="${eco:translate('Delete activities older then')}"/>

				<table:tr>
					<table:tdtext text_t="Delete activities older then" isMandatory="true"/>

					<table:tddata>
						<tags:inputHint inputId="${leave_activity_for_days_control}" focused="true" hintTitle_t="Leave Activity For Days"
										hint="${eco:translate('Delete activities older then')}">
							<jsp:attribute name="inputField">
								<form:input path="${leave_activity_for_days_control}" cssErrorClass="invalid" size="4"/> ${eco:translate('activity cleanup job => parameter => days')}
							</jsp:attribute>
						</tags:inputHint>
					</table:tddata>

				</table:tr>

				<table:tr>
					<table:tdtext text_t="Actyvity types to delete" isMandatory="true"/>
					<table:tddata>
						<js:checkboxMassChecker checkboxClass="${activity_stream_type_ids_to_delete_control}"/>
						<br/>
						<br/>
						<form:checkboxes path="${activity_stream_type_ids_to_delete_control}" items="${activityTypes}" itemValue="id" itemLabel="name" delimiter="<br />" cssErrorClass="invalid" />
					</table:tddata>
				</table:tr>

			</table:table>

		</jsp:attribute>

	</admin:jobEditData>

</tags:page>