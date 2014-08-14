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

			var restrictionTypesUser = ${restrictionListModel.restrictionTypesUser};
			var restrictionTypesPhoto = ${restrictionListModel.restrictionTypesPhoto};
			var restrictionStatuses = ${restrictionListModel.restrictionStatuses};

			func( restrictionTypesUser, restrictionTypesPhoto, restrictionStatuses, $( '#restriction-list-container' ) );
		} );

	</script>

</tags:page>