<%@ page import="utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="userPhotoAlbumListModel" type="ui.controllers.users.photoAlbums.list.UserPhotoAlbumListModel" scope="request" />

<c:set var="canEdit" value="<%=UserUtils.isTheUserThatWhoIsCurrentUser( userPhotoAlbumListModel.getUser() )%>" />

<tags:page pageModel="${userPhotoAlbumListModel.pageModel}">

	<c:set var="user" value="${userPhotoAlbumListModel.user}" />
	<c:set var="userPhotoAlbums" value="${userPhotoAlbumListModel.userPhotoAlbums}" />
	<c:set var="userPhotoAlbumsQtyMap" value="${userPhotoAlbumListModel.userPhotoAlbumsQtyMap}" />

	<links:userPhotoAlbumNew userId="${user.id}">
		<html:img id="addUserPhotoAlbum" src="add32.png" width="32" height="32" alt="${eco:translate('Create new photo album')}" />
	</links:userPhotoAlbumNew>

	<table:table width="700">

		<jsp:attribute name="thead">
			<c:if test="${canEdit}">
				<table:td />
				<table:td />
			</c:if>

			<table:td width="200">${eco:translate( "Album name" )}</table:td>
			<table:td>${eco:translate( "Description" )}</table:td>
			<table:td>${eco:translate( "Photos" )}</table:td>
		</jsp:attribute>

		<jsp:body>

			<c:forEach var="userPhotoAlbum" items="${userPhotoAlbums}">
				<table:tr>

					<c:if test="${canEdit}">
						<table:tdicon>
							<links:userPhotoAlbumEdit userId="${user.id}" albumId="${userPhotoAlbum.id}">
								<html:img id="album_${userPhotoAlbum.id}" src="edit16.png" width="16" height="16" />
							</links:userPhotoAlbumEdit>
						</table:tdicon>

						<table:tdicon>
							<links:userPhotoAlbumDelete userId="${user.id}" albumId="${userPhotoAlbum.id}">
								<html:img id="delete_album_${userPhotoAlbum.id}" src="delete16.png" width="16" height="16" onclick="return deleteUserPhotoAlbum();" />
							</links:userPhotoAlbumDelete>
						</table:tdicon>
					</c:if>

					<table:td>
						<links:userPhotoAlbumPhotos userPhotoAlbum="${userPhotoAlbum}" />
					</table:td>

					<table:td>${eco:escapeHtml(userPhotoAlbum.description)}</table:td>

					<table:td width="30" cssClass="textcentered">${userPhotoAlbumsQtyMap[userPhotoAlbum.id]}</table:td>

				</table:tr>
			</c:forEach>

		</jsp:body>

	</table:table>

	<script type="text/javascript">
		function deleteUserPhotoAlbum() {
			return confirm( "${eco:translate('Delete photo album?')}" );
		}
	</script>

</tags:page>