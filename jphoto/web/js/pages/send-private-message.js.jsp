<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

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

	return {

		sendPrivateMessage: function ( fromUserId, toUserId, toUserName, callback ) {

			$( '#privateMessageTextId' ).val( '' );

			$( "#sendPrivateMessageToUserDivId" )
					.dialog( 'option', 'title', "${eco:translate('Send private message to')}" + ' ' + toUserName )
					.dialog( 'option', 'buttons', {
								Cancel:function () {
									$( this ).dialog( "close" );
								},
								"${eco:translate('Send message')}": function() {
									var privateMessageDTO = new PrivateMessageSendingDTO( fromUserId, toUserId, $( '#privateMessageTextId' ).val() );

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
	};
});