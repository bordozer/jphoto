<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="genreListModel" type="ui.controllers.genres.GenreListModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<c:set var="genreListEntries" value="${genreListModel.genreListEntries}" />

<tags:page pageModel="${genreListModel.pageModel}">

	<div class="row row-bottom-padding-10">

		<c:set var="counter" value="0" />

		<c:forEach var="genreListEntry" items="${genreListEntries}">

			<c:if test="${counter > 2}">
				</div>
				<div class="row row-bottom-padding-10">

				<c:set var="counter" value="0" />
			</c:if>

			<div class="col-lg-4">

				<div class="panel panel-info">

					<div class="panel-heading">
						<h3 class="panel-title">
							<links:genrePhotos genre="${genreListEntry.genre}"/>
						</h3>
					</div>

					<div class="panel-body text-center" style="height: 350px;">
						<a href="${genreListEntry.photosByGenreURL}">
							<img src="${genreListEntry.photoPreviewWrapper.photoPreviewImgUrl}" alt="" title="${genreListEntry.genreIconTitle}">
						</a>
					</div>

					<div class="panel-footer">
						${eco:translate("Genre list: photos in genre count")}: ${genreListEntry.photosCount}
					</div>

				</div>

			</div>

			<c:set var="counter" value="${counter + 1}" />

		</c:forEach>

	</div>

</tags:page>