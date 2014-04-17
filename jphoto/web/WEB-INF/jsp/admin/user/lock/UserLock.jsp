<%@ page import="ui.services.ajax.AjaxService" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="userLockModel" type="admin.controllers.user.lock.UserLockModel" scope="request"/>

<c:set var="userId" value="${userLockModel.userId}" />
<c:set var="baseUrl" value="${eco:baseUrl()}" />

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "ajaxService", ApplicationContextHelper.<AjaxService>getBean( AjaxService.BEAN_NAME ) );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<script type="text/javascript" src="${baseUrl}/js/lib/jquery/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="${baseUrl}/js/lib/date.format.js"></script>

	<script type="text/javascript" src="${baseUrl}/js/require-config.js.jsp"></script>
	<script type="text/javascript" src="${baseUrl}/js/lib/front-end/require.js"></script>

	<link href="${baseUrl}/css/jphoto.css" rel="stylesheet" type="text/css"/>

	<style type="text/css">

		.user-lock-area-header {
			float:left;
			width: 100%;
			text-align: center;
		}

		.user-lock-area-tab {
			border-top-left-radius: 5px;
			border-top-right-radius: 5px;
		}

		.user-lock-area {
			float: left;
			width: 45%;
			margin: 5px;
		}
	</style>

</head>

<body>

<div class="user-lock-area-header">
<div class="user-lock-area">
		<div class="user-lock-area-header block-background user-lock-area-tab">Lock user</div>

		<div id="user-lock-area-form-id" >
			<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
		</div>

	</div>

	<div class="user-lock-area" style="float: right;">
		<div class="user-lock-area-header block-background user-lock-area-tab">Locking history</div>
		<div id="user-lock-history-id" >
			<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
		</div>

	</div>
</div>

<script type="text/javascript" src="${baseUrl}/js/lib/jsonrpc.js"></script>

<script type="text/javascript">

	var jsonRPC;
	jQuery().ready( function() {

		jsonRPC = new JSONRpcClient( "${baseUrl}/JSON-RPC" );

		require( ['components/time-range/time-range'], function ( timeRange ) {
			timeRange( ${userId}, jsonRPC.ajaxService, $( '#user-lock-area-form-id' ) );
		} );

		require( ['modules/admin/user/lock/user-lock'], function ( userLock ) {
			userLock( ${userLockModel.userId}, "${baseUrl}", $( '#user-lock-history-id' ) );
		} );

	});

</script>

</body>
</html>
