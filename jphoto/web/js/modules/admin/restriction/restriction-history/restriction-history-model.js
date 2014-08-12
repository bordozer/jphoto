define( ["backbone"], function ( Backbone ) {

	var RestrictionHistoryEntryModel = Backbone.Model.extend( {

	});

	var RestrictionHistoryModel = Backbone.Collection.extend( {

		model: RestrictionHistoryEntryModel,

		initialize:function ( options ) {
			this.url = options.baseUrl + "/rest/admin/restrictions/" + ( options.filter.restrictionEntryTypeId == 1 ? "members" : "photos" ) + "/" + options.filter.entryId + "/history/";
			this.translations = options.translations;
		}

	 } );

	return { RestrictionHistoryEntryModel:RestrictionHistoryEntryModel, RestrictionHistoryModel:RestrictionHistoryModel};
} );
