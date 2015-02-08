define( [ 'jquery'
			, 'modules/portal/page/portal-page-model'
			, 'modules/portal/page/portal-page-view'
		], function ( $, Model, View ) {

	function init( container ) {

		var model = new Model.PortalPageModel();
		model.fetch( { cache: false } );

		var view = new View.PortalPageView( { model: model, el: container } );
	}

	return init;
} );