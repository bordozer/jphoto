define( [ 'jquery'
			, 'modules/portal/page/portal-page-model'
			, 'modules/portal/page/portal-page-view'
		], function ( $, Model, View ) {

	function init( dateOptions, container ) {
		var model = new Model.PortalPageModel( { dateOptions: dateOptions } );
		var view = new View.PortalPageView( { model: model, el: container } );
	}

	return init;
} );