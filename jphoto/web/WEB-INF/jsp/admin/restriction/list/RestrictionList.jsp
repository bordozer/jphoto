<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="restrictionListModel" type="admin.controllers.restriction.list.RestrictionListModel" scope="request"/>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<tags:page pageModel="${restrictionListModel.pageModel}">

	<link href="${baseUrl}/css/restriction.css" rel="stylesheet" type="text/css"/>

	<div style="float: left; width: 100%;">
		<div id="restriction-list-container" >
			<img src="${eco:imageFolderURL()}/progress.gif" title="Please, wait...">
		</div>
	</div>

	<script type="text/javascript">

		require( ['modules/admin/restriction/list/restriction-list'], function ( func ) {

			var translations = {
				filterButtonTitle: "${eco:translate('Restriction filter form: Filter button title')}"
				, emptySearchResultText: "${eco:translate('Restriction filter form: Empty Search Result Text')}"
				, filterByTypeTitle: "${eco:translate('Restriction filter form: Filter by type title')}"
				, filterByStatusTitle: "${eco:translate('Restriction filter form: Filter by status title')}"
			};

			var restrictionTypes = ${restrictionListModel.restrictionTypes};
			var restrictionStatuses = ${restrictionListModel.restrictionStatuses};

			func( restrictionTypes, restrictionStatuses, translations, $( '#restriction-list-container' ) );
		} );

	</script>

</tags:page>