<%@ page import="ui.services.ajax.AjaxService" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="restrictionModel" type="admin.controllers.restriction.RestrictionModel" scope="request"/>

<c:set var="entryId" value="${restrictionModel.entryId}" />
<c:set var="baseUrl" value="${eco:baseUrl()}" />

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "ajaxService", ApplicationContextHelper.<AjaxService>getBean( AjaxService.BEAN_NAME ) );
%>
<c:set var="restrictionEntryTypeId" value="${restrictionModel.restrictionEntryType.id}" />

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

		.restriction-area-header {
			float:left;
			width: 100%;
			text-align: center;
		}

		.restriction-area-tab {
			border-top-left-radius: 5px;
			border-top-right-radius: 5px;
		}

		.restriction-history-entry-container {
			float: left;
			width: 97%;
			text-align: left;
			margin-bottom: 5px;
			margin-top: 5px;
			padding: 5px;
		}

		.restriction-history-entry-header {
			border-right: none;
			border-left: none;
			border-top: none;
			padding-bottom: 2px;
			margin-bottom: 5px;
		}

	</style>

</head>

<body>

	<div class="restriction-area-header">

		<div style="float: left; width: 300px; margin-right: 15px;">
			<div class="restriction-area-header block-shadow block-background restriction-area-tab" style="height: 20px;">
				${eco:translate1('Restriction: Range form title: New restriction of $1', restrictionModel.entryName)}
			</div>

			<div id="new-restriction-form" >
				<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
			</div>

		</div>

		<div style="float: right; width: 400px;">
			<div class="restriction-area-header block-shadow block-background restriction-area-tab" style="height: 20px;">
				${eco:translate('Restriction: Restriction history title')}
			</div>
			<div id="restriction-history-container" >
				<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
			</div>

		</div>

	</div>

	<script type="text/javascript" src="${baseUrl}/js/lib/jsonrpc.js"></script>

	<script type="text/javascript">

		var jsonRPC = new JSONRpcClient( "${baseUrl}/JSON-RPC" );

		require( ['modules/admin/restriction/restriction/restriction'], function ( func ) {
			var translations = {
				timePeriod: "${eco:translate('Time period component: Time period')}"
				, dateRange: "${eco:translate('Time period component: Date range')}"
				, buttonTitle: "${eco:translate1('Restriction: Do restriction $1 button title', restrictionModel.entryName)}"
				, hoursUnit: "${eco:translate('Time period component: hours')}"
				, daysUnit: "${eco:translate('Time period component: days')}"
				, daysMonth: "${eco:translate('Time period component: month')}"
				, daysYear: "${eco:translate('Time period component: year')}"
			};

			var restrictionTypes = ${restrictionModel.restrictionTypes};

			func( ${entryId}, ${restrictionEntryTypeId}, restrictionTypes, translations, $( '#new-restriction-form' ), jsonRPC.ajaxService );
		} );

		require( ['modules/admin/restriction/restriction-history/restriction-history'], function ( func ) {
			var translations = {
				restrictionDuration: "${eco:translate('Restriction history: Restriction duration')}"
				, expiresAfter: "${eco:translate('Restriction history: Expires after')}"
				, createdBy: "${eco:translate('Restriction history: Created by')}"
				, restrictedAtTime: "${eco:translate('Restriction history: restricted at time')}"
				, cancel: "${eco:translate('Restriction history: cancel restriction')}"
				, cancelTitle: "${eco:translate('Restriction history: cancel title')}"
				, deleteRestriction: "${eco:translate('Restriction history: delete restriction')}"
				, deleteTitle: "${eco:translate('Restriction history: delete title')}"
				, cancelledBy: "${eco:translate('Restriction history: cancelled by')}"
				, cancelledAtTime: "${eco:translate('Restriction history: cancelled at time')}"
				, wasRestrictedTitle: "${eco:translate('Restriction history: was restricted title')}"
				, cancelConfirmation: "${eco:translate('Restriction history: cancel confirmation')}"
				, deleteConfirmation: "${eco:translate('Restriction history: was delete confirmation')}"
			};
			func( ${entryId}, translations, "${baseUrl}", $( '#restriction-history-container' ) );
		} );

	</script>

</body>
</html>
