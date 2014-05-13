<%@ tag import="utils.UserUtils" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="entryType" required="true" type="core.enums.FavoriteEntryType" %>

<c:set var="showAddUserToFavoritesIcon" value="<%=UserUtils.isCurrentUserLoggedUser() && ! UserUtils.isUsersEqual( user, EnvironmentContext.getCurrentUser() )%>" />

<c:if test="${showAddUserToFavoritesIcon}">
	<icons:faforiteIcon favoriteEntry="${user}" entryType="${entryType}" />
</c:if>