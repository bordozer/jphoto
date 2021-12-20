<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoInfo" required="true" type="com.bordozer.jphoto.core.general.photo.PhotoInfo" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoPreviewsListLink( photoInfo.getPhoto().getId() )%>"/>

<c:set var="buttonTitle" value="${eco:translate1('Links: $1 - photo preview, previews link title', eco:escapeHtml(photoInfo.photo.name))}"/>

<a href="${link}" title="${buttonTitle}">${photoInfo.previewCount}</a>
