define( ["backbone"], function ( Backbone ) {

	var RestrictionListModel = Backbone.Model.extend( {

		initialize: function ( options ) {

			this.url = options.baseUrl + "/rest/admin/restrictions/search/";

			this.restrictionTypes = options.restrictionTypes;
			this.translations = options.translations;
			this.historyEntryTranslations = options.historyEntryTranslations;
		}

	 } );

	return { RestrictionListModel:RestrictionListModel };
} );
