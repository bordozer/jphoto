<%@ tag import="rest.users.albums.UserAlbumTranslationDTO" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="org.json.JSONObject" %>
<%@ tag import="org.json.JSONArray" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="userId" type="java.lang.Integer" required="true" %>
<%@ attribute name="selectedIds" type="java.util.List" required="false" %>
<%@ attribute name="groupSelectionClass" type="java.lang.String" required="false" %>
<%@ attribute name="onEditJSFunction" type="java.lang.String" required="false" %>
<%@ attribute name="onDeleteJSFunction" type="java.lang.String" required="false" %>

<%
	final JSONArray selectedIdsJson = new JSONArray( selectedIds );
	final UserAlbumTranslationDTO translationDTO = new UserAlbumTranslationDTO( ApplicationContextHelper.getTranslatorService(), EnvironmentContext.getLanguage() );
%>
<c:set var="translationDTO" value="<%=new JSONObject( translationDTO )%>" />
<c:set var="selectedIdsJson" value="<%=selectedIdsJson%>" />

<style type="text/css">

	.user-album-details {
		display: inline-block;
		width: 300px;
		/*padding-left: 23px;*/
		padding-top: 10px;
		padding-bottom: 5px;
		line-height: 20px;
	}

	.album-list-entry {
		float: left;
		width: 100%;
		padding-top: 2px;
		padding-bottom: 2px;
		margin-bottom: 2px;
		border-bottom: 1px dashed #848078;
	}
</style>

<div class="user-album-container" style="float: left; padding: 5px; width: 400px;"></div>

<script type="text/javascript">

	require( ['components/user/albums/user-albums'], function ( func ) {

		var selectedAlbumIds = ${selectedIdsJson};
		var translationDTO = ${translationDTO};

		func( { userId: ${userId}
					, container: $( '.user-album-container' )
					, selectedAlbumIds: selectedAlbumIds
					, groupSelectionClass: '${groupSelectionClass}'
					, translationDTO: translationDTO
					, onEdit: ${onEditJSFunction}
					, onDelete: ${onDeleteJSFunction}
				  } );
	} );

</script>