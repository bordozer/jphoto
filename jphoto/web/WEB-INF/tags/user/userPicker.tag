<%@ tag import="org.jabsorb.JSONRPCBridge" %>
<%@ tag import="core.services.user.UserTeamService" %>
<%@ tag import="core.context.ApplicationContextHelper" %>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="userIdControl" type="java.lang.String" required="true" %>
<%@ attribute name="user" type="core.general.user.User" required="false" %>
<%@ attribute name="callbackJSFunction" type="java.lang.String" required="false" %>

<%
	final UserTeamService userTeamService = ApplicationContextHelper.<UserTeamService>getBean( UserTeamService.BEAN_NAME );
	JSONRPCBridge.getGlobalBridge().registerObject( "userTeamService", userTeamService );
%>

<style type="text/css">
	.ui-autocomplete {
		max-height: 300px;
		overflow-y: auto;
		overflow-x: hidden;
	}
	* html .ui-autocomplete {
		height: 100px;
	}
</style>

<c:set var="searchControl" value="${userIdControl}_search"/>
<c:set var="foundMemberText" value="${eco:translate('Found member:')}"/>

<c:set var="imageNotFoundImg" value="${eco:imageFolderURL()}/imagenotfound.png"/>

<c:set var="resetFoundMemberImg" value="${eco:imageFolderURL()}/icons16/resetFoundMember.png"/>
<c:set var="resetFoundMemberHint" value="${eco:translate('Reset found member in user picker')}"/>

<c:set var="avatarHeight" value="150"/>

<input type="hidden" id="${userIdControl}" name="${userIdControl}" value="${not empty user ? user.id : 0}" />

<div id="foundMemberDiv">

	<div id="foundMemberAvatarDiv" style="float: left; height: ${avatarHeight}px; width: 27%; text-align: center;">
		<img id="memberSearchAvatar" src="${not empty user ? eco:userAvatarUrl(user.id) : imageNotFoundImg}" width="95%" />
	</div>

	<div style="float: right; width: 65%; height: ${avatarHeight}px; margin-left: 10px;">

		<div id="foundMemberLinkDiv" style="float :right; width: 100%; height: 30px;">
			<div id="foundMemberResetDiv" style="float: left; width: 7%; height: 100%; text-align: center;">
				<c:if test="${not empty user}">
					<img src="${resetFoundMemberImg}" alt="${resetFoundMemberHint}" title="${resetFoundMemberHint}" width="16" height="16" onclick="resetFoundMember();" />
				</c:if>
			</div>

			<div id="foundMemberCardLinkDiv" style="float: left; width: 85%; height: 100%;">
				<c:if test="${not empty user}">
					${foundMemberText} <user:userCard user="${user}"/>
				</c:if>
			</div>

		</div>

		<div id="memberSearchDiv" style="float: left; width: 100%;">
			<input type="text" id="${searchControl}" name="${searchControl}" value="${not empty user ? user.name : ''}" size="35">
		</div>

	</div>

</div>

<script type="text/javascript">

	$( document ).ready( function () {

		$( "#${searchControl}" ).autocomplete( {
													 width:300,
													 max:10,
													 delay:100,
													 minLength:0,
													 autoFocus:true,
													 cacheLength:1,
													 scroll:true,
													 highlight:false,
													 source:function ( request, response ) {
														 var searchString = $( '#${searchControl}' ).val();
														 var userPickerDTOs = jsonRPC.userTeamService.userLinkAjax( searchString ).list;

														 response( $.map( userPickerDTOs, function ( item ) {
															 var img = "<img src='" + item.userAvatarUrl + "' />";
															 return {
																 label:item.userName + ", " + item.userGender,
																 value:item.userName,
																 userId:item.userId,
																 userCardLink:item.userCardLink,
																 userAvatarUrl:item.userAvatarUrl,
																 userNameEscaped:item.userNameEscaped,
																 userName:item.userName
															 }
														 } ) )
													 },
													 select:function ( event, ui ) {
														 $( '#${userIdControl}' ).val( ui.item.userId );
														 $( '#foundMemberCardLinkDiv' ).html( '${foundMemberText} ' + ui.item.userCardLink );
														 $( '#foundMemberResetDiv' ).html( "<img src=\"${resetFoundMemberImg}\" alt=\"${resetFoundMemberHint}\"  title=\"${resetFoundMemberHint}\" width=\"16\" height=\"16\" onclick=\"resetFoundMember();\" />" );
														 $( '#memberSearchAvatar' ).attr( 'src', ui.item.userAvatarUrl );
														 <c:if test="${not empty callbackJSFunction}">
														 	${callbackJSFunction}( ui.item );
														 </c:if>
													 }
												 } ).data( "autocomplete" )._renderItem = function( ul, item ) {
														return $( "<li></li>" )
																.data( "item.autocomplete", item )
																.append( "<a>" + "<img src='" + item.userAvatarUrl + "' width='30' /> " + item.userNameEscaped + "</a>" )
																.appendTo( ul );
												};

		$( "#${searchControl}" ).bind( 'focus', function () { $( this ).autocomplete( "search" ); } );
	} );

	function resetFoundMember() {
		$( '#${userIdControl}' ).val( 0 );
		$( '#${searchControl}' ).val( '' );
		$( '#foundMemberResetDiv' ).text( '' );
		$( '#foundMemberCardLinkDiv' ).text( '' );
		$( '#memberSearchAvatar' ).attr( 'src', '${imageNotFoundImg}' );
		$( '#${searchControl}' ).focus();
	}

</script>