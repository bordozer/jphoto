define( [ 'jquery'
			, 'modules/portal/photos/best/best-photos-model'
			, 'modules/portal/photos/best/best-photos-view'
		], function ( $, Model, View ) {

	function init( container ) {
		var model = new Model.PortalPageBestPhotosModel();
		var view = new View.PortalPageBestPhotosView( { model: model, el: container } );
	}

	return init;
} );