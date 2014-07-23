define( ["components/user-picker/user-picker-model"
		, "components/user-picker/user-picker-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( baseUrl, container ) {

		var userPickerModel = new Model.UserPickerModel( { baseUrl: baseUrl } );

		var userPickerView = new View.UserPickerView( { model: userPickerModel, el: container } );
		userPickerView.renderPicker();
	}

	return init;

} );