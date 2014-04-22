<%@ tag import="ui.controllers.photos.groupoperations.PhotoGroupOperationModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<%@ attribute name="photoList" required="true" type="ui.elements.PhotoList" %>

<c:set var="photoListTitle" value="${photoList.photoListTitle}"/>
<c:set var="totalPhotos" value="${photoList.totalPhotos}"/>

<c:set var="groupOperationForm" value="groupOperationForm" />

<c:set var="photoGroupOperationMenues" value="<%=photoList.getPhotoGroupOperationMenuContainer().getGroupOperationMenus()%>" />
<c:set var="isGroupOperationEnabled" value="${not empty photoGroupOperationMenues}" />
<c:set var="showPaging" value="${photoList.showPaging}"/>

<c:set var="formAction" value="" />
<c:if test="${isGroupOperationEnabled}">
	<c:set var="formAction" value="${eco:baseUrl()}/photos/groupOperations/" />
</c:if>

<c:set var="photoGroupOperationMenues" value="<%=photoList.getPhotoGroupOperationMenuContainer().getGroupOperationMenus()%>" />
<c:set var="isGroupOperationEnabled" value="${not empty photoGroupOperationMenues}" />

<icons:favoritesJS />

<c:if test="${showPaging}">
	<tags:paging showSummary="false"/>
</c:if>

<eco:form action="${formAction}" formName="${groupOperationForm}">

	<div class="photo-list-container">

		<div class="photo-list-title block-background block-border block-shadow">

			<c:if test="${not empty photoListTitle}">
				${photoListTitle}
			</c:if>

		</div>

		<div class="empty-photo-list-text">
			<c:if test="${totalPhotos == 0}">
				${eco:translate(photoList.noPhotoText)}
			</c:if>
		</div>

		<c:forEach var="photoId" items="${photoList.photoIds}" varStatus="status">

			<c:set var="photoId" value="${photoId}" />

			<div class="photo-container block-border block-shadow block-background photo-container-${photoList.photoListId}-${photoId}">
				<div style="width: 16px; height: 16px; margin-left: auto; margin-right: auto; margin-top: 150px;">
					<html:spinningWheel16 title="${eco:translate('The photo is being loaded...')}" />
				</div>
			</div>

		</c:forEach>

	</div>

	<c:if test="${showPaging}">
		<tags:paging showSummary="true"/>
	</c:if>

	<script type="text/javascript">

		var photosToRender = [];

		<c:forEach var="photoId" items="${photoList.photoIds}" varStatus="status">
			photosToRender.push( ${photoId} );
		</c:forEach>

		renderPhotos( photosToRender );

		function renderPhotos( photosToRender ) {

			require( ['modules/photo/list/photo-list'], function ( photoListEntry ) {
				for (var i = 0; i < photosToRender.length; i++) {
					var photoId = photosToRender[i];
					var photoUniqueClass = 'photo-container-' + ${photoList.photoListId} +'-' + photoId;
					photoListEntry( photoId, ${isGroupOperationEnabled}, '${eco:baseUrl()}', $( '.' + photoUniqueClass ) );
				}
			} );
		}

	</script>

	<js:confirmAction />

	<c:set var="controlSelectedPhotoIds" value="<%=PhotoGroupOperationModel.FORM_CONTROL_SELECTED_PHOTO_IDS%>"/>

	<c:if test="${isGroupOperationEnabled}">

		<div class="footerseparatorverysmall"></div>

		<c:set var="controlPhotoGroupOperationId" value="<%=PhotoGroupOperationModel.FORM_CONTROL_PHOTO_GROUP_OPERATION_ID%>" />

		<div style="float: left; width: 90%; padding-left: 50px;">

			<js:checkBoxChecker namePrefix="${controlSelectedPhotoIds}" />

			<label for="${controlPhotoGroupOperationId}">${eco:translate('Photo list: Group operations with selected photos')}</label>

			<select id="${controlPhotoGroupOperationId}" name="${controlPhotoGroupOperationId}">
				<option value="" selected="selected"></option>
				<c:forEach var="photoGroupOperationMenu" items="${photoGroupOperationMenues}">
					<c:set var="photoGroupOperation" value="${photoGroupOperationMenu.photoGroupOperation}"/>
					<option value="${photoGroupOperation.id}">${eco:translate(photoGroupOperation.name)}</option>
				</c:forEach>
			</select>

			<html:submitButton id="ok" caption_t="Photo list: Do group operations" onclick="return submitForm();" />
		</div>

		<script type="text/javascript">

			function submitForm() {
				var groupOperationSelect = $( '#${controlPhotoGroupOperationId}' );
				if ( groupOperationSelect.val() == '' || groupOperationSelect.val() == -1 ) {
					showErrorMessage( '${eco:translate("Photo list: Please, select group operation first")}' );
					return false;
				}

				var controlSelectedPhotoIds = $( '#${controlSelectedPhotoIds}:checked' );
				if ( controlSelectedPhotoIds.length == 0 ) {
					showErrorMessage( '${eco:translate("Photo list: Please, select at least one photo first")}' );
					return false;
				}

				var form = $( '#${groupOperationForm}' );
				form.submit();

				return true;
			}

		</script>

		<div class="footerseparatorsmall"></div>
	</c:if>

</eco:form>