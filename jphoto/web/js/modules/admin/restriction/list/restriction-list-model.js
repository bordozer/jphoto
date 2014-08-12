define( ["backbone"], function ( Backbone ) {

	var RestrictionListEntryModel = Backbone.Model.extend( {

	});

	var RestrictionListModel = Backbone.Collection.extend( {

		model: RestrictionListEntryModel,

		initialize:function ( options ) {
			this.url = options.baseUrl + "/rest/admin/restrictions/photos/138137/history/";

			this.restrictionTypes = options.restrictionTypes;
			this.translations = options.translations;
		}

	 } );

	return { RestrictionListModel:RestrictionListModel };
} );
