define( ["components/user-picker/user-picker-model"
		, "components/user-picker/user-picker-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( initialValue, callbackFunction, baseUrl, container ) {

		var userPickerModel = new Model.UserPickerModel( { searchString: initialValue, baseUrl: baseUrl } );

		var userPickerView = new View.UserPickerView( { model: userPickerModel, el: container, callbackFunction: callbackFunction } );
	}

	return init;

} );