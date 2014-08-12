define( ["backbone"], function ( Backbone ) {

	var RestrictionListEntryModel = Backbone.Model.extend( {

	});

	var RestrictionListModel = Backbone.Collection.extend( {

		model: RestrictionListEntryModel,

		defaults: function() {
			return {
				selectedTypeIds: []
			}
		},

		initialize: function ( options ) {

			this.url = options.baseUrl + "/rest/admin/restrictions/search/";

			this.restrictionTypes = options.restrictionTypes;
			this.translations = options.translations;
			this.historyEntryTranslations = options.historyEntryTranslations;
		}

	 } );

	return { RestrictionListModel:RestrictionListModel };
} );
