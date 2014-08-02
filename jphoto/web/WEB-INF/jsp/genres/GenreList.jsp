<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="genreListModel" type="ui.controllers.genres.GenreListModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<c:set var="genreListEntries" value="${genreListModel.genreListEntries}" />

<tags:page pageModel="${genreListModel.pageModel}">

	<div style="width: 90%; margin-left: auto; margin-right: auto; padding-top: 50px;">

		<c:forEach var="genreListEntry" items="${genreListEntries}">

			<div class="block-background block-shadow" style="display: inline-block; width: 30%; text-align: center; vertical-align: top; height: 400px;  border: 1px dotted #cccccc; margin: 10px;">

				<div style="float: left; width: 100%; height: 370px;">
					<h2 class=""><links:genrePhotos genre="${genreListEntry.genre}"/></h2>
					<a href="${genreListEntry.photosByGenreURL}">
						<img src="${genreListEntry.photoPreviewWrapper.photoPreviewImgUrl}" alt="" title="${genreListEntry.genreIconTitle}">
					</a>
				</div>

				<div style="float: left; width: 100%; text-align: left; padding-left: 30px;">
					${eco:translate("Genre list: photos in genre count")}: ${genreListEntry.photosCount}
				</div>

			</div>

		</c:forEach>

	</div>

</tags:page>