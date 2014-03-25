<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="genresTranslationsModel" type="admin.controllers.genres.translations.GenresTranslationsModel" scope="request"/>

<tags:page pageModel="${genreListModel.pageModel}">
	Genres translations
</tags:page>