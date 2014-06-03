<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>

<c:set var="photoNameEscaped" value="${eco:escapeHtml(photo.name)}" />
<c:set var="title" value="${eco:translate1('Photo card link: $1', photoNameEscaped)}" />

<links:photoCard id="${photo.id}">
	<img src="${eco:baseUrl()}/download/photos/${photo.id}/preview/" class="photo-preview-image" style="vertical-align: middle;" title="${title}" />
</links:photoCard>
