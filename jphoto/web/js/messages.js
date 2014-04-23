define( [ 'jquery', 'noty' ], function ( $ ) {

	return function () {

		var parameters = { closeClick: true, closeEsc: true, centered: true, showOverlay: true, onLoad: function () {} };
		var messageTimeout = 5000;

		function showMessage ( divId, message, params, autoCloseAfter ) {
			$( "#" + divId + "_message" ).html( message );
			showDiv( divId, params, autoCloseAfter );
		}

		function showDiv( divId, params, autoCloseAfter ) {

			console.log( 'Shown message: ', message );

			/*$( "#" + divId ).attr( "data-lightbox", "divId" ); //.lightbox( params );

			 if ( autoCloseAfter > 0 ) {
			 setTimeout( function () {
			 fadeoutAndCloseMessageBox( divId );
			 }, autoCloseAfter );
			 }*/
		}

		return {

			showUIMessage_InformationMessage_ManualClosing: function () { //showInformationMessageNoAutoClose
				var divId = 'infoMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Information: function () { //showInformationMessage
				var divId = 'infoMessageDiv';
				showMessage( divId, message, parameters, messageTimeout );
			},

			showUIMessage_Success: function () { //showSuccessMessage
				var divId = 'successMessageDiv';
				showMessage( divId, message, parameters, messageTimeout );
			},

			showUIMessage_Alert: function () { //showAlertMessage
				var divId = 'alertMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Warning: function () { //showWarningMessage
				var divId = 'warningMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Error: function () { //showErrorMessage
				var divId = 'errorMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Notification: function ( message ) {
				console.log( 'notifySuccessMessage' );
				var n = noty( {
					text: message
					, type: 'success'
					, dismissQueue: true
					, layout: 'bottomRight'
					, theme: 'defaultTheme'
					, timeout: messageTimeout
				} );
			}
		}
	}();
} );
