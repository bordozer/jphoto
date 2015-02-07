define( [ 'jquery'
			, 'modules/portal/portal-page-model'
			, 'modules/portal/portal-page-view'
		], function ( $, Model, View ) {

	function init( container ) {

		var model = new Model.PhotoListEntryModel();
		model.fetch( { cache: false } );

		var view = new View.PhotoListEntryModelView( { model: model, el: container } );
	}

	return init;
} );