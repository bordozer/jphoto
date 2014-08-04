<%@ page import="admin.controllers.jobs.edit.action.PhotoActionGenerationModel" %>
<%@ page import="admin.controllers.jobs.edit.AbstractAdminJobModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="jobModelPhotoAction" type="admin.controllers.jobs.edit.action.PhotoActionGenerationModel" scope="request"/>

<c:set var="totalActionFormControl" value="<%=PhotoActionGenerationModel.TOTAL_ACTIONS_FORM_CONTROL%>"/>
<c:set var="photosQtyFormControl" value="<%=PhotoActionGenerationModel.PHOTOS_QTY_FORM_CONTROL%>"/>
<c:set var="photoLinesControl" value="generationType"/>

<c:set var="dateFromFormControl" value="<%=PhotoActionGenerationModel.DATE_FROM_FORM_CONTROL%>"/>
<c:set var="dateToFormControl" value="<%=PhotoActionGenerationModel.DATE_TO_FORM_CONTROL%>"/>

<tags:page pageModel="${jobModelPhotoAction.pageModel}">

	<admin:jobEditData jobModel="${jobModelPhotoAction}">

		<jsp:attribute name="jobForm">

			<table:table width="700" border="0">

				<table:tr>
					<table:td colspan="2">
						<admin:saveJobButton jobModel="${jobModelPhotoAction}"/>
					</table:td>
				</table:tr>

				<table:separatorInfo colspan="2" title="${eco:translate('Job JSP: Job parameters')}"/>

				<table:tr>
					<table:tdtext text_t="Photo actions job parameters: Photos action to generate" isMandatory="true"/>
					<table:tddata>
						<html:input fieldId="${totalActionFormControl}" fieldValue="${jobModelPhotoAction.totalActions}" size="7"/>
					</table:tddata>
				</table:tr>

				<table:tr>
					<table:tdtext text_t="Photo actions job parameters: Photos"/>
					<table:tddata>
						<html:input fieldId="${photosQtyFormControl}" fieldValue="${jobModelPhotoAction.photosQty}" size="7"/>
						<br/>
						${eco:translate('Photo actions job parameters: Leave empty to select all photos')}
					</table:tddata>
				</table:tr>

				<table:separator colspan="2"/>

				<table:tr>
					<table:td colspan="2">
						<tags:dateRange dateRangeTypeId="${jobModelPhotoAction.dateRangeTypeId}"
										dateFrom="${jobModelPhotoAction.dateFrom}"
										dateTo="${jobModelPhotoAction.dateTo}"
										timePeriod="${jobModelPhotoAction.timePeriod}"/>
					</table:td>
				</table:tr>

				<table:separator colspan="2"/>

			</table:table>

		</jsp:attribute>

	</admin:jobEditData>

</tags:page>