<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoMarksListLink( photo.getId() )%>" />

<c:set var="buttonTitle" value="${eco:translate1('$1 - votes', eco:escapeHtml(photo.name))}" />

<a href ="${link}" title="${buttonTitle}">
	<jsp:doBody />
</a>