<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ tag import="com.bordozer.jphoto.utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>
<%@ attribute name="entryType" required="true" type="com.bordozer.jphoto.core.enums.FavoriteEntryType" %>
<%@ attribute name="iconSize" required="true" type="java.lang.Integer" %>
<%@ attribute name="iconIndex" required="false" type="java.lang.Integer" %>

<c:if test="${empty iconIndex}">
    <c:set var="iconIndex" value="1"/>
</c:if>

<c:set var="showAddUserToFavoritesIcon"
       value="<%=UserUtils.isCurrentUserLoggedUser() && ! UserUtils.isUsersEqual( user, EnvironmentContext.getCurrentUser() )%>"/>

<c:if test="${showAddUserToFavoritesIcon}">
    <icons:faforiteIcon favoriteEntry="${user}" entryType="${entryType}" iconSize="${iconSize}" iconIndex="${iconIndex}"/>
</c:if>
