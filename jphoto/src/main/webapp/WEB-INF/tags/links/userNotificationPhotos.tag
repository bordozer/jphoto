<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUsersNewPhotosNotificationMenuLink( user.getId() )%>"/>

<c:set var="userNameEscaped" value="${eco:escapeHtml(user.name)}"/>

<c:set var="text" value="${eco:translate('Links: $1: Notifications about new photos')}"/>

<a href="${link}" title="${eco:translate1("Links: $1: notifications about new photos", userNameEscaped)}">${text}</a>
