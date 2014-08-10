<%@ page import="ui.services.ajax.AjaxService" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ page import="ui.translatable.GenericTranslatableList" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page import="ui.translatable.GenericTranslatableEntry" %>
<%@ page import="java.util.List" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="static com.google.common.collect.Lists.newArrayList" %>
<%@ page import="rest.admin.restriction.RestrictionTypeDTO" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="userRestrictionModel" type="admin.controllers.user.restriction.UserRestrictionModel" scope="request"/>

<c:set var="userId" value="${userRestrictionModel.userId}" />
<c:set var="baseUrl" value="${eco:baseUrl()}" />

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "ajaxService", ApplicationContextHelper.<AjaxService>getBean( AjaxService.BEAN_NAME ) );
	final List<GenericTranslatableEntry> restrictionTypes = GenericTranslatableList.restrictionUserTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries();

	final List<JSONObject> jsonObjects = newArrayList();
	for ( final GenericTranslatableEntry restrictionType : restrictionTypes ) {
		jsonObjects.add( new JSONObject( new RestrictionTypeDTO( restrictionType.getId(), restrictionType.getName() ) ) );
	}

	final JSONArray restrictionTypesJSON = new JSONArray( jsonObjects );
%>
<c:set var="restrictionTypesJSON" value="<%=restrictionTypesJSON%>" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<script type="text/javascript" src="${baseUrl}/js/require-config.js.jsp"></script>
	<script type="text/javascript" src="${baseUrl}/js/lib/front-end/require.js"></script>

	<script type="text/javascript" src="${baseUrl}/js/lib/jquery/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="${baseUrl}/js/lib/date.format.js"></script>

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

	</style>

</head>

<body>

	<div class="user-lock-area-header">

		<div style="float: left; width: 300px; margin-right: 15px;">
			<div class="user-lock-area-header block-background user-lock-area-tab">${eco:translate1('User restriction: Range form title: New restriction of $1', userRestrictionModel.userName)}</div>

			<div id="user-lock-form" >
				<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
			</div>

		</div>

		<div style="float: right; width: 400px;">
			<div class="user-lock-area-header block-background user-lock-area-tab">${eco:translate('User restriction: Restriction history title')}</div>
			<div id="user-lock-history" >
				<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
			</div>

		</div>

	</div>

	<script type="text/javascript" src="${baseUrl}/js/lib/jsonrpc.js"></script>

	<script type="text/javascript">

		var jsonRPC = new JSONRpcClient( "${baseUrl}/JSON-RPC" );

		require( ['modules/admin/user/restriction/user-restriction'], function ( func ) {
			var translations = {
				timePeriod: "${eco:translate('Time period component: Time period')}"
				, dateRange: "${eco:translate('Time period component: Date range')}"
				, buttonTitle: "${eco:translate1('User restriction: Do restriction $1 button title', userRestrictionModel.userName)}"
				, hoursUnit: "${eco:translate('Time period component: hours')}"
				, daysUnit: "${eco:translate('Time period component: days')}"
				, daysMonth: "${eco:translate('Time period component: month')}"
				, daysYear: "${eco:translate('Time period component: year')}"
			};

			var restrictionTypes = ${restrictionTypesJSON};

			func( ${userId}, restrictionTypes, translations, $( '#user-lock-form' ), jsonRPC.ajaxService );
		} );

		require( ['modules/admin/user/restriction-history/user-lock-history'], function ( userLockHistory ) {
			userLockHistory( ${userId}, "${baseUrl}", $( '#user-lock-history' ) );
		} );

	</script>

</body>
</html>
