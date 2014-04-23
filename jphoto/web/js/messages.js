define( [ 'jquery', 'noty' ], function ( $ ) {

	return function () {

		var parameters = { closeClick: true, closeEsc: true, centered: true, showOverlay: true, onLoad: function () {} };
		var messageTimeout = 5000;

		function showMessage ( divId, messageText, params, autoCloseAfter ) {
			$( "#" + divId + "_message" ).html( messageText );
			showDiv( divId, params, messageText, autoCloseAfter );
		}

		function showDiv( divId, params, messageText, autoCloseAfter ) {

//			console.log( 'Shown message: ', messageText );
			this.showUIMessage_Notification( "TODO: use lightbox<br />" + messageText );

			/*$( "#" + divId ).attr( "data-lightbox", "divId" ); //.lightbox( params );

			 if ( autoCloseAfter > 0 ) {
			 setTimeout( function () {
			 fadeoutAndCloseMessageBox( divId );
			 }, autoCloseAfter );
			 }*/
		}

		return {

			showUIMessage_InformationMessage_ManualClosing: function ( messageText ) {
				var divId = 'infoMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Information: function ( messageText ) {
				var divId = 'infoMessageDiv';
				showMessage( divId, message, parameters, messageTimeout );
			},

			showUIMessage_Success: function ( messageText ) { //showSuccessMessage
				var divId = 'successMessageDiv';
				showMessage( divId, message, parameters, messageTimeout );
			},

			showUIMessage_Alert: function ( messageText ) { //showAlertMessage
				var divId = 'alertMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Warning: function ( messageText ) { //showWarningMessage
				var divId = 'warningMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Error: function ( messageText ) { //showErrorMessage
				var divId = 'errorMessageDiv';
				showMessage( divId, message, parameters );
			},

			showUIMessage_Notification: function ( messageText ) {
				var n = noty( {
					text: messageText
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
