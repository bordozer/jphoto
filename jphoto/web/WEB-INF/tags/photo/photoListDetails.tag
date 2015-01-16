<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<%@ attribute name="photoList" required="true" type="ui.elements.PhotoList" %>

<c:set var="showPaging" value="${photoList.showPaging}"/>
<c:set var="totalPhotos" value="${photoList.photosCountToShow}"/>

<c:if test="${showPaging}">
	<tags:paging showSummary="false"/>
</c:if>

<div class="photo-list-container">

	<photo:photoListHeader photoList="${photoList}"/>

	<div class="empty-photo-list-text">
		<c:if test="${totalPhotos == 0}">
			${eco:translate(photoList.noPhotoText)}
		</c:if>
	</div>

	<div style="float: left; width: 100%; text-align: center;">
		<c:forEach var="photoId" items="${photoList.photoIds}" varStatus="status">
			<div class="photo-container-${photoList.photoListId}-${photoId}"></div>
		</c:forEach>
	</div>

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

		require( ['modules/photo/list-big/photo-list-big'], function ( photoListEntry ) {
			for ( var i = 0; i < photosToRender.length; i++ ) {
				var photoId = photosToRender[i];
				var photoUniqueClass = 'photo-container-' + ${photoList.photoListId} +'-' + photoId;
				photoListEntry( photoId, ${photoList.photoListId}, $( '.' + photoUniqueClass ) );
			}
		} );
	}

</script>
