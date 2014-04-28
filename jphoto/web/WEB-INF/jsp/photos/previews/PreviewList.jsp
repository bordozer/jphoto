<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<jsp:useBean id="photoPreviewsModel" type="ui.controllers.photos.previews.PhotoPreviewsModel" scope="request"/>

<tags:page pageModel="${photoPreviewsModel.pageModel}">

	<c:set var="photo" value="${photoPreviewsModel.photo}" />
	<c:set var="photoPreviews" value="${photoPreviewsModel.photoPreviews}" />

	<div class="photoPreviewLight">
		<photo:photoPreviewWithNudeControl photoPreviewWrapper="${photoPreviewsModel.photoPreviewWrapper}" />
	</div>

	<table:table>
		<c:forEach var="photoPreview" items="${photoPreviews}">
	    	<table:tr>

				<table:tdunderlined>
					<user:userCard user="${photoPreview.user}"/>
				</table:tdunderlined>

				<table:tdunderlined>${eco:formatDate(photoPreview.previewTime)} ${eco:formatTimeShort(photoPreview.previewTime)}</table:tdunderlined>

	    	</table:tr>
		</c:forEach>

		<table:tr>
			<table:td>${eco:translate('Total:')}</table:td>
			<table:td>${fn:length(photoPreviews)}</table:td>
		</table:tr>

	</table:table>

</tags:page>

