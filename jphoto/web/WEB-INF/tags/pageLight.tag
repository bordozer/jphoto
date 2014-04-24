<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="title" required="true" type="java.lang.String" %>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<%-- TODO:  --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>${title}</title>
	<meta name="keywords" content=""/>
	<meta name="description" content=""/>

	<script type="text/javascript" src="${baseUrl}/js/require-config.js.jsp"></script>
	<script type="text/javascript" src="${baseUrl}/js/lib/front-end/require.js"></script>

	<link rel="stylesheet" type="text/css" href="${baseUrl}/css/jphoto.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrl}/js/lib/jquery/css/smoothness/jquery-ui-1.10.4.custom.css"/>

</head>
<body>

	<script type="text/javascript">
		var jsonRPC;
		require( ['jsonrpc'], function() {
			jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );
		} );
	</script>

	<jsp:doBody />

</body>
</html>