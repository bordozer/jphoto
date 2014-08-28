<%@ page import="ui.controllers.photos.groupoperations.PhotoGroupOperationModel" %>
<%@ page import="ui.controllers.photos.groupoperations.PhotoGroupOperationValidator" %>
<%@ page import="ui.controllers.photos.groupoperations.handlers.AbstractGroupOperationHandler" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="x_rt" uri="http://java.sun.com/jstl/xml_rt" %>

<jsp:useBean id="photoGroupOperationModel" type="ui.controllers.photos.groupoperations.PhotoGroupOperationModel" scope="request"/>

<c:set var="controlPhotoAlbumId" value="<%=PhotoGroupOperationModel.FORM_CONTROL_PHOTO_ALBUM_ID%>"/>

<c:set var="photoGroupOperationEntries" value="${photoGroupOperationModel.photoGroupOperationEntries}"/>
<c:set var="selectedPhotosQty" value="${fn:length(photoGroupOperationEntries)}"/>

<c:set var="customPercent" value="${eco:floor(100 / selectedPhotosQty) - 3}"/>
<c:set var="width" value="${selectedPhotosQty < 5 ? customPercent : 23}"/>

<c:set var="photoGroupOperationType" value="${photoGroupOperationModel.photoGroupOperationType}"/>
<c:set var="noGenreSelected" value="<%=PhotoGroupOperationValidator.NO_GENRE_SELECTED%>"/>

<c:set var="singleEntryId" value="<%=AbstractGroupOperationHandler.ENTRY_ID%>" />

