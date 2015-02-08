<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="portalPageModel" type="ui.controllers.portalpage.PortalPageModel" scope="request" />

<tags:page pageModel="${portalPageModel.pageModel}">

	<link rel="Stylesheet" type="text/css" href="${eco:baseUrl()}/js/lib/slick-master/css/slick.css" />
	<link rel="Stylesheet" type="text/css" href="${eco:baseUrl()}/js/lib/slick-master/css/slick-theme.css" />

	<link rel="Stylesheet" type="text/css" href="${eco:baseUrl()}/js/lib/bxslider/jquery.bxslider.css" />

	<div class="row">
		<div class="col-lg-12 portal-page-container">

		</div>
	</div>

	<script type="text/javascript">

		require( [ 'jquery', 'modules/portal/page/portal-page' ], function ( $, portalPage ) {
			portalPage( $( '.portal-page-container') );
		} );

	</script>

</tags:page>
