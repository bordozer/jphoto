<%@ tag import="com.bordozer.jphoto.core.enums.FavoriteEntryType" %>
<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserFavoriteBlackListLink( user.getId() )%>"/>

<c:set var="userNameEscaped" value="${eco:escapeHtml(user.name)}"/>
<c:set var="favoriteEntryTypeName" value="<%=FavoriteEntryType.MEMBERS_INVISIBILITY_LIST.getName()%>"/>

<a href="${link}" title="${eco:translate1("Links: authors invisible list of $1", userNameEscaped)}">${eco:translate(favoriteEntryTypeName)}</a>
