<%@ page import="utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<jsp:useBean id="userTeamMemberListModel" type="ui.controllers.users.team.list.UserTeamMemberListModel" scope="request" />

<c:set var="userTeamMembers" value="${userTeamMemberListModel.userTeamMembers}" />
<c:set var="userTeamMemberPhotosQtyMap" value="${userTeamMemberListModel.userTeamMemberPhotosQtyMap}" />
<c:set var="canEdit" value="<%=UserUtils.isTheUserThatWhoIsCurrentUser( userTeamMemberListModel.getUser() )%>" />

<tags:page pageModel="${userTeamMemberListModel.pageModel}">

	<links:userTeamMemberNew userId="${userTeamMemberListModel.user.id}">
		<html:img id="addUserTeamMember" src="add32.png" width="32" height="32" alt="${eco:translate('Create new team member')}" />
	</links:userTeamMemberNew>

	<br/>
	<br/>

	<table:table width="800">

		<jsp:attribute name="thead">
			<c:if test="${canEdit}">
				<table:td />
				<table:td />
			</c:if>

			<table:td>${eco:translate( "Avatar" )}</table:td>
			<table:td>${eco:translate( "Member name" )}</table:td>
			<table:td>${eco:translate( "Member" )}</table:td>
			<table:td />
			<table:td>${eco:translate( "Type" )}</table:td>
			<table:td>${eco:translate( "Photos qty" )}</table:td>
		</jsp:attribute>

		<jsp:body>

			<c:forEach var="userTeamMember" items="${userTeamMembers}">

				<c:set var="userTeamMemberPhotosQty" value="${userTeamMemberPhotosQtyMap[userTeamMember.id]}" />

				<table:tr>

					<c:if test="${canEdit}">
						<table:tdicon>
							<links:userTeamMemberEdit userTeamMember="${userTeamMember}">
								<html:img id="member_${userTeamMember.id}" src="edit16.png" width="16" height="16" />
							</links:userTeamMemberEdit>
						</table:tdicon>

						<table:tdicon>
							<c:if test="${userTeamMemberPhotosQty == 0}">
								<links:userTeamMemberDelete userTeamMember="${userTeamMember}">
									<html:img id="delete_member_${userTeamMember.id}" src="delete16.png" width="16" height="16" onclick="return deleteUserTeamMember();" />
								</links:userTeamMemberDelete>
							</c:if>

							<c:if test="${userTeamMemberPhotosQty > 0}">
								<icons:canNotDelete hint="${eco:translate('You have already assigned this member to one or more photos')}"/>
							</c:if>
						</table:tdicon>
					</c:if>

					<table:td cssClass="textcentered">

						<c:if test="${not empty userTeamMember.teamMemberUser}">
							${eco:userAvatarImage(userTeamMember.teamMemberUser.id, 70, 70, '', '', '')}
						</c:if>

						<c:if test="${empty userTeamMember.teamMemberUser}">
							<img src="${eco:imageFolderURL()}/imagenotfound.png" height="50" />
						</c:if>

					</table:td>

					<table:td>
						<links:userTeamMemberCard userTeamMember="${userTeamMember}"/>
					</table:td>

					<table:td>
						<c:if test="${not empty userTeamMember.teamMemberUser}">
							<user:userCard user="${userTeamMember.teamMemberUser}" />
						</c:if>
					</table:td>

					<table:tdicon>
						<icons:teamMemberType teamMemberType="${userTeamMember.teamMemberType}" />
					</table:tdicon>

					<table:td cssClass="textcentered">
						${userTeamMember.teamMemberType.nameTranslated}
					</table:td>

					<table:td cssClass="textcentered">
						${userTeamMemberPhotosQty}
					</table:td>

				</table:tr>

			</c:forEach>

		</jsp:body>

	</table:table>

	<script type="text/javascript">
		function deleteUserTeamMember() {
			return confirm( "${eco:translate('Delete user team member?')}" );
		}
	</script>

</tags:page>