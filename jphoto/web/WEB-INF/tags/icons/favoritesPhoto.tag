<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="core.general.user.User" %>
<%@ tag import="utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>
<%@ attribute name="entryType" required="true" type="core.enums.FavoriteEntryType" %>

<%
	final User currentUser = EnvironmentContext.getCurrentUser();

	final boolean isLoggedUser = UserUtils.isCurrentUserLoggedUser();
	boolean showAddToFavoritesIcon = isLoggedUser && ! UserUtils.isUserOwnThePhoto( currentUser, photo );
%>

<c:set var="currentUser" value="<%=currentUser%>"/>

<c:set var="isLoggedUser" value="<%=isLoggedUser%>"/>
<c:set var="showAddToFavoritesIcon" value="<%=showAddToFavoritesIcon%>"/>
<c:set var="entryType" value="<%=entryType%>"/>

<c:set var="photoName" value="${eco:escapeHtml(photo.name)}"/>

<c:if test="${showAddToFavoritesIcon}">
	<icons:faforiteIcon favoriteEntry="${photo}" entryType="${entryType}" entryName="${photo.name}" />
</c:if>

