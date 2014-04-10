<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoEditLink( photo.getId() )%>" />

<a href="${link}"><jsp:doBody/></a>