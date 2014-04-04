<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="core.enums.FavoriteEntryType" %>
<%@ tag import="utils.UserUtils" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="core.general.user.User" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="hideIconToFavoriteUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconToFriend" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconNewPhotoNotification" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconToBlackList" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconSendPrivateMessage" required="false" type="java.lang.Boolean" %>

<c:if test="${not hideIconToFavoriteUser}">
	<icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.FAVORITE_MEMBERS%>" />
</c:if>

<c:if test="${not hideIconToFriend}">
	<icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.FRIENDS%>" />
</c:if>

<c:if test="${not hideIconNewPhotoNotification}">
	<icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.NEW_PHOTO_NOTIFICATION%>" />
</c:if>

<c:if test="${not hideIconToBlackList}">
	<icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.BLACKLIST%>" />
</c:if>

<%
	final User currentUser = EnvironmentContext.getCurrentUser();
	final boolean isLoggedUser = UserUtils.isCurrentUserLoggedUser();
%>

<c:set var="showIcon" value="<%=isLoggedUser && ! UserUtils.isUsersEqual( user, currentUser )%>"/>

<c:if test="${not hideIconSendPrivateMessage and showIcon}">
	<icons:sendPrivateMessage toUser="${user}" />
</c:if>

