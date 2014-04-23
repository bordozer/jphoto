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

			showInformationMessage: function () {
				var divId = 'infoMessageDiv';
				showMessage( divId, message, parameters, messageTimeout );
			}

			, showInformationMessageNoAutoClose: function () {
				var divId = 'infoMessageDiv';
				showMessage( divId, message, parameters );
			}

			, showSuccessMessage: function () {
				var divId = 'successMessageDiv';
				showMessage( divId, message, parameters, messageTimeout );
			}

			, showAlertMessage: function () {
				var divId = 'alertMessageDiv';
				showMessage( divId, message, parameters );
			}

			, showWarningMessage: function () {
				var divId = 'warningMessageDiv';
				showMessage( divId, message, parameters );
			}

			, showErrorMessage: function () {
				var divId = 'errorMessageDiv';
				showMessage( divId, message, parameters );
			}

			, showMessage: function ( divId, message, params, autoCloseAfter ) {
				$( "#" + divId + "_message" ).html( message );
				showDiv( divId, params, autoCloseAfter );
			}

			, showMessage_Notify: function ( message ) {
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
