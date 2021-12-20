<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="photo" required="true" type="com.bordozer.jphoto.core.general.photo.Photo" %>

<c:set var="photoName" value="${eco:escapeHtml(photo.name)}"/>

<links:photoEdit photo="${photo}">
    <html:img id="photoEditIcon" src="edit16.png" width="" height="" alt="${eco:translate1('Edit \\\'$1\\\'', photoName)}"/>
</links:photoEdit>
