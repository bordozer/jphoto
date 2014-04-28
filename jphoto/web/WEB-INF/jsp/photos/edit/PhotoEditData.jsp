<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.DEL_edit.PhotoEditDataModel" scope="request"/>

<c:set var="photoId" value="" />

<div class="photo-edit-container"></div>


<script type="text/javascript">

	function renderPhotos( photosToRender ) {

		require( ['modules/photo/edit/photo-edit'], function ( photoEdit ) {
			for ( var i = 0; i < photosToRender.length; i++ ) {

				photoEdit( ${photoId}, '${eco:baseUrl()}', $( '.photo-edit-container' ) );
			}
		} );
	}

</script>