<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotosByGenreLink( genre.getId() )%>" />

<c:set var="buttonTitle" value="${eco:translate1('Show photos: $1', genre.name)}" />

<a href ="${link}" title="${buttonTitle}">${genre.name}</a>