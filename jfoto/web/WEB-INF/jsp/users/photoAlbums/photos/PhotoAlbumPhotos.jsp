<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="userPhotoAlbumPhotosModel" type="controllers.users.photoAlbums.photos.UserPhotoAlbumPhotosModel" scope="request" />

<tags:page pageModel="${userPhotoAlbumPhotosModel.pageModel}">

	<tags:entryMenuJs />

	<photo:photoList photoList="${userPhotoAlbumPhotosModel.photoList}" />

</tags:page>