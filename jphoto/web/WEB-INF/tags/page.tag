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
		require( ['jsonrpc'], function( jsonrpc ) {
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
			require( [ 'jquery', 'ui_messages' ], function ( $, messages ) {
				messages.showUIMessage_Notification( messageText );
			} );
		}

		function showUIMessage_InformationMessage_ManualClosing( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, messages ) {
				messages.showUIMessage_InformationMessage_ManualClosing( messageText );
			} );
		}

		function showUIMessage_Information( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, messages ) {
				messages.showUIMessage_Information( messageText );
			} );
		}

		function showUIMessage_Warning( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, messages ) {
				messages.showUIMessage_Warning( messageText );
			} );
		}

		function showUIMessage_Error( messageText ) {
			require( [ 'jquery', 'ui_messages' ], function ( $, messages ) {
				messages.showUIMessage_Error( messageText );
			} );
		}
	</script>

	<tags:messageDivs />

	<c:set var="privateMessageTextId" value="privateMessageTextId" />
	<c:set var="sendPrivateMessageToUserDivId" value="sendPrivateMessageToUserDivId" />

	<div id="${sendPrivateMessageToUserDivId}" title="..." style="display: none;">
		<html:textarea inputId="${privateMessageTextId}" title="${eco:translate('Message')}" hint="${eco:translate('Private message text')}" rows="7" cols="50" />
	</div>

	<script type="text/javascript">

		require( [ 'jquery', 'jquery_ui' ], function( $ ) {

			$( function () {
				$( "#${sendPrivateMessageToUserDivId}" ).dialog( {
												height:300
												, width:600
												, modal:true
												, autoOpen:false
											 } );
			} );

			function sendPrivateMessage( fromUserId, toUserId, toUserName, callback ) {

				$( '#${privateMessageTextId}' ).val( '' );

				$( "#${sendPrivateMessageToUserDivId}" )
						.dialog( 'option', 'title', "${eco:translate('Send private message to')}" + ' ' + toUserName )
						.dialog( 'option', 'buttons', {
														Cancel:function () {
															$( this ).dialog( "close" );
														},
														"${eco:translate('Send message')}": function() {
															var privateMessageDTO = new PrivateMessageSendingDTO( fromUserId, toUserId, $( '#${privateMessageTextId}' ).val() );

															var ajaxResultDTO = jsonRPC.ajaxService.sendPrivateMessageAjax( privateMessageDTO );

															if ( ! ajaxResultDTO.successful ) {
																showUIMessage_Error( ajaxResultDTO.message );
																return false;
															}

															showUIMessage_Notification( "${eco:translate('The message has been sent to')}" + ' ' + toUserName );

															$( this ).dialog( "close" );

															if ( callback != undefined ) {
																callback();
															}
														}
													} )
						.dialog( "open" );
			}

			function PrivateMessageSendingDTO( fromUserId, toUserId, privateMessageText ) {
				this.fromUserId = fromUserId;
				this.toUserId = toUserId;
				this.privateMessageText = privateMessageText;

				this.getFromUserId = function () {
					return this.fromUserId;
				};

				this.getToUserId = function () {
					return this.toUserId;
				};

				this.getPrivateMessageText = function () {
					return this.privateMessageText;
				};
			}
		} );

	</script>


	<c:if test="${isSuperAdmin}">
		<c:set var="lockUserDivId" value="lockUserDivId" />
		<c:set var="lockUserIFrameId" value="lockUserIFrame" />

		<div id="${lockUserDivId}" title="..." style="display: none;">
			<iframe id="${lockUserIFrameId}" src="" width="98%" height="98%" style="border: none;"></iframe>
		</div>

		<script type="text/javascript">

			require( ['jquery', 'jquery_ui'], function( $, ui ) {

				$( function () {
					$( "#${lockUserDivId}" ).dialog( {
													height:500
													, width:800
													, modal:true
													, autoOpen:false
												 } );
				} );

				function adminLockUser( userId, userName ) {

					var url = "${eco:baseAdminUrl()}/members/" + userId + "/lock/";
					$( '#${lockUserIFrameId}' ).attr( 'src', url );

					$( "#${lockUserDivId}" )
							.dialog( 'option', 'title', "${eco:translate('Lock user')}" + ' ' + userName + ' ( #' + userId + ' )' )
							.dialog( 'option', 'buttons', {
															Cancel:function () {
																$( this ).dialog( "close" );
															}/*,
															"${eco:translate('Lock')}": function() {

																$( this ).dialog( "close" );
															}*/
														} )
							.dialog( "open" );
				}

				function adminPhotoNudeContentSet( photoId ) {
					adminSetPhotoNudeContent( photoId, true );
				}

				function adminPhotoNudeContentRemove( photoId ) {
					adminSetPhotoNudeContent( photoId, false );
				}

				function adminSetPhotoNudeContent( photoId, isNudeContent ) {
					jsonRPC.ajaxService.setPhotoNudeContent( photoId, isNudeContent );

					/*require( ['modules/photo/list/photo-list'], function ( photoListEntry ) {
						var photoListId = 1;
						var element = $( '.photo-container-' + 1 +'-' + photoId );
						photoListEntry( photoId, photoListId, true, '${eco:baseUrl()}', element );
					} );*/
				}
			});
		</script>

		<script type="text/javascript" src="${eco:baseUrl()}/js/translationsReload.jsp"></script>

	</c:if>

	<jsp:doBody/>

	<tags:devMode>
		<input type="hidden" id="the_end_of_the_page_content">
	</tags:devMode>

</eco:page>