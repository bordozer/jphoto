<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoInfo" required="true" type="core.general.photo.PhotoInfo" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoPreviewsListLink( photoInfo.getPhoto().getId() )%>" />

<c:set var="buttonTitle" value="${eco:translate1('$1 - previews', eco:escapeHtml(photoInfo.photo.name))}" />

<a href ="${link}" title="${buttonTitle}">${photoInfo.previewCount}</a>