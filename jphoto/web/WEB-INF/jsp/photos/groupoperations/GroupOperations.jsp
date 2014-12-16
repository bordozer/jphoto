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

					<div class="block-border photo-container photo-container-${photo.id}" style="position: relative; display: inline-block; vertical-align: top; min-height: 400px; height: auto; width: ${width}%; ${isGroupOperationAccessible ? "" : "border: 1px solid red;" } padding: 5px; margin: 5px;">

						<div class="floatleft text-centered" style="height: auto; min-height: 320px; padding-bottom: 5px;" onclick="toggleCheckbox( '${photo.id}' );">

							<links:photoCard id="${photo.id}">
								<img src="${photoGroupOperationEntry.photoPreviewImgUrl}" class="photo-preview-image" style="vertical-align: top;" title="${photo.nameEscaped}"/>
							</links:photoCard>

						</div>

						<div class="floatleft" style="text-align: center; padding-bottom: 10px;">
							<photo:photoCard photo="${photo}"/>
						</div>

						<div class="floatleft" style="">

							<c:forEach var="entry" items="${photoGroupOperationModel.photoGroupOperationEntryPropertiesMap}">
								<c:set var="photoGroupOperationEntryProperty" value="${entry.value}"/>

								<c:set var="photo" value="${photoGroupOperationEntry.photo}"/>
								<c:if test="${photoGroupOperationEntryProperty.photoId == photo.id}">
									<c:set var="entryId" value="${photoGroupOperationEntryProperty.entryId}" />
									<c:set var="fieldId" value="photo-${photo.id}-entry-${entryId}"/>
									<div id="container-${entryId}">
										<input type="checkbox" id="${fieldId}" name="${fieldId}" value="true" class="group-operation-checkbox-${entryId} checkbox-${photo.id}" <c:if test="${photoGroupOperationEntryProperty.value}">checked</c:if> />
										${photoGroupOperationEntryProperty.name}
										<br />
										<input type="hidden" id="${fieldId}" name="_${fieldId}" value="false">
										<input type="hidden" id="photoId" value="${photo.id}">
										<input type="hidden" name="${photo.id}" value="${entryId}">
									</div>
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
				<%--<c:forEach var="userPhotoAlbum" items="${photoGroupOperationModel.userPhotoAlbums}">
					<js:checkboxMassChecker checkboxClass="group-operation-checkbox-${userPhotoAlbum.id}" /> ${userPhotoAlbum.name}
					<br />
				</c:forEach>--%>
				<script type="text/javascript">
					function doNothing(){}
				</script>
				<div style="float: left; width: 100%; padding-bottom: 10px;">
					<user:userAlbums userId="${photoGroupOperationModel.user.id}"
						groupSelectionClass="group-operation-checkbox-"
						onEditJSFunction="doNothing"
						onDeleteJSFunction="doNothing"
					/>
				</div>
			</c:if>

			<c:if test="${photoGroupOperationType == 'ARRANGE_TEAM_MEMBERS'}">
				<script type="text/javascript">

					function onEdit( teamMember ) {

						var name = teamMember.get( 'entryName' ) + " ( " + teamMember.get( 'teamMemberTypeName' ) + " )";
						var userTeamMemberId = teamMember.get( 'userTeamMemberId' );

						require( [ 'jquery' ], function ( $ ) {
							$( ".photo-container" ).each( function( index, _container ) {
								var container = $( _container );
								var label = $( '.label-' + userTeamMemberId, container );

								if ( label.length > 0 ) {
									label.text( name );
								} else {
									var photoId = $( '#photoId', container ).val();

									var memberContainer = $( "<div id='container-" + userTeamMemberId + "'></div>" );
									var id = "photo-" + photoId + "-entry-" + userTeamMemberId;

									memberContainer.append( "<input type='checkbox' id=\"" + id + "\" name=\"" + id + "\" value='true' class='group-operation-checkbox-" + userTeamMemberId + " checkbox-" + photoId + "'>" );
									memberContainer.append( " <label for=\"" + id + "\"><span class='label-" + userTeamMemberId + "'>" + name + "</span></label>" );
									memberContainer.append( "<br />" );
									memberContainer.append( "<input type='hidden' id=\"_" + id + "\" name=\"_" + id + "\" value='false'>" );
									memberContainer.append( "<input type='hidden' id='photoId' value='" + photoId + "'>" );
									memberContainer.append( "<input type='hidden' name='" + photoId + "' value='" + userTeamMemberId + "'>" );

									$( container ).append( memberContainer );
								}
							});
						} );
					}

					function onDelete( teamMemberId ) {
						require( [ 'jquery' ], function ( $ ) {
							$( ".photo-container" ).each( function( index, container ) {
								$( '#container-' + teamMemberId, container ).remove();
							});
						} );
					}

				</script>

				<div style="float: left; width: 100%; padding-bottom: 10px;">
					<user:userTeam userId="${photoGroupOperationModel.user.id}"
								   groupSelectionClass="group-operation-checkbox-"
								   onEditJSFunction="onEdit"
								   onDeleteJSFunction="onDelete"
							/>
				</div>

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