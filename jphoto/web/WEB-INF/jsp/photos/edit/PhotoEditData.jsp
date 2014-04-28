<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<c:set var="photoId" value="${photoEditDataModel.photoId}"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

	<div class="floatleft">

		<div class="floatleft text-centered">

			<div class="floatleft" style="width: 300px;">
				<img src="${eco:baseUrl()}/download/file/?filePath=${photoEditDataModel.tempPhotoFile}" alt="Photo file" height="250px">
			</div>

			<form:form modelAttribute="photoEditDataModel">

				<%--<div class="photo-edit-container"></div>--%>

			</form:form>

		</div>
	</div>

	<%--<script type="text/javascript">

		renderPhotoEditForm();

		function renderPhotoEditForm() {

			require( [ 'jquery', 'modules/photo/edit/photo-data'], function ( $, photoEdit ) {
				photoEdit( ${photoId}, '${eco:baseUrl()}', $( '.photo-edit-container' ) );
			} );
		}

	</script>--%>

</tags:page>