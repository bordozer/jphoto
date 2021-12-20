define( ["modules/admin/restriction/restriction-history/restriction-history-model"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( filter, container ) {

		var restrictionHistoryModel = new Model.RestrictionHistoryModel( { filter:filter } );

		var restrictionHistoryView = new View.RestrictionHistoryView( { model: restrictionHistoryModel, el: container } );

		restrictionHistoryModel.fetch( { cache: false } );
	}

	return init;

} );
