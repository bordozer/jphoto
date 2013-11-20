<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>

<c:set var="photoName" value="${eco:escapeHtml(photo.name)}"/>

<links:photoDelete photo="${photo}">
	<html:img id="photoDeleteIcon" src="delete16.png" width="" height="" alt="${eco:translate1('Delete \\\'$1\\\'', photoName)}" onclick="return confirmDeletion( 'Delete photo \\'${photoName}\\'?' );" />
</links:photoDelete>