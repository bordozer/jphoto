<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userTeamMember" required="true" type="com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember" %>

<c:set var="link"
       value="<%=ApplicationContextHelper.getUrlUtilsService().getUserTeamMemberDeleteLink( userTeamMember.getUser().getId(), userTeamMember.getId() )%>"/>

<a href="${link}">
    <jsp:doBody/>
</a>
