<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoId" type="java.lang.Integer" required="true" %>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<div class="user-team-container" style="float: left; padding: 5px; width: 300px;"></div>

<style type="text/css">

	.user-team-member-details {
		display: inline-block;
		width: 280px;
		padding-left: 23px;
		padding-top: 10px;
		padding-bottom: 5px;
		line-height: 20px;
	}

	.user-team-list-entry {
		float: left;
		width: 100%;
		padding-top: 2px;
		padding-bottom: 2px;
		margin-bottom: 2px;
		border-bottom: 1px dashed #848078;
	}
</style>

<script type="text/javascript">
	require( ['modules/photo/upload/userTeam/user-team'], function ( userTeam ) {
		userTeam( ${photoId}, "${baseUrl}", $( '.user-team-container' ) );
	} );
</script>