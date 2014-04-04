<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.enums.FavoriteEntryType" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserFavoriteFriendsLink( user.getId() )%>" />

<c:set var="userNameEscaped" value="${eco:escapeHtml(user.name)}" />
<c:set var="favoriteEntryTypeName" value="<%=FavoriteEntryType.FRIEND.getName()%>"/>

<a href ="${link}" title="${eco:translate1("Friends of $1", userNameEscaped)}">${eco:translate(favoriteEntryTypeName)}</a>