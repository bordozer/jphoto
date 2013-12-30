<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userNotificationsControlModel" class="controllers.users.notifications.UserNotificationsControlModel" scope="request"/>

<tags:page pageModel="${userNotificationsControlModel.pageModel}">

	User Notifications Control

</tags:page>