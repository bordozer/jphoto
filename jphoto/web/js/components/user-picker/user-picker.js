define( ["components/user-picker/user-picker-model"
		, "components/user-picker/user-picker-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( initialValue, callback, baseUrl, container ) {

		var userPickerModel = new Model.UserPickerModel( { searchString: initialValue, callback: callback, baseUrl: baseUrl } );

		var userPickerView = new View.UserPickerView( { model: userPickerModel, el: container } );
	}

	return init;

} );