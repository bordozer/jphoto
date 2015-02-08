define( [ 'jquery'
			, 'modules/portal/photos/latest/latest-photos-model'
			, 'modules/portal/photos/latest/latest-photos-view'
		], function ( $, Model, View ) {

	function init( container ) {

		var model = new Model.PortalPageLatestPhotosModel();
		model.fetch( { cache: false } );

		var view = new View.PortalPageLatestPhotosView( { model: model, el: container } );
	}

	return init;
} );