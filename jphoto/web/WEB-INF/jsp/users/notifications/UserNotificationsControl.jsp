<%@ page import="core.general.user.EmailNotificationType" %>
<%@ page import="ui.controllers.users.notifications.UserNotificationsControlModel" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="core.services.utils.UrlUtilsService" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userNotificationsControlModel" class="ui.controllers.users.notifications.UserNotificationsControlModel" scope="request"/>

<%
	final UrlUtilsService urlUtilsService = ApplicationContextHelper.getBean( UrlUtilsService.BEAN_NAME );
%>
<c:set var="photosWithSubscribeOnNewCommentsLink" value="<%=urlUtilsService.getPhotosWithSubscribeOnNewCommentsLink( userNotificationsControlModel.getUser().getId() )%>"/>
<c:set var="usersNewPhotosNotificationMenuLink" value="<%=urlUtilsService.getUsersNewPhotosNotificationMenuLink( userNotificationsControlModel.getUser().getId() )%>"/>
<c:set var="userFavoriteFriendsLink" value="<%=urlUtilsService.getUserFavoriteFriendsLink( userNotificationsControlModel.getUser().getId() )%>"/>

<tags:page pageModel="${userNotificationsControlModel.pageModel}">

	<c:set var="emailNotificationTypes" value="<%=EmailNotificationType.values()%>"/>
	<c:set var="emailNotificationTypeIdsControl" value="<%=UserNotificationsControlModel.EMAIL_NOTIFICATION_TYPE_IDS_FORM_CONTROL%>"/>

	<form:form modelAttribute="userNotificationsControlModel" method="POST">

		<table:table width="600">

			<table:separatorInfo colspan="2" title="${eco:translate('Email Notifications')}" />

			<table:tr>
				<table:td colspan="2">
					<form:checkboxes path="${emailNotificationTypeIdsControl}" items="${emailNotificationTypes}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />" />
				</table:td>
			</table:tr>

			<table:separatorInfo colspan="3" title="${eco:translate('Activity Notifications')}" />

			<table:tr>
				<table:td><a href="${photosWithSubscribeOnNewCommentsLink}">${eco:translate("New comments of the photos I\'m tracking which")}</a></table:td>
				<table:td>${userNotificationsControlModel.photoQtyWhichCommentsUserIsTracking}</table:td>
			</table:tr>

			<table:tr>
				<table:td><a href="${usersNewPhotosNotificationMenuLink}">${eco:translate("New photo of the member I'm tracking who's")}</a></table:td>
				<table:td>${userNotificationsControlModel.usersQtyWhoNewPhotoUserIsTracking}</table:td>
			</table:tr>

			<table:tr>
				<table:td><a href="${userFavoriteFriendsLink}">${eco:translate("Your friends")}</a></table:td>
				<table:td>${userNotificationsControlModel.friendsQty}</table:td>
			</table:tr>

			<table:trok text_t="Save" />

		</table:table>

	</form:form>

</tags:page>