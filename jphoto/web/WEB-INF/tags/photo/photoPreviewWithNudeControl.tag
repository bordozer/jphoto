<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<%@ attribute name="photoPreviewWrapper" required="true" type="core.general.photo.PhotoPreviewWrapper" %>

<c:set var="photo" value="${photoPreviewWrapper.photo}"/>
<c:set var="photoPreviewHasToBeHiddenBecauseOfNudeContent" value="${photoPreviewWrapper.photoPreviewHasToBeHiddenBecauseOfNudeContent}"/>

<c:set var="photoNameEscaped" value="${eco:escapeHtml(photo.name)}" />
<c:set var="title" value="${eco:translate1('Photo: $1', photoNameEscaped)}" />

<links:photoCard id="${photo.id}">
	<c:if test="${not photoPreviewHasToBeHiddenBecauseOfNudeContent}">
		<img src="${photoPreviewWrapper.photoPreviewImgUrl}" class="photo-preview-image" style="vertical-align: middle;" title="${title}" />
	</c:if>
	<c:if test="${photoPreviewHasToBeHiddenBecauseOfNudeContent}">
		<photo:nudeContentPreview />
	</c:if>
</links:photoCard>

<br />
<br />

<photo:photoCard photo="${photo}" />

