<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="photoListModel" type="ui.controllers.photos.list.PhotoListModel" scope="request"/>

<tags:page pageModel="${photoListModel.pageModel}">

	<style type="text/css">

		.photo-list-container {
			float: left;
			width: 98%;
			padding: 20px;
			/*border: 1px solid #880000;*/
		}

		.photo-list-title {
			float: left;
			width: 97%;
			height: 30px;
			border-top-left-radius: 5px;
			border-top-right-radius: 5px;
			padding-left: 25px;
			padding-top: 10px;
			font-size: 16px;
			font-weight: bold;
		}

		.photo-container {
			width: 200px;
			height: 250px;
			display: inline-block;
			margin: 20px;
			border: 1px solid #3388cc;
		}

	</style>

	<tags:entryMenuJs />

	<c:set var="user" value="${photoListModel.user}" />

	<c:set var="userPhotosByGenres" value="${photoListModel.userPhotosByGenres}" />
	<c:set var="showUserPhotoGenres" value="${not empty user && not empty userPhotosByGenres}" />

	<c:if test="${showUserPhotoGenres}">
		<photo:photosByUserByGenre user="${user}" userPhotosByGenres="${userPhotosByGenres}" />
	</c:if>

	<c:forEach var="photoList" items="${photoListModel.photoLists}">
		<%--<photo:photoList photoList="${photoList}" />--%>
		<photo:photoListPostponedLoading photoList="${photoList}" />
	</c:forEach>

	<c:if test="${showUserPhotoGenres}">
		<photo:photosByUserByGenre user="${user}" userPhotosByGenres="${userPhotosByGenres}" />
	</c:if>

	<c:if test="${photoListModel.showPhotoSearchForm}">
		<div class="floatleft">
			<photo:photoFilter />
		</div>
	</c:if>

	<div class="footerseparator"></div>

</tags:page>
