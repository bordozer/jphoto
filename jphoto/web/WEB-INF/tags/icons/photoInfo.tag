<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoInfoLink( photo.getId() )%>" />
<c:set var="photoName" value="${eco:escapeHtml(photo.name)}"/>

<a href="#" onclick="openPopupWindowCustom( '${link}', 'photoInfo_${photo.id}', 460, 700, 100, 100 ); return false;">
	<html:img id="photoInfoIcon" src="info16.png" width="" height="" alt="${eco:translate('Info')} ${photoName}"/>
</a>
