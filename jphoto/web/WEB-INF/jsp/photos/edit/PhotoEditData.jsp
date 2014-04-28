<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

	<div class="floatleft">

		<div class="floatleft text-centered">

			<div class="floatleft" style="width: 300px;">
				<img src="${eco:baseUrl()}/download/file/?filePath=${photoEditDataModel.tempPhotoFile}" alt="Photo file" height="250px">
			</div>

			<tags:photoDataEdit photo="${photoEditDataModel.photo}" />



		</div>
	</div>

</tags:page>