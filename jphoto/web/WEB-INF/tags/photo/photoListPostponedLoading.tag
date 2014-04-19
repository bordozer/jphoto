<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

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

<eco:form action="${formAction}" formName="${groupOperationForm}">

	<icons:favoritesJS />

	<c:if test="${showPaging}">
		<tags:paging showSummary="false"/>
	</c:if>

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

		<c:forEach var="photoInfo" items="${photoList.photoInfos}" varStatus="status">

			<c:set var="photoId" value="${photoInfo.photo.id}" />

			<div class="photo-container block-border block-shadow block-background photo-container-${photoId}">
				<div style="width: 16px; height: 16px; margin-left: auto; margin-right: auto; margin-top: 110px;">
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

		<c:forEach var="photoInfo" items="${photoList.photoInfos}" varStatus="status">
			photosToRender.push( ${photoInfo.photo.id} );
		</c:forEach>

		renderPhotos( photosToRender );

		function renderPhotos( photosToRender ) {

			require( ['modules/photo/list/photo-list'], function ( photoListEntry ) {
				for (var i = 0; i < photosToRender.length; i++) {
					var photoId = photosToRender[i];
					photoListEntry( photoId, '${eco:baseUrl()}', $( '.photo-container-' + photoId ) );
				}
			} );
		}

	</script>

</eco:form>