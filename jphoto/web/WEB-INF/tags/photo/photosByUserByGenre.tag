<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="userPhotosByGenres" required="true" type="java.util.List" %>

<c:if test="${not empty userPhotosByGenres}">

	<c:set var="total" value="0" />
	<c:forEach var="userPhotosByGenre" items="${userPhotosByGenres}">
		<c:set var="total" value="${total + userPhotosByGenre.photosQty}" />
	</c:forEach>

	<div style="float: left; width: 100%; margin: 10px; padding-bottom: 20px; text-align: center;">

		<links:userPhotos user="${user}" /> ( ${total} ) &nbsp;-&nbsp;

		<c:forEach var="userPhotosByGenre" items="${userPhotosByGenres}" varStatus="status">

			<c:set var="genre" value="${userPhotosByGenre.genre}" />
			<c:set var="photosQty" value="${userPhotosByGenre.photosQty}" />

			<links:photosByUserByGenre user="${user}" genre="${genre}"/> ( ${photosQty} )

			<c:if test="${not status.last}">
				&nbsp;-&nbsp;
			</c:if>

		</c:forEach>

	</div>

</c:if>