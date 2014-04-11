<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="core.services.translator.nerds.LinkNerdText" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getWrittenComments( user.getId() )%>" />

<c:set var="userNameEscaped" value="${eco:escapeHtml(user.name)}" />
<c:set var="linkNerdText" value="<%=LinkNerdText.USER_STATISTICS_COMMENTS_WRITTEN%>"/>

<a href ="${link}" title="${eco:translate1(linkNerdText.title, userNameEscaped)}">${eco:translate(linkNerdText.text)}</a>