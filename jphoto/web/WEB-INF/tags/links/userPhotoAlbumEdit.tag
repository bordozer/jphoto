<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userId" required="true" type="java.lang.Integer" %>
<%@ attribute name="albumId" required="true" type="java.lang.Integer" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserPhotoAlbumEditLink( userId, albumId )%>" />

<a href ="${link}">
	<jsp:doBody/>
</a>