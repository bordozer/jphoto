define( ["components/user-picker/user-picker-model"
		, "components/user-picker/user-picker-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( controlName, initialUserId, callbackFunction, container ) {

		var userPickerModel = new Model.UserPickerModel( { controlName: controlName, initialUserId: initialUserId } );

		var userPickerView = new View.UserPickerView( { model: userPickerModel, el: container, callbackFunction: callbackFunction } );
	}

	return init;

} );