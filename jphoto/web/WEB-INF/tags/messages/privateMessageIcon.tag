<%@ tag import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="messages" tagdir="/WEB-INF/tags/messages" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="privateMessageType" required="true" type="core.enums.PrivateMessageType" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPrivateMessagesList( user.getId(), privateMessageType )%>" />

<a href="${link}">
	<html:img32 src="messages/${privateMessageType.icon}" alt="${eco:translate(privateMessageType.name)}" />
</a>
