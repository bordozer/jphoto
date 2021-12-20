<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="com.bordozer.jphoto.core.enums.FavoriteEntryType" %>
<%@ tag import="com.bordozer.jphoto.core.general.user.User" %>
<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ tag import="com.bordozer.jphoto.utils.UserUtils" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>
<%@ attribute name="iconSize" required="false" type="java.lang.Integer" %>
<%@ attribute name="hideIconToFavoriteUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconToFriend" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconNewPhotoNotification" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconToBlackList" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconSendPrivateMessage" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideIconPhotoVisibilityInPhotoList" required="false" type="java.lang.Boolean" %>

<c:if test="${empty iconSize}">
    <c:set var="iconSize" value="16"/>
</c:if>

<c:if test="${not hideIconToFavoriteUser}">
    <icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.FAVORITE_MEMBERS%>" iconSize="${iconSize}"/>
</c:if>

<c:if test="${not hideIconToFriend}">
    <icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.FRIENDS%>" iconSize="${iconSize}"/>
</c:if>

<c:if test="${not hideIconNewPhotoNotification}">
    <icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.NEW_PHOTO_NOTIFICATION%>" iconSize="${iconSize}"/>
</c:if>

<c:if test="${not hideIconToBlackList}">
    <icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.BLACKLIST%>" iconSize="${iconSize}"/>
</c:if>

<c:set var="showIcon" value="%>"/>
<%
    final User currentUser = EnvironmentContext.getCurrentUser();
    final boolean isLoggedUser = UserUtils.isCurrentUserLoggedUser();
    final boolean isAdmin = ApplicationContextHelper.getSecurityService().isSuperAdminUser(currentUser);
%>

<c:set var="isAdmin" value="<%=isAdmin%>"/>

<c:if test="${not isAdmin and not hideIconPhotoVisibilityInPhotoList}">
    <icons:favoritesUser user="${user}" entryType="<%=FavoriteEntryType.MEMBERS_INVISIBILITY_LIST%>" iconSize="${iconSize}"/>
</c:if>

<c:set var="showIcon" value="<%=isLoggedUser && ! UserUtils.isUsersEqual( user, currentUser )%>"/>

<c:if test="${not hideIconSendPrivateMessage and showIcon}">
    <icons:sendPrivateMessage toUser="${user}"/>
</c:if>


