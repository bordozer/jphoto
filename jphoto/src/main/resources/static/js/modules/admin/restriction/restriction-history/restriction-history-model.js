define( ["backbone"], function ( Backbone ) {

	var RestrictionHistoryEntryModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/admin/restrictions/history/" + this.get( 'id' ) + "/" );
		}
	});

	var RestrictionHistoryModel = Backbone.Collection.extend( {

		model: RestrictionHistoryEntryModel,

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "rest/admin/restrictions/history/" + ( options.filter.restrictionEntryTypeId == 1 ? "members" : "photos" ) + "/" + options.filter.entryId + "/" );
		}

	 } );

	return { RestrictionHistoryEntryModel: RestrictionHistoryEntryModel, RestrictionHistoryModel: RestrictionHistoryModel} ;
} );
