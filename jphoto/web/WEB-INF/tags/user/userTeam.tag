<%@ tag import="ui.translatable.GenericTranslatableList" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="org.json.JSONArray" %>
<%@ tag import="org.json.JSONObject" %>
<%@ tag import="ui.translatable.GenericTranslatableEntry" %>
<%@ tag import="java.util.List" %>
<%@ tag import="static com.google.common.collect.Lists.newArrayList" %>
<%@ tag import="rest.users.team.UserTeamTranslationDTO" %>
<%@ tag import="core.services.translator.Language" %>
<%@ tag import="core.services.translator.TranslatorService" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userId" type="java.lang.Integer" required="true" %>
<%@ attribute name="selectedByDefaultUserMembersIds" type="java.util.List" required="false" %>
<%@ attribute name="groupSelectionClass" type="java.lang.String" required="false" %>
<%@ attribute name="onEditJSFunction" type="java.lang.String" required="false" %>
<%@ attribute name="onDeleteJSFunction" type="java.lang.String" required="false" %>

<%
	final Language language = EnvironmentContext.getLanguage();
	final TranslatorService translatorService = ApplicationContextHelper.getTranslatorService();

	final List<GenericTranslatableEntry> entries = GenericTranslatableList.userTeamMemberTypeList( language, translatorService ).getEntries();

	final List<JSONObject> jsonObjects = newArrayList();
	for ( final GenericTranslatableEntry entry : entries ) {
		jsonObjects.add( new JSONObject( entry ) );
	}
	final JSONArray userTeamMemberTypes = new JSONArray( jsonObjects );
	final JSONArray selectedUserTeamMemberIdsJson = new JSONArray( selectedByDefaultUserMembersIds );
%>

<%
	final UserTeamTranslationDTO translationDTO = new UserTeamTranslationDTO( translatorService, language );
%>

<c:set var="baseUrl" value="${eco:baseUrl()}" />
<c:set var="userTeamMemberTypes" value="<%=userTeamMemberTypes%>" />
<c:set var="selectedUserTeamMemberIdsJson" value="<%=selectedUserTeamMemberIdsJson%>" />
<c:set var="translationDTO" value="<%=new JSONObject( translationDTO )%>" />

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
	require( ['components/editableList/entries/team/user-team'], function ( userTeam ) {

		var userTeamMemberTypes = ${userTeamMemberTypes};
		var selectedUserTeamMemberIds = ${selectedUserTeamMemberIdsJson};
		var translationDTO = ${translationDTO};

		userTeam( { userId: ${userId}
			, container: $( '.user-team-container' )
			, userTeamMemberTypes: userTeamMemberTypes
			, selectedUserTeamMemberIds: selectedUserTeamMemberIds
			, groupSelectionClass: '${groupSelectionClass}'
			, translationDTO: translationDTO
			, onEdit: ${onEditJSFunction}
			, onDelete: ${onDeleteJSFunction}
		  } );
	} );
</script>























