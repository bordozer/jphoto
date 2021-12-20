<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="com.bordozer.jphoto.core.general.photo.Photo" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoMarksListLink( photo.getId() )%>"/>

<c:set var="buttonTitle" value="${eco:translate1('Links: $1 - photo preview, set marks link title', eco:escapeHtml(photo.name))}"/>

<a href="${link}" title="${buttonTitle}">
    <jsp:doBody/>
</a>
