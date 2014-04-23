define( [ 'jquery', 'noty', 'toastmessage' ], function ( $ ) {

	return function () {

		return {

			showUIMessage_InformationMessage_ManualClosing: function ( messageText ) {
				$().toastmessage( 'showNoticeToast', messageText ); // TODO: implement manual closing
			},

			showUIMessage_Information: function ( messageText ) {
				$().toastmessage( 'showNoticeToast', messageText );
			},

			showUIMessage_Warning: function ( messageText ) {
				$().toastmessage( 'showWarningToast', messageText );
			},

			showUIMessage_Error: function ( messageText ) {
				$().toastmessage( 'showErrorToast', messageText );
			},

			showUIMessage_FromCustomDiv: function ( element ) {
				$().toastmessage( 'showNoticeToast', element.html() );
			},

			showUIMessage_Notification: function ( messageText ) {
				var n = noty( {
					text: messageText
					, type: 'success'
					, dismissQueue: true
					, layout: 'bottomRight'
					, theme: 'defaultTheme'
					, timeout: 5000
				} );
			}
		}
	}();
} );
