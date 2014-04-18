<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="photoListModel" type="ui.controllers.photos.list.PhotoListModel" scope="request"/>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<tags:page pageModel="${photoListModel.pageModel}">

	<script type="text/javascript" src="${baseUrl}/js/require-config.js.jsp"></script>
	<script type="text/javascript" src="${baseUrl}/js/lib/front-end/require.js"></script>

	<link rel="stylesheet" href="${baseUrl}/css/photo-list.css" type="text/css"/>

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
