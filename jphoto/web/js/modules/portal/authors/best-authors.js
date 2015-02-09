define( [ 'jquery'
			, 'modules/portal/authors/best-authors-model'
			, 'modules/portal/authors/best-authors-view'
		], function ( $, Model, View ) {

	function init( dateFrom, dateTo, container ) {
		var model = new Model.PortalPageBestAuthorsModel( { dateFrom: dateFrom, dateTo: dateTo } );
		var view = new View.PortalPageBestAuthorsModel( { model: model, el: container } );
	}

	return init;
} );