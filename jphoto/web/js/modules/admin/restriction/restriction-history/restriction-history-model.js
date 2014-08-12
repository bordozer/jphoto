define( ["backbone"], function ( Backbone ) {

	var RestrictionHistoryEntryModel = Backbone.Model.extend( {

	});

	var RestrictionHistoryModel = Backbone.Collection.extend( {

		model: RestrictionHistoryEntryModel,

		initialize:function ( options ) {
			this.restrictionEntryTypeId = options.restrictionEntryTypeId;
			this.url = options.baseUrl + "/rest/admin/restrictions/" + ( options.restrictionEntryTypeId == 1 ? "members" : "photos" ) + "/" + options.entryId + "/history/";
			this.translations = options.translations;
		}

	 } );

	return { RestrictionHistoryEntryModel:RestrictionHistoryEntryModel, RestrictionHistoryModel:RestrictionHistoryModel};
} );
