<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="translations" tagdir="/WEB-INF/tags/links/translations" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<jsp:useBean id="genreListModel" type="admin.controllers.genres.list.GenreListModel" scope="request"/>

<c:set var="genres" value="<%=genreListModel.getGenreList()%>"/>
<c:set var="defaultMinimalMarksForGenreBest" value="<%=genreListModel.getSystemMinMarksToBeInTheBestPhotoOfGenre()%>"/>
<c:set var="separatorHeight" value="1"/>

<c:set var="newGenreButtonHint" value="${eco:translate( 'Create new genre' )}"/>
<c:set var="deleteGenreButtonHint" value="${eco:translate( 'Delete genre' )}"/>
<c:set var="cannotdeleteGenreButtonHint" value="${eco:translate( 'Deletion is not accessible' )}"/>

<tags:page pageModel="${genreListModel.pageModel}" >

	<links:genreNew>
		<html:img32 src="add32.png" alt="${newGenreButtonHint}" />
	</links:genreNew>

	<translations:genres>
		<html:img32 src="icons32/translate.png" alt="${eco:translate('Genre list: Genres translations')}" />
	</translations:genres>

	<br/>
	<br/>

	<c:set var="colspan" value="9"/>

	<table:table width="950">

		<jsp:attribute name="thead">
			<table:td>${eco:translate( "" )}</table:td>
			<table:td>${eco:translate( "" )}</table:td>
			<table:td>${eco:translate( "id" )}</table:td>
			<table:td width="200">${eco:translate( "Genre list: Member name" )}</table:td>

			<table:td>${eco:translate( "Genre list: Can contain nude" )}</table:td>
			<table:td>${eco:translate( "Genre list: Contains nude" )}</table:td>

			<table:td>${eco:translate( "Genre list: Photos Qty" )}</table:td>
			<table:td>${eco:translate( "Genre list: Min marks" )}</table:td>
			<table:td>${eco:translate( "Genre list: Voting categories" )}</table:td>
		</jsp:attribute>

		<jsp:body>
			<table:separator colspan="${colspan}" height="${separatorHeight}"/>

			<c:forEach var="genre" items="${genres}">

				<c:set var="photosQty" value="${genreListModel.photosByGenreMap[genre.id]}" />

				<table:tr>

					<table:tdicon>
						<links:genreEdit id="${genre.id}">
							<html:img id="editGenreIcon" src="edit16.png" width="16" height="16" alt="${eco:translate('Edit')}" />
						</links:genreEdit>
					</table:tdicon>

					<table:tdicon>
						<c:if test="${photosQty == 0}">
							<links:genreDelete id="${genre.id}">
								<html:img id="deleteGenreIcon" src="delete16.png" width="16" height="16" alt="${deleteGenreButtonHint}" onclick="return confirmDeletion( 'Delete ${eco:translateGenre(genre.id)}?' );" />
							</links:genreDelete>
						</c:if>

						<c:if test="${photosQty > 0}">
							<html:img id="cannotDeleteGenreIcon" src="cannotdelete.png" width="16" height="16" alt="${cannotdeleteGenreButtonHint}" onclick="alert('${eco:translate('The genre has uploaded photos! Can not be deleted.')}');" />
						</c:if>
					</table:tdicon>

					<table:tdicon>${genre.id}</table:tdicon>
					<table:td>
						<links:genrePhotos genre="${genre}"/>
						<br />
						${genre.name}
					</table:td>

					<table:td cssClass="text-centered">${genre.canContainNudeContent ? "Yes" : ""}</table:td>
					<table:td cssClass="text-centered">${genre.containsNudeContent ? "Yes" : ""}</table:td>

					<table:td cssClass="textright">${photosQty} &nbsp;</table:td>
					<table:td cssClass="text-centered">${genre.minMarksForBest > 0 ? genre.minMarksForBest : eco:translate('System default')} &nbsp;</table:td>
					<table:td>
						<ul>
							<c:forEach var="photoVotingCategory" items="${genre.photoVotingCategories}">
								<li>${eco:translateVotingCategory(photoVotingCategory.id)}</li>
							</c:forEach>
						</ul>
					</table:td>
				</table:tr>

				<table:separator colspan="${colspan}" height="${separatorHeight}"/>

			</c:forEach>
		</jsp:body>
	</table:table>

	<br />

	${eco:translate('System default minimal marks needed to be in the best photos in a photo category')}: <b>${defaultMinimalMarksForGenreBest}</b>

	<js:confirmAction />

</tags:page>