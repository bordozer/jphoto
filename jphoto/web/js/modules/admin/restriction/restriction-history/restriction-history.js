define( ["modules/admin/restriction/restriction-history/restriction-history-model"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( filter, historyEntryTranslations, baseUrl, container ) {

		var restrictionHistoryModel = new Model.RestrictionHistoryModel( { filter:filter, historyEntryTranslations: historyEntryTranslations, baseUrl:baseUrl } );

		var restrictionHistoryView = new View.RestrictionHistoryView( { model: restrictionHistoryModel, el: container } );
	}

	return init;

} );