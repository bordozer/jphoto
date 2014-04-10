<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.enums.FavoriteEntryType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserBookmarkedPhotosLink( user.getId() )%>" />

<c:set var="userNameEscaped" value="${eco:escapeHtml(user.name)}" />
<c:set var="favoriteEntryTypeName" value="<%=FavoriteEntryType.BOOKMARKED_PHOTOS.getName()%>"/>

<a href ="${link}" title="${eco:translate1("$1\'s bookmarked photos", userNameEscaped)}">${eco:translate(favoriteEntryTypeName)}</a>