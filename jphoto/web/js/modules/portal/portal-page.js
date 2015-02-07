define( [ 'jquery'
			, 'modules/portal/portal-page-model'
			, 'modules/portal/portal-page-view'
		], function ( $, Model, View ) {

	function init( container ) {

		var model = new Model.PortalPageModel();
		model.fetch( { cache: false } );

		var view = new View.PortalPageView( { model: model, el: container } );
		view.render();
	}

	return init;
} );