<%@ tag import="core.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="toUser" required="true" type="core.general.user.User" %>
<%@ attribute name="callback" required="false" type="java.lang.String" %>

<c:set var="fromUserId" value="<%=EnvironmentContext.getCurrentUser().getId()%>"/>
<c:set var="toUserId" value="${toUser.id}"/>
<c:set var="userMenuIconId" value="userMenuIconId_${toUserId}"/>

<c:set var="callbackText" value=""/>
<c:if test="${not empty callback}">
	<c:set var="callbackText" value=", ${callback}"/>
</c:if>

<html:img16 src="sendPrivateMessageToUser16.png" alt="${eco:translate('Send private message')}" onclick="sendPrivateMessage( ${fromUserId}, ${toUserId}, '${eco:escapeHtml(toUser.name)}'${callbackText} );"/>