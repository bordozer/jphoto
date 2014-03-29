<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="teamMemberType" required="true" type="core.enums.UserTeamMemberType" %>
<c:set var="teamMemberTypeName" value="${eco:translate(teamMemberType.name)}"/>

<html:img id="utm_${teamMemberType.id}" src="userTeamMemberTypeIcons/${teamMemberType.icon}" width="16" height="16" alt="${teamMemberTypeName}" />