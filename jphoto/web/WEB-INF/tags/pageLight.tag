<%@ tag import="ui.services.ajax.AjaxService" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="org.jabsorb.JSONRPCBridge" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="title" required="true" type="java.lang.String" %>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "ajaxService", ApplicationContextHelper.<AjaxService>getBean( AjaxService.BEAN_NAME ) );
%>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>${title}</title>
	<meta name="keywords" content=""/>
	<meta name="description" content=""/>

	<script type="text/javascript" src="${baseUrl}/js/lib/jsonrpc.js"></script>
	<script type="text/javascript">
		var jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );
	</script>

	<script type="text/javascript" src="${baseUrl}/js/require-config.js.jsp"></script>
	<script type="text/javascript" src="${baseUrl}/js/lib/front-end/require.js"></script>

	<link rel="stylesheet" type="text/css" href="${baseUrl}/css/jphoto.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrl}/js/lib/jquery/css/smoothness/jquery-ui-1.10.4.custom.css"/>

</head>
<body>

	<jsp:doBody />

</body>
</html>