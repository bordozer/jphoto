<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="archivingJobJobModel" type="admin.controllers.jobs.edit.archiving.ArchivingJobJobModel" scope="request"/>

<tags:page pageModel="${archivingJobJobModel.pageModel}">

	<admin:jobEditData jobModel="${archivingJobJobModel}">

	<jsp:attribute name="jobForm">

		<table:table width="700" border="0">

			<table:tr>
				<table:td colspan="2">
					<admin:saveJobButton jobModel="${archivingJobJobModel}"/>
				</table:td>
			</table:tr>

			<table:separatorInfo colspan="2" title="${eco:translate('Job JSP: Job parameters')}"/>

			<table:tr>
				<table:td>
					${eco:translate1('Delete information about photos previews older then $1 days', archivingJobJobModel.archivePreviewsOlderThen )}:
				</table:td>

				<table:td>
					<form:checkbox path="previewsArchivingEnabled" cssErrorClass="invalid" size="4"/>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>
					${eco:translate1('Archive information about photos appraisal older then $1 days', archivingJobJobModel.archiveAppraisalOlderThen )}:
				</table:td>

				<table:td>
					<form:checkbox path="appraisalArchivingEnabled" cssErrorClass="invalid" size="4"/>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>
					${eco:translate1('Archive photos uploaded earlie then $1 days', archivingJobJobModel.archivePhotosOlderThen )}:
				</table:td>

				<table:td>
					<form:checkbox path="photosArchivingEnabled" cssErrorClass="invalid" size="4"/>
				</table:td>
			</table:tr>

			<table:separator colspan="2"/>

		</table:table>

	</jsp:attribute>

</admin:jobEditData>

</tags:page>