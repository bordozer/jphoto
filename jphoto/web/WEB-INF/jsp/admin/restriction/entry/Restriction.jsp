<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="restrictionModel" type="admin.controllers.restriction.entry.RestrictionModel" scope="request"/>

<c:set var="entryId" value="${restrictionModel.entryId}" />
<c:set var="baseUrl" value="${eco:baseUrl()}" />

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
	<link href="${baseUrl}/css/restriction.css" rel="stylesheet" type="text/css"/>

</head>

<body>

	<div class="restriction-area-header">

		<div style="float: left; width: 300px; margin-right: 15px;">
			<div class="restriction-area-header block-shadow block-background restriction-area-tab" style="height: 20px;">
				${eco:translate1('Restriction: New restriction form tab title', restrictionModel.entryName)}
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

	<script type="text/javascript">

		require( ['modules/admin/restriction/restriction/restriction'], function ( func ) {

			var translations = {
				timePeriod: "${eco:translate('Time period component: Time period')}"
				, dateRange: "${eco:translate('Time period component: Date range')}"
				, buttonTitle: "${eco:translate1('Restriction: Do restriction', restrictionModel.entryName)}"
				, hoursUnit: "${eco:translate('Time period component: hours')}"
				, daysUnit: "${eco:translate('Time period component: days')}"
				, daysMonth: "${eco:translate('Time period component: month')}"
				, daysYear: "${eco:translate('Time period component: year')}"
			};

			var restrictionTypes = ${restrictionModel.restrictionTypes};

			func( ${entryId}, ${restrictionEntryTypeId}, restrictionTypes, translations, $( '#new-restriction-form' ) );
		} );

		require( ['modules/admin/restriction/restriction-history/restriction-history'], function ( func ) {

			var filter = {
				entryId: ${entryId}
				, restrictionEntryTypeId: ${restrictionEntryTypeId}
			};

			func( filter, $( '#restriction-history-container' ) );
		} );

	</script>

</body>
</html>
