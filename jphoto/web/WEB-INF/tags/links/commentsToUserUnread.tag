<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUnreadCommentsToUserList( user.getId() )%>" />

<c:set var="userNameEscaped" value="${eco:escapeHtml(user.name)}" />

<c:set var="text" value="${eco:translate('Received unread comments')}" />

<a href ="${link}" title="${eco:translate1("$1: received but unread comments", userNameEscaped)}">${text}</a>