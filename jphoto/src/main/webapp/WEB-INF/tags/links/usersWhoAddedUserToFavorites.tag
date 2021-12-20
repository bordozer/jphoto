<%@ tag import="com.bordozer.jphoto.core.services.translator.nerds.LinkNerdText" %>
<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUsersQtyWhoAddedInFavoriteMembersLink( user.getId() )%>"/>

<c:set var="userNameEscaped" value="${eco:escapeHtml(user.name)}"/>
<c:set var="linkNerdText" value="<%=LinkNerdText.USER_STATISTICS_THE_USER_IS_ADDED_IN_FAVORITE_MEMBERS_BY%>"/>

<a href="${link}" title="${eco:translate1(linkNerdText.title, userNameEscaped)}">${eco:translate(linkNerdText.text)}</a>
