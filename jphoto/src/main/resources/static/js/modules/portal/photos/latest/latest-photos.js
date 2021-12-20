define( [ 'jquery'
			, 'modules/portal/photos/latest/latest-photos-model'
			, 'modules/portal/photos/latest/latest-photos-view'
		], function ( $, Model, View ) {

	function init( container ) {
		var model = new Model.PortalPageLatestPhotosModel();
		var view = new View.PortalPageLatestPhotosView( { model: model, el: container } );
	}

	return init;
} );