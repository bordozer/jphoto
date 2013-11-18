
var parameters = { closeClick:true, closeEsc:true, centered:true, showOverlay:true, onLoad:function () {} };
var messageTimeout = 2000;

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

	$( "#" + divId ).lightbox_me( params );

	if ( autoCloseAfter > 0 ) {
		setTimeout( function () {
			fadeoutAndCloseMessageBox( divId );
		}, autoCloseAfter );
	}
}

function notifySuccessMessage( message ) {
	noty( {
			  text:message, type:'success', layout:'bottomRight', timeout:messageTimeout
		  } );
}
