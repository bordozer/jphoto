<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="userPhotosByGenres" required="true" type="java.util.List" %>

<c:set var="separator" value="&nbsp;&middot;&nbsp" />

<c:if test="${not empty userPhotosByGenres}">

	<style type="text/css">
		.user-photos {
			display: inline-block;
			width: auto;
			padding-right: 10px;
			padding-left: 10px;
			text-wrap: avoid;
			height: 25px;
		}
	</style>

	<c:set var="total" value="0" />
	<c:forEach var="userPhotosByGenre" items="${userPhotosByGenres}">
		<c:set var="total" value="${total + userPhotosByGenre.photosQty}" />
	</c:forEach>

	<div class="row text-center">

		<div class="user-photos"><links:userPhotos user="${user}" /> ( ${total} )</div> ${separator}

		<c:forEach var="userPhotosByGenre" items="${userPhotosByGenres}" varStatus="status">

			<c:set var="genre" value="${userPhotosByGenre.genre}" />
			<c:set var="photosQty" value="${userPhotosByGenre.photosQty}" />

			<div class="user-photos">
				<links:photosByUserByGenre user="${user}" genre="${genre}"/> ( ${photosQty} )
			</div>

			<c:if test="${not status.last}">
				${separator}
			</c:if>

		</c:forEach>

	</div>

</c:if>