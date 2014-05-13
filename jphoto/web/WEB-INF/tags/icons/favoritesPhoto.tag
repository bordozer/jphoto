<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>
<%@ attribute name="entryType" required="true" type="core.enums.FavoriteEntryType" %>

<c:set var="showAddToFavoritesIcon" value="<%=UserUtils.isCurrentUserLoggedUser() && ! UserUtils.isUserOwnThePhoto( EnvironmentContext.getCurrentUser(), photo )%>"/>

<c:if test="${showAddToFavoritesIcon}">
	<icons:faforiteIcon favoriteEntry="${photo}" entryType="${entryType}" />
</c:if>

