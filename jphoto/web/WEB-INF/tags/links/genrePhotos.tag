<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotosByGenreLink( genre.getId() )%>" />

<c:set var="genreNameTranslated" value="${eco:translateGenre(genre.id)}"/>

<c:set var="buttonTitle" value="${eco:translate1('Show photos: $1', genreNameTranslated)}" />

<a href ="${link}" title="${buttonTitle}">${genreNameTranslated}</a>