<%@ page import="ui.elements.PhotoList" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.google.common.collect.Lists.newArrayList" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="userPhotoAlbumPhotosModel" type="ui.controllers.users.photoAlbums.photos.UserPhotoAlbumPhotosModel" scope="request" />

<%
	final List<PhotoList> photoLists = newArrayList();
	photoLists.add( userPhotoAlbumPhotosModel.getPhotoList() );
%>
<c:set var="photoLists" value="<%=photoLists%>" />

<tags:page pageModel="${userPhotoAlbumPhotosModel.pageModel}">

	<photo:photoListsRender photoLists="${photoLists}" />

</tags:page>