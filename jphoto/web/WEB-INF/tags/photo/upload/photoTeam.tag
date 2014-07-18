<%@ tag import="ui.translatable.GenericTranslatableList" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="org.json.JSONArray" %>
<%@ tag import="org.json.JSONObject" %>
<%@ tag import="ui.translatable.GenericTranslatableEntry" %>
<%@ tag import="java.util.List" %>
<%@ tag import="static com.google.common.collect.Lists.newArrayList" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoId" type="java.lang.Integer" required="true" %>

<%
	final List<GenericTranslatableEntry> entries = GenericTranslatableList.userTeamMemberTypeList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries();

	final List<JSONObject> jsonObjects = newArrayList();
	for ( final GenericTranslatableEntry entry : entries ) {
		jsonObjects.add( new JSONObject( entry ) );
	}
	final JSONArray userTeamMemberTypes = new JSONArray( jsonObjects );
%>

<c:set var="baseUrl" value="${eco:baseUrl()}" />
<c:set var="userTeamMemberTypes" value="<%=userTeamMemberTypes%>" />

<div class="user-team-container" style="float: left; padding: 5px; width: 400px;"></div>

<style type="text/css">

	.user-team-member-details {
		display: inline-block;
		width: 280px;
		/*padding-left: 23px;*/
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
		var userTeamMemberTypes = ${userTeamMemberTypes};
		userTeam( ${photoId}, "${baseUrl}", $( '.user-team-container' ), userTeamMemberTypes );
	} );
</script>