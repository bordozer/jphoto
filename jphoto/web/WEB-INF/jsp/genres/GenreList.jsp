<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="genreListModel" type="ui.controllers.genres.GenreListModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<c:set var="genreListEntries" value="${genreListModel.genreListEntries}" />

<tags:page pageModel="${genreListModel.pageModel}">

	<div style="width: 70%; margin-left: auto; margin-right: auto; padding-top: 50px;">

		<c:forEach var="genreListEntry" items="${genreListEntries}">

			<div style="display: inline-block; width: 30%;">
				<h3>${genreListEntry.genreNameTranslated}</h3>
			</div>

		</c:forEach>

	</div>

</tags:page>