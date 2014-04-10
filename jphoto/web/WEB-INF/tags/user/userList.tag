<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="users" required="true" type="java.util.List" %>
<%@ attribute name="userListDataMap" required="true" type="java.util.Map" %>
<%@ attribute name="showEditIcons" required="true" type="java.lang.Boolean" %>

<icons:favoritesJS />

<c:set var="colspan" value="12" />

<tags:entryMenuJs />

<table:table id="user_list_table" width="100%" oddEven="true">

		<jsp:attribute name="thead">
			<c:if test="${showEditIcons}">
				<table:td>&nbsp;</table:td>
			</c:if>
			<table:td>${eco:translate( "id" )}</table:td>
			<table:td>${eco:translate( "Avatar" )}</table:td>
			<table:td>${eco:translate( "Member name" )}</table:td>
			<table:td />
			<table:td>${eco:translate( "Status" )}</table:td>
			<%--<table:td>${eco:translate( "Login" )}</table:td>--%>
			<%--<table:td>${eco:translate( "Email" )}</table:td>--%>
			<table:td>${eco:translate( "Birthday" )}</table:td>
			<table:td>${eco:translate( "Gender" )}</table:td>
			<table:td>${eco:translate( "Membership type" )}</table:td>
			<table:td>${eco:translate( "Photos in line" )}</table:td>
			<table:td>${eco:translate( "Photos Qty" )}</table:td>
		</jsp:attribute>

	<jsp:body>

		<c:forEach var="user" items="${users}">

			<c:set var="userListData" value="${userListDataMap[user.id]}"/>

			<c:set var="photosByUser" value="${userListData.photosByUser}"/>
			<c:set var="userAvatar" value="${userListData.userAvatar}"/>
			<c:set var="userMenu" value="${userListData.userMenu}"/>

			<table:tr>
				<c:if test="${showEditIcons}">
					<table:tdicon cssClass="member_list_column_edit_icon">
						<links:userEdit user="${user}">
							<html:img id="editUserIcon" src="edit16.png" width="16" height="16"/> <%-- TODO: remove EDIT icons from here --%>
						</links:userEdit>
					</table:tdicon>
				</c:if>

				<table:tdicon cssClass="member_list_column_member_id">${user.id}</table:tdicon>

				<table:tdicon>
					<c:set var="isUserHasAvatar" value="${not empty userAvatar && userAvatar.hasAvatar}" />

					<c:if test="${isUserHasAvatar}">
						<img id="member_avatar_img_${user.id}" src="${userAvatar.userAvatarFileUrl}" alt="${eco:escapeHtml(user.name)}" height="50">
					</c:if>

					<c:if test="${not isUserHasAvatar}">
						<c:set var="noAvatar" value="noAvataBoy.png" />
						<c:if test="${user.gender == 'FEMALE'}">
							<c:set var="noAvatar" value="noAvatarGirl.png" />
						</c:if>
						<html:img id="member_default_avatar_${user.id}" src="icons48/${noAvatar}" width="48" height="48" />
					</c:if>
				</table:tdicon>
				<table:td cssClass="member_list_column_member_card_link"><user:userCard user="${user}" /></table:td>

				<table:td>
					<icons:userIcons user="${user}"
									 hideIconSendPrivateMessage="true"
							/>
					<tags:entryMenu entryMenu="${userMenu}" />
				</table:td>

				<table:td cssClass="textcentered">${eco:translate(user.userStatus.name)}</table:td>

				<%--<table:td>${eco:escapeHtml(user.login)}</table:td>--%>
				<%--<table:td>${eco:escapeHtml(user.email)}</table:td>--%>
				<table:td>${eco:formatDate(user.dateOfBirth)}</table:td>
				<table:td cssClass="textcentered">
					<icons:userGender user="${user}" />
				</table:td>
				<table:td cssClass="textcentered">${eco:translate(user.membershipType.name)}</table:td>
				<table:td cssClass="textcentered">${user.photosInLine > 0 ? user.photosInLine : 'auto'}</table:td>
				<table:td cssClass="textright">${photosByUser}</table:td>
			</table:tr>

			<%--<table:separator colspan="${colspan}"/>--%>
		</c:forEach>

	</jsp:body>

</table:table>