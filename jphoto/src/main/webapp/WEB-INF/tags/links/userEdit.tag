<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserEditLink( user.getId() )%>"/>

<a href="${link}" title="${eco:translate1('Links: Edit member data: $1', user.name)}">
    <jsp:doBody/>
</a>
