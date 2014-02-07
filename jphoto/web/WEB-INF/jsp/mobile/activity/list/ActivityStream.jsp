<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Test mobile page</title>
	<meta name="keywords" content=""/>
	<meta name="description" content=""/>

	<link href="<c:url value="/mobile/css/jphoto-mobile.css"/>" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<c:url value="/mobile/css/jquery.mobile-1.4.0.css"/>"/>

	<%-- Dependensies configuration --%>
	<script data-main="<c:url value="/mobile/js/modules/activity/require-config.js"/>" src="<c:url value="/mobile/js/lib/require.js"/>"></script>

</head>
<body>

	<h3>Hello, mobile activity page</h3>

</body>
</html>
