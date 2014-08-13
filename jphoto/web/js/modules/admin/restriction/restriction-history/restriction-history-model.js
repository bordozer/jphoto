define( ["backbone"], function ( Backbone ) {

	var RestrictionHistoryEntryModel = Backbone.Model.extend( {

	});

	var RestrictionHistoryModel = Backbone.Collection.extend( {

		model: RestrictionHistoryEntryModel,

		initialize: function ( options ) {

			this.url = Backbone.JPhoto.url( "/rest/admin/restrictions/" + ( options.filter.restrictionEntryTypeId == 1 ? "members" : "photos" ) + "/" + options.filter.entryId + "/history/" );

			this.historyEntryTranslations = options.historyEntryTranslations;
		}

	 } );

	return { RestrictionHistoryEntryModel:RestrictionHistoryEntryModel, RestrictionHistoryModel:RestrictionHistoryModel};
} );
