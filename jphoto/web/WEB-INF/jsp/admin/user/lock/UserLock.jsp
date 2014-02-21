<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Test mobile page</title>
	<meta name="keywords" content=""/>
	<meta name="description" content=""/>

	<link href="<c:url value="/common/css/jphoto-mobile.css"/>" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<c:url value="/common/js/lib/front-end/css/jquery.mobile-1.4.0.css"/>"/>

	<%-- Dependensies configuration --%>
	<script data-main="<c:url value="/common/js/modules/admin/user/lock/require-config-user-lock.js"/>" src="<c:url value="/common/js/lib/front-end/require.js"/>"></script>

</head>
<body>

<div class='userLockAreaDiv'>

</div>

</body>
</html>
