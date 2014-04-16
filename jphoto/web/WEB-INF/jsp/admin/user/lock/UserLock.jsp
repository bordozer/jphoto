<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="userLockModel" type="admin.controllers.user.lock.UserLockModel" scope="request"/>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<script type="text/javascript" src="${baseUrl}/common/js/require-config.js.jsp"></script>
	<script type="text/javascript" src="${baseUrl}/common/js/lib/front-end/require.js"></script>
</head>

<body>

<script type="text/javascript">

	require( ['modules/admin/user/lock/user-lock'], function ( userLock ) {
		userLock( ${userLockModel.userId}, "${baseUrl}" );
	} );

</script>

</body>
</html>
