<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="com.bordozer.jphoto.core.general.photo.Photo" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoInfoLink( photo.getId() )%>"/>

<a href="${link}">
    <jsp:doBody/>
</a>
