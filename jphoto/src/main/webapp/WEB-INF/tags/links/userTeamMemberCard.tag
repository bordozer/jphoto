<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="userTeamMember" required="true" type="com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember" %>

<c:set var="link"
       value="<%=ApplicationContextHelper.getUrlUtilsService().getUserTeamMemberCardLink( userTeamMember.getUser().getId(), userTeamMember.getId() )%>"/>

<c:set var="teamMemberNameEscaped" value="${eco:escapeHtml(userTeamMember.teamMemberName)}"/>
<c:set var="teamMemberTypeName" value="${eco:translate(userTeamMember.teamMemberType.name)}"/>

<a href="${link}" title="${eco:translate2('Links: The team member of user: $1 - $2', teamMemberNameEscaped, teamMemberTypeName)}">${teamMemberNameEscaped}</a>
