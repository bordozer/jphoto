<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="genreListModel" type="ui.controllers.genres.GenreListModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<c:set var="genreListEntries" value="${genreListModel.genreListEntries}" />

<tags:page pageModel="${genreListModel.pageModel}">

	<div style="width: 80%; margin-left: auto; margin-right: auto; padding-top: 50px;">

		<c:forEach var="genreListEntry" items="${genreListEntries}">

			<div style="display: inline-block; width: 30%; text-align: center; vertical-align: top;">
				<h2><links:genrePhotos genre="${genreListEntry.genre}"/></h2>
				<a href="${genreListEntry.photosByGenreURL}">
					<img src="${genreListEntry.photoPreviewWrapper.photoPreviewImgUrl}" alt="">
				</a>
			</div>

		</c:forEach>

	</div>

</tags:page>