define( ["components/user-picker/user-picker-model"
		, "components/user-picker/user-picker-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( callback, baseUrl, container ) {

		var userPickerModel = new Model.UserPickerModel( { callback: callback, baseUrl: baseUrl } );
//		userPickerModel.fetch( { cache: false } );

		var userPickerView = new View.UserPickerView( { model: userPickerModel, el: container } );
//		userPickerView.render();
	}

	return init;

} );