<tags:page pageModel="${photoGroupOperationModel.pageModel}">

	<script type="text/javascript">
		function toggleCheckbox( photoId ) {
			$( ".checkbox-" + photoId ).trigger( 'click' );
		}
	</script>

	<div style="width: 98%; margin-left: 50px;">

		<form:form modelAttribute="photoGroupOperationModel" action="${eco:baseUrl()}/photos/groupOperations/confirm/">

			<h3>${eco:translate('Selected photos')}</h3>

			<div class="floatleft" style="padding-bottom: 30px;">

				<c:forEach var="photoGroupOperationEntry" items="${photoGroupOperationEntries}">
					<c:set var="photo" value="${photoGroupOperationEntry.photo}"/>
					<c:set var="isGroupOperationAccessible" value="${photoGroupOperationEntry.groupOperationAccessible}"/>

					<div class="block-border photo-container-${photo.id}" style="position: relative; display: inline-block; vertical-align: top; min-height: 300px; height: auto; width: ${width}%; ${isGroupOperationAccessible ? "" : "border: 1px solid red;" } padding: 5px; margin: 5px;">

						<div class="floatleft text-centered" style="height: auto; min-height: 230px;" onclick="toggleCheckbox( '${photo.id}' );">

							<links:photoCard id="${photo.id}">
								<img src="${photoGroupOperationEntry.photoPreviewImgUrl}" class="photo-preview-image" style="vertical-align: middle;" title="${photo.nameEscaped}"/>
							</links:photoCard>
							<br/>
							<br/>

						</div>

						<div class="floatleft" style="text-align: center;">
							<photo:photoCard photo="${photo}"/>
						</div>

						<div class="floatleft" style="">

							<c:forEach var="entry" items="${photoGroupOperationModel.photoGroupOperationEntryPropertiesMap}">
								<c:set var="photoGroupOperationEntryProperty" value="${entry.value}"/>

								<c:set var="photo" value="${photoGroupOperationEntry.photo}"/>
								<c:if test="${photoGroupOperationEntryProperty.photoId == photo.id}">
									<c:set var="entryId" value="${photoGroupOperationEntryProperty.entryId}" />

									<c:set var="fieldId" value="photoGroupOperationEntryPropertiesMap['${photo.id}_${entryId}'].value"/>

									<input type="checkbox" id="${fieldId}" name="${fieldId}" value="true" class="group-operation-checkbox-${entryId} checkbox-${photo.id}" <c:if test="${photoGroupOperationEntryProperty.value}">checked</c:if> />
									${photoGroupOperationEntryProperty.name}
									<br />
									<input type="hidden" id="_${fieldId}" name="_${fieldId}" value="false">
								</c:if>
							</c:forEach>

							<c:if test="${not empty photoGroupOperationEntry.photoOperationAllowanceMessage}">
								<br/>
								${photoGroupOperationEntry.photoOperationAllowanceMessage}
							</c:if>

							<c:if test="${not empty photoGroupOperationEntry.photoOperationInfo}">
								<br/>
								${photoGroupOperationEntry.photoOperationInfo}
							</c:if>

						</div>
					</div>

				</c:forEach>

			</div>

			<h3>${eco:translate('Group operation details')}:</h3>

			<c:if test="${photoGroupOperationType == 'ARRANGE_PHOTO_ALBUMS'}">
				<c:forEach var="userPhotoAlbum" items="${photoGroupOperationModel.userPhotoAlbums}">
					<js:checkboxMassChecker checkboxClass="group-operation-checkbox-${userPhotoAlbum.id}" /> ${userPhotoAlbum.name}
					<br />
				</c:forEach>
			</c:if>

			<c:if test="${photoGroupOperationType == 'ARRANGE_TEAM_MEMBERS'}">
				<div style="float: left; width: 100%; padding-bottom: 10px;">
					<user:userTeam userId="${photoGroupOperationModel.user.id}" />
				</div>
				<%--<c:forEach var="userTeamMember" items="${photoGroupOperationModel.userTeamMembers}">
					<js:checkboxMassChecker checkboxClass="group-operation-checkbox-${userTeamMember.id}" /> <links:userTeamMemberCard userTeamMember="${userTeamMember}" /> ( ${eco:translate(userTeamMember.teamMemberType.name)} )
					<br />
				</c:forEach>--%>
			</c:if>

			<c:if test="${photoGroupOperationType == 'ARRANGE_NUDE_CONTENT'}">
				<js:checkboxMassChecker checkboxClass="group-operation-checkbox-${singleEntryId}" /> ${eco:translate('Nude content')}
			</c:if>

			<c:if test="${photoGroupOperationType == 'DELETE_PHOTOS'}">
				<js:checkboxMassChecker checkboxClass="group-operation-checkbox-${singleEntryId}" initiallyChecked="true" />
				<br/>
				${eco:translate('Group deletion: Selected photos will be deleted')}
				<br/>
				<br/>
				${eco:translate('Group deletion: Also will be deleted:')}
				<ul>
					<li>${eco:translate('Group deletion: Comments to photos')}</li>
					<li>${eco:translate('Group deletion: Voting information')}</li>
					<li>${eco:translate('Group deletion: Photos awards')}</li>
					<li>${eco:translate('Group deletion: Photos will be deleted from all albums')}</li>
				</ul>
				<b>${eco:translate('Group deletion: Operation can NOT be undone')}</b>

				<script type="text/javascript">
					function performGroupOperation() {
						return confirm( "${eco:translate('Group deletion: All selected photos will be deleted FOREVER. It is the last chance to prevent this. Proceed?')}" );
					}
				</script>
			</c:if>
			<c:if test="${photoGroupOperationType != 'DELETE_PHOTOS'}">
				<script type="text/javascript">
					function performGroupOperation() {
						return confirm( "${eco:translate1("Group operation '$1'\\n\\nPerform?", photoGroupOperationType.name)}" );
					}
				</script>
			</c:if>

			<c:if test="${photoGroupOperationType == 'MOVE_TO_GENRE'}">
				<js:checkboxMassChecker checkboxClass="group-operation-checkbox-${singleEntryId}" initiallyChecked="true" />
				<br />
				${eco:translate("Note. If a photo has 'Nude contain' option, then it is possible to move it only in category that supports nude content. Moving of unsuitable for the selected category photos will be skipped.")}
				<br />
				<br />
				${eco:translate('Select the genre to move to')}:
				<br />
				<br />
				<form:select path="moveToGenreId">
					<form:option value="${noGenreSelected}">&nbsp;</form:option>
					<form:options items="${photoGroupOperationModel.genreEntries}" itemValue="genreId" itemLabel="genreName" />
				</form:select>
			</c:if>

			<br/>
			<br/>

			${eco:translate('Click the button below to perform group operation')}
			<br />
			<html:submitButton id="doAction" caption_t="${photoGroupOperationType.name}" onclick="return performGroupOperation();"/>

		</form:form>

	</div>

	<tags:springErrorHighliting bindingResult="${photoGroupOperationModel.bindingResult}"/>

	<div class="footerseparatorsmall"></div>

</tags:page>