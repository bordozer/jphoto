define( [ 'jquery'
			, 'modules/portal/genres/pp-genres-model'
			, 'modules/portal/genres/pp-genres-view'
		], function ( $, Model, View ) {

	function init( container ) {
		var model = new Model.PortalPageGenresModel();
		var view = new View.PortalPageGenresView( { model: model, el: container } );
	}

	return init;
} );