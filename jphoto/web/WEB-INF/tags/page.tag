<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="org.jabsorb.JSONRPCBridge" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="ui.services.ajax.AjaxService" %>
<%@ tag import="core.general.menus.EntryMenuType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="pageModel" required="true" type="ui.elements.PageModel" %>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "ajaxService", ApplicationContextHelper.<AjaxService>getBean( AjaxService.BEAN_NAME ) );
%>
<c:set var="baseUrl" value="${eco:baseUrl()}" />
<c:set var="isSuperAdmin" value="<%=ApplicationContextHelper.getSecurityService().isSuperAdminUser( EnvironmentContext.getCurrentUser().getId() )%>"/>
<c:set var="menuType_PhotoId" value="<%=EntryMenuType.PHOTO.getId()%>" />

<eco:page pageModel="${pageModel}">

	<script type="text/javascript">

		var jsonRPC;
		require( ['jsonrpc'], function() {
			jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );
		} );

		require( ['jquery'], function( $ ) {

			function fadeoutAndCloseMessageBox( divId ) {
				var fadeOutTime = 400;
				$( "#" + divId ).fadeOut( fadeOutTime, "linear" );

				setTimeout( function () {
					closeMessageBox( divId )
				}, fadeOutTime );
			}

			function closeMessageBox( divId ) {
				$( '#' + divId ).trigger( 'close' );
			}
		});

		function showUIMessage_Notification( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, ui_messages ) {
				ui_messages.showUIMessage_Notification( messageText );
			} );
		}

		function showUIMessage_InformationMessage_ManualClosing( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, ui_messages ) {
				ui_messages.showUIMessage_InformationMessage_ManualClosing( messageText );
			} );
		}

		function showUIMessage_Information( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, ui_messages ) {
				ui_messages.showUIMessage_Information( messageText );
			} );
		}

		function showUIMessage_Warning( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, ui_messages ) {
				ui_messages.showUIMessage_Warning( messageText );
			} );
		}

		function showUIMessage_Error( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, ui_messages ) {
				ui_messages.showUIMessage_Error( messageText );
			} );
		}
	</script>

	<tags:messageDivs />

	<div id="sendPrivateMessageToUserDivId" title="..." style="display: none;">
		<html:textarea inputId="privateMessageTextId" title="${eco:translate('Message')}" hint="${eco:translate('Private message text')}" rows="7" cols="50" />
	</div>

	<script type="text/javascript">

		require( [ 'jquery', 'jquery_ui' ], function( $ ) {
			$( function () {
				$( "#sendPrivateMessageToUserDivId" ).dialog( {
					height:300
					, width:600
					, modal:true
					, autoOpen:false
				 } );
			} );
		} );

		function sendPrivateMessage( fromUserId, toUserId, toUserName, callback ) {
			require( [ 'jquery', '/js/pages/send-private-message.js.jsp' ], function ( $, sendMessageFunction ) {
				sendMessageFunction.sendPrivateMessage( fromUserId, toUserId, toUserName, callback );
			} );
		}

	</script>


	<c:if test="${isSuperAdmin}">

		<div id="lockUserDivId" title="..." style="display: none;">
			<iframe id="lockUserIFrame" src="" width="98%" height="98%" style="border: none;"></iframe>
		</div>

		<script type="text/javascript">

			require( ['jquery', 'jquery_ui'], function( $ ) {
				$( function () {
					$( "#lockUserDivId" ).dialog( {
						height:500
						, width:800
						, modal:true
						, autoOpen:false
					 } );
				} );
			});

			function adminLockUser( userId, userName ) {
				require( [ 'jquery', '/js/pages/admin-functions.js.jsp' ], function ( $, adminFunctions ) {
					adminFunctions.adminLockUser( userId, userName );
				} );
			}

			function adminPhotoNudeContentSet( photoId ) {
				require( [ 'jquery', '/js/pages/admin-functions.js.jsp' ], function ( $, adminFunctions ) {
					adminFunctions.adminPhotoNudeContentSet( photoId, jsonRPC );
				} );
			}

			function adminPhotoNudeContentRemove( photoId ) {
				require( [ 'jquery', '/js/pages/admin-functions.js.jsp' ], function ( $, adminFunctions ) {
					adminFunctions.adminPhotoNudeContentRemove( photoId, jsonRPC );
				} );
			}
		</script>

		<script type="text/javascript" src="${eco:baseUrl()}/js/translationsReload.jsp"></script>

	</c:if>

	<jsp:doBody/>

	<tags:devMode>
		<input type="hidden" id="the_end_of_the_page_content">
	</tags:devMode>

</eco:page>