<%@ page import="ui.controllers.photos.DEL_edit.PhotoEditDataModel" %>
<%@ page import="ui.controllers.photos.DEL_edit.PhotoEditWizardStep" %>
<%@ page import="core.enums.YesNo" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="anonym" tagdir="/WEB-INF/tags/anonym" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.DEL_edit.PhotoEditDataModel" scope="request"/>

<c:set var="photoFileControl" value="<%=PhotoEditDataModel.PHOTO_EDIT_DATA_FILE_FORM_CONTROL%>"/>
<c:set var="photoFileUploadWizardStepId" value="<%=PhotoEditWizardStep.PHOTO_FILE_UPLOAD.getId()%>"/>
<c:set var="yesId" value="<%=YesNo.YES.getId()%>"/>
<c:set var="uploadAllowance" value="${photoEditDataModel.uploadAllowance}"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

	<h3>${eco:translate(photoEditDataModel.currentStep.stepDescription)}</h3>

	<photo:photoAllowance uploadAllowance="${uploadAllowance}" />

	<eco:form action="${eco:baseUrl()}/photos/save/" multipartForm="true">

		<input type="hidden" id="currentStepId" name="currentStepId" value="${photoFileUploadWizardStepId}">

		<table:table>

			<table:separatorInfo colspan="2" height="50" title="${eco:translate('Select photo file')}"/>

			<table:tredit>
				<table:tdtext text_t="File" labelFor="${photoFileControl}" isMandatory="true"/>

				<table:tddata>
					<form:input path="photoEditDataModel.file" type="file" id="${photoFileControl}"/>
				</table:tddata>
			</table:tredit>

			<table:separatorInfo colspan="2" height="50" title="${eco:translate('Data from previous step')}"/>

			<table:tr>
				<table:tdtext text_t="Photo name"/>
				<table:tddata>${photoEditDataModel.name}</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Genre"/>
				<table:tddata><links:genrePhotos genre="${photoEditDataModel.genre}" /></table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Contains nude content"/>
				<table:tddata>${eco:translate(photoEditDataModel.containsNudeContent ? 'Yes' : 'No')}</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Background color"/>
				<table:tddata><div style="float: left; width: 100px; height: 17px ;background: #${photoEditDataModel.bgColor}">&nbsp;</div></table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Description"/>
				<table:tddata>${photoEditDataModel.description}</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Keywords"/>
				<table:tddata>${photoEditDataModel.keywords}</table:tddata>
			</table:tr>

			<c:set var="isAnonymousDay" value="${photoEditDataModel.anonymousDay}" />
			<c:set var="isAnonymousPosting" value="${photoEditDataModel.anonymousPosting}" />

			<table:tr>
				<table:tdtext text_t="Anonymous posting"/>
				<table:tddata>
					${eco:translate(isAnonymousPosting ? "Yes" : "No")}
					<br />
					<anonym:anonymousDaySchedule />
				</table:tddata>
			</table:tr>

			<table:separator colspan="2" />

			<table:tr>
				<table:tdtext text_t="Comments allowance"/>
				<table:tddata>${eco:translate(photoEditDataModel.commentsAllowance.name)}</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Notification about new comments"/>
				<table:tddata>${eco:translate(photoEditDataModel.notificationEmailAboutNewPhotoComment == yesId ? 'Yes' : 'No')}</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Voting allowance"/>
				<table:tddata>${eco:translate(photoEditDataModel.votingAllowance.name)}</table:tddata>
			</table:tr>

			<table:separator colspan="2" />

			<table:tr>
				<table:tdtext text_t="Photo team"/>
				<table:tddata>
					<c:forEach var="photoTeamMember" items="${photoEditDataModel.photoTeamMembers}" >
						<links:userTeamMemberCard userTeamMember="${photoTeamMember}" />
						<br />
					</c:forEach>
				</table:tddata>
			</table:tr>

			<table:separator colspan="2" />

			<table:tr>
				<table:tdtext text_t="Photo albums"/>
				<table:tddata>
					<c:forEach var="photoAlbum" items="${photoEditDataModel.photoAlbums}" >
						<links:userPhotoAlbumPhotos userPhotoAlbum="${photoAlbum}" />
						<br />
					</c:forEach>
				</table:tddata>
			</table:tr>

			<table:trok text_t="Save" onclick="return savePhoto();"/>

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${photoEditDataModel.bindingResult}"/>

	<script type="text/javascript">
		function savePhoto() {
			$( '#FormName' ).submit();
		}
	</script>

</tags:page>