define( ["backbone"], function ( Backbone ) {

	var RestrictionHistoryEntryModel = Backbone.Model.extend( {

	});

	var RestrictionHistoryModel = Backbone.Collection.extend( {

		model: RestrictionHistoryEntryModel,

		initialize:function ( options ) {
			this.url = options.baseUrl + "/rest/admin/restriction/members/" + options.userId + "/history/";
			this.translations = options.translations;
		}

	 } );

	return { RestrictionHistoryEntryModel:RestrictionHistoryEntryModel, RestrictionHistoryModel:RestrictionHistoryModel};
} );
