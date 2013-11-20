<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="userTeam" required="true" type="core.general.user.userTeam.UserTeam" %>
<%@ attribute name="teamMemberPhotosQtyMap" required="true" type="java.util.Map" %>

<c:set var="cssClass" value="textright"/>
<c:set var="width" value="50%"/>

<c:set var="userTeamMembers" value="${userTeam.userTeamMembers}" />

	<table:table width="100%" oddEven="true">

		<table:separatorInfo colspan="3" title="${eco:translate('Team')}" />

		<c:if test="${not empty userTeamMembers}">

			<c:forEach var="userTeamMember" items="${userTeamMembers}">

				<table:tr>

					<table:td cssClass="${cssClass}" width="${width}">
						<links:userTeamMemberCard userTeamMember="${userTeamMember}"/>
					</table:td>

					<table:tdicon>
						<icons:teamMemberType teamMemberType="${userTeamMember.teamMemberType}" />
					</table:tdicon>

					<table:td>
						${teamMemberPhotosQtyMap[userTeamMember]}
					</table:td>

				</table:tr>

			</c:forEach>

		</c:if>

		<c:if test="${empty userTeamMembers}">
			<table:tr>
				<table:td colspan="2" cssClass="textcentered">
					${eco:translate('No team')}
				</table:td>
			</table:tr>
		</c:if>

	</table:table>