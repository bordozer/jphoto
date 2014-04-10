<%@ tag import="core.services.entry.FavoritesService" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.general.user.User" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="entryType" required="true" type="core.enums.FavoriteEntryType" %>

<%
	final FavoritesService favoritesService = ApplicationContextHelper.getBean( FavoritesService.BEAN_NAME );
	final User currentUser = EnvironmentContext.getCurrentUser();

	final boolean isLoggedUser = UserUtils.isCurrentUserLoggedUser();
	final boolean showAddToFavoritesIcon = isLoggedUser && ! UserUtils.isUsersEqual( user, currentUser );
	final boolean isEntryInUserFavorites = isLoggedUser && favoritesService.isEntryInFavorites( currentUser.getId(), user.getId(), entryType.getId() );
%>

<c:set var="showAddUserToFavoritesIcon" value="<%=showAddToFavoritesIcon%>" />
<c:set var="isEntryInUserFavorites" value="<%=isEntryInUserFavorites%>" />

<c:set var="userName" value="${eco:escapeHtml(user.name)}"/>

<c:if test="${showAddUserToFavoritesIcon}">
	<icons:faforiteIcon favoriteEntry="${user}" entryType="${entryType}" isEntryInUserFavorites="${isEntryInUserFavorites}" entryName="${userName}" />
</c:if>