<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userId" required="true" type="java.lang.Integer" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserTeamNewMemberLink( userId )%>"/>

<a href="${link}">
    <jsp:doBody/>
</a>
