<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="restrictionListModel" type="admin.controllers.restriction.list.RestrictionListModel" scope="request"/>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<tags:page pageModel="${restrictionListModel.pageModel}">

	<link href="${baseUrl}/css/restriction.css" rel="stylesheet" type="text/css"/>

	<%--<form:checkboxes path="restrictions" items="selectedRestrictions" itemValue="id" itemLabel="name" />--%>

	<div style="float: right; width: 400px;">
		<div id="restriction-history-container" >
			<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
		</div>
	</div>

	<script type="text/javascript">

		require( ['modules/admin/restriction/restriction-history/restriction-history'], function ( func ) {

			var filter = {
				entryId: 138137
				, restrictionEntryTypeId: 2
			};

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

			func( filter, translations, "${baseUrl}", $( '#restriction-history-container' ) );
		} );

	</script>

</tags:page>