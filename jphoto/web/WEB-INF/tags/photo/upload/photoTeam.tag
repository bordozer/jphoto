<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoId" type="java.lang.Integer" required="true" %>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<div class="user-team block-border" style="float: left; padding: 5px; width: 300px;"></div>

<style type="text/css">
	.user-team-member-details {
		display: inline-block;
		width: 280px;
		padding-left: 23px;
		padding-top: 10px;
		padding-bottom: 10px;
		border-left: none;
		border-right: none;
		line-height: 20px;
		/*border-bottom: none;*/
	}
</style>

<script type="text/javascript">
	require( ['modules/photo/upload/userTeam/user-team'], function ( userTeam ) {
		userTeam( ${photoId}, "${baseUrl}", $( '.user-team' ) );
	} );
</script>