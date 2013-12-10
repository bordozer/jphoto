<%@ page import="admin.controllers.jobs.edit.preview.PreviewGenerationModel" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
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

			<table:table border="0" width="500">

				<table:tr>
					<table:td colspan="2">
						<admin:saveJobButton jobModel="${previewGenerationModel}"/>
					</table:td>
				</table:tr>

				<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}"/>

				<table:tr>
					<table:td>
						${eco:translate('Preview size')}:
					</table:td>

					<table:td>
						<tags:inputHint inputId="${previewSizeControl}" focused="true" hintTitle_t="Preview size" hint="${eco:translate('Preview size')}">
						<jsp:attribute name="inputField">
							<form:input path="${previewSizeControl}" cssErrorClass="invalid" size="4"/>
						</jsp:attribute>
						</tags:inputHint>
					</table:td>

				</table:tr>

				<table:tr>
					<table:td>
						${eco:translate('Skip generation if preview already exists')}
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
