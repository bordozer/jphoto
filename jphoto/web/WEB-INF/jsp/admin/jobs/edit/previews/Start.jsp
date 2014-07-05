<%@ page import="admin.controllers.jobs.edit.preview.PreviewGenerationModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="previewGenerationModel" type="admin.controllers.jobs.edit.preview.PreviewGenerationModel" scope="request"/>

<c:set var="previewSizeControl" value="<%=PreviewGenerationModel.PREVIEW_SIZE_FORM_CONTROL%>"/>
<c:set var="savedJob" value="${previewGenerationModel.job}"/>

<tags:page pageModel="${previewGenerationModel.pageModel}">

	<admin:jobEditData jobModel="${previewGenerationModel}">

		<jsp:attribute name="jobForm">

			<table:table border="0" width="700">

				<table:tr>
					<table:td colspan="2">
						<admin:saveJobButton jobModel="${previewGenerationModel}"/>
					</table:td>
				</table:tr>

				<table:separatorInfo colspan="2" title="${eco:translate('Job JSP: Job parameters')}"/>

				<table:tr>
					<table:td>
						${eco:translate('Preview generation job: Preview size')}:
					</table:td>

					<table:td>
						<form:input path="${previewSizeControl}" cssErrorClass="invalid" size="4"/>
					</table:td>

				</table:tr>

				<table:tr>
					<table:td>
						${eco:translate('Preview generation job: Skip generation if preview already exists')}
					</table:td>
					<table:td>
						<form:checkbox path="skipPhotosWithExistingPreview"/>
					</table:td>

				</table:tr>

				<table:separator colspan="2"/>

			</table:table>

		</jsp:attribute>

	</admin:jobEditData>

</tags:page>
