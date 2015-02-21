<%@ tag import="ui.controllers.photos.groupoperations.PhotoGroupOperationModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="photoList" required="true" type="ui.elements.PhotoList" %>

<c:set var="totalPhotos" value="${photoList.photosCountToShow}"/>

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

<eco:form action="${formAction}" formName="${groupOperationForm}">

	<style type="text/css">
		.photo-spinning-wheel {
			width: 16px;
			height: 16px;
			margin-left: auto;
			margin-right: auto;
			margin-top: 150px;
		}
	</style>

	<a name="${photoList.photoListId}"></a>

	<div class="row">
		<c:if test="${showPaging and totalPhotos > 0}">
			<tags:paging showSummary="false"/>
		</c:if>
	</div>

	<div class="panel panel-default">

		<photo:photoListHeader photoList="${photoList}" />

		<div class="panel-body photo-list-photos">

			<c:if test="${totalPhotos == 0}">
				${eco:translate(photoList.noPhotoText)}
			</c:if>

			<c:forEach var="photoId" items="${photoList.photoIds}" varStatus="status">

				<c:set var="photoId" value="${photoId}" />

				<div class="photo-container-${photoList.photoListId}-${photoId}">
					<div class="photo-spinning-wheel">
						<html:spinningWheel16 title="${eco:translate('The photo is being loaded...')}" />
					</div>
				</div>

			</c:forEach>

		</div>

		<div class="panel-footer">

			<c:if test="${totalPhotos > 0}">

				<c:if test="${showPaging}">
					<tags:paging showSummary="true"/>
				</c:if>

				<div class="row row-bottom-padding-10">
					<div class="col-lg-10">
						<photo:photoListBottomText bottomText="${photoList.bottomText}" photosCriteriasDescription="${photoList.photosCriteriasDescription}" />
					</div>

					<div class="col-lg-2 text-right">
						<photo:photoAllBestLink linkToFullList="${photoList.linkToFullList}" linkToFullListText="${eco:translate(photoList.linkToFullListText)}" />
					</div>
				</div>
			</c:if>
			
			<c:if test="${not empty photoList.hiddenPhotoIds}">
				<span class="show-hidden-photo-link-container">
					${eco:translate1( 'Photo list: there are $1 hidden photo(s)', fn:length(photoList.hiddenPhotoIds))}
					<a href="#" onclick="renderHiddenPhotos(); return false;">${eco:translate('PhotoList: Show hidden photos')}</a>
				</span>
			</c:if>

			<c:if test="${isGroupOperationEnabled}">

				<div class="row row-bottom-padding-10">

					<js:confirmAction />

					<c:set var="controlSelectedPhotoIds" value="<%=PhotoGroupOperationModel.FORM_CONTROL_SELECTED_PHOTO_IDS%>"/>
					<c:set var="controlPhotoGroupOperationId" value="<%=PhotoGroupOperationModel.FORM_CONTROL_PHOTO_GROUP_OPERATION_ID%>" />

					<js:checkboxMassChecker checkboxClass="${controlSelectedPhotoIds}" />

					<label for="${controlPhotoGroupOperationId}">${eco:translate('Photo list: Group operations with selected photos')}</label>

					<select id="${controlPhotoGroupOperationId}" name="${controlPhotoGroupOperationId}">
						<option value="" selected="selected"></option>
						<c:forEach var="photoGroupOperationMenu" items="${photoGroupOperationMenues}">
							<c:set var="photoGroupOperation" value="${photoGroupOperationMenu.photoGroupOperation}"/>
							<option value="${photoGroupOperation.id}">${eco:translate(photoGroupOperation.name)}</option>
						</c:forEach>
					</select>

					<html:submitButton id="ok" caption_t="Photo list: Do group operations" onclick="return submitForm();" />

					<script type="text/javascript">

						function submitForm() {
							var groupOperationSelect = $( '#${controlPhotoGroupOperationId}' );
							if ( groupOperationSelect.val() == '' || groupOperationSelect.val() == -1 ) {
								showUIMessage_Error( '${eco:translate("Photo list: Please, select group operation first")}' );
								return false;
							}

							var controlSelectedPhotoIds = $( '#${controlSelectedPhotoIds}:checked' );
							if ( controlSelectedPhotoIds.length == 0 ) {
								showUIMessage_Error( '${eco:translate("Photo list: Please, select at least one photo first")}' );
								return false;
							}

							var form = $( '#${groupOperationForm}' );
							form.submit();

							return true;
						}

					</script>

				</div> <%-- / group operations --%>

			</c:if>

		</div> <%-- / panel footer--%>

	</div> <%-- / panel--%>

	<script type="text/javascript">

		var photosToRender = [];

		<c:forEach var="photoId" items="${photoList.photoIds}" varStatus="status">
			photosToRender.push( ${photoId} );
		</c:forEach>

		renderPhotos( photosToRender );

		function renderPhotos( photosToRender ) {

			require( [ 'jquery' ], function ( $ ) {
				for ( var i = 0; i < photosToRender.length; i++ ) {
					var photoId = photosToRender[i];
					var photoContainer = $( '.photo-container-' + ${photoList.photoListId} +'-' + photoId );
					renderPhoto( photoId, photoContainer );
				}
			} );
		}

		function renderPhoto( photoId, container ) {

			var displayOptions = {
				groupOperationEnabled: ${isGroupOperationEnabled}
			};

			require( [ 'jquery', 'modules/photo/list/entry/photo-list-entry'], function ( $, render ) {
				render( photoId, ${photoList.photoListId}, displayOptions, container );
			} );
		}

		function renderHiddenPhotos() {
			var photoIds = ${photoList.hiddenPhotoIdsToJSArray};

			require( [ 'jquery' ], function ( $ ) {
				var photosContainer = $( '.photo-list-photos' );

				for ( var i = 0; i < photoIds.length; i++ ) {
					var photoId = photoIds[i];
					var photoContainer = $( "<div class='photo-container-" + ${photoList.photoListId} +'-' + photoId + "'></div>" );
					photosContainer.append( photoContainer )

					renderPhoto( photoId, photoContainer );
				}

				$( '.show-hidden-photo-link-container' ).hide();
			} );
		}

	</script>

</eco:form>