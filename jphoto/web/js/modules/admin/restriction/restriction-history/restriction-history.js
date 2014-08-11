define( ["modules/admin/restriction/restriction-history/restriction-history-model"
		, "modules/admin/restriction/restriction-history/restriction-history-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( entryId, restrictionEntryTypeId, translations, baseUrl, container ) {

		var restrictionHistoryModel = new Model.RestrictionHistoryModel( { entryId:entryId, restrictionEntryTypeId: restrictionEntryTypeId, translations: translations, baseUrl:baseUrl } );

		var restrictionHistoryView = new View.RestrictionHistoryView( { model: restrictionHistoryModel, el: container } );
	}

	return init;

} );