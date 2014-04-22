
var parameters = { closeClick:true, closeEsc:true, centered:true, showOverlay:true, onLoad:function () {} };
var messageTimeout = 5000;

function showInformationMessage( message ) {
	var divId = 'infoMessageDiv';
	showMessage( divId, message, parameters, messageTimeout );
}

function showInformationMessageNoAutoClose( message ) {
	var divId = 'infoMessageDiv';
	showMessage( divId, message, parameters );
}

function showSuccessMessage( message ) {
	var divId = 'successMessageDiv';
	showMessage( divId, message, parameters, messageTimeout );
}

function showAlertMessage( message ) {
	var divId = 'alertMessageDiv';
	showMessage( divId, message, parameters );
}

function showWarningMessage( message ) {
	var divId = 'warningMessageDiv';
	showMessage( divId, message, parameters );
}

//function showValidationDataErrorMessage( message ) {
//	var divId = 'validationDataErrorMessageDiv';
//	showMessage( divId, message, parameters );
//}

function showErrorMessage( message ) {
	var divId = 'errorMessageDiv';
	showMessage( divId, message, parameters );
}

function showMessage( divId, message, params, autoCloseAfter ) {
	$( "#" + divId + "_message" ).html( message );
	showDiv( divId, params, autoCloseAfter );
}

function showDiv( divId, params, autoCloseAfter ) {

	/*$( "#" + divId ).attr( "data-lightbox", "divId" ); //.lightbox( params );

	if ( autoCloseAfter > 0 ) {
		setTimeout( function () {
			fadeoutAndCloseMessageBox( divId );
		}, autoCloseAfter );
	}*/
}

function notifySuccessMessage( message ) {
	var n = noty( {
		text: message
		, type: 'success'
		, dismissQueue: true
		, layout: 'bottomRight'
		, theme: 'defaultTheme'
		, timeout: messageTimeout
	} );
}
