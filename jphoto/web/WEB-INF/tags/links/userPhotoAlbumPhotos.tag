<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userPhotoAlbum" required="true" type="core.general.user.userAlbums.UserPhotoAlbum" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserPhotoAlbumPhotosLink( userPhotoAlbum.getUser().getId(), userPhotoAlbum.getId() )%>" />

<c:set var="albumNameEscaped" value="${eco:escapeHtml(userPhotoAlbum.name)}" />
<c:set var="userNameEscaped" value="${eco:escapeHtml(userPhotoAlbum.user.name)}" />

<a href ="${link}" title="${eco:translate2("Member $1, photo album '$2'", userNameEscaped, albumNameEscaped)}">${albumNameEscaped}</a>