<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotosByUserLink( user.getId() )%>" />

<a class="photos-by-user-by-genre-link" href ="${link}" title="${eco:translate1("$1: show all photos", eco:escapeHtml(user.name))}">${eco:translate("All photos")}</a>