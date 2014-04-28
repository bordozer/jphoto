<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<c:set var="photoId" value="${photoEditDataModel.photoId}"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">
	<div class="photo-edit-container"></div>

	<script type="text/javascript">

		renderPhotoEditForm();

		function renderPhotoEditForm() {

			require( [ 'jquery', 'modules/photo/edit/photo-edit'], function ( $, photoEdit ) {
				photoEdit( ${photoId}, '${eco:baseUrl()}', $( '.photo-edit-container' ) );
			} );
		}

	</script>

</tags:page>