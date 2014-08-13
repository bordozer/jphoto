define( ["backbone"], function ( Backbone ) {

	var RestrictionSearchResultEntryModel = Backbone.Model.extend( {

	});

	var RestrictionListModel = Backbone.Collection.extend( {

		model: RestrictionSearchResultEntryModel,

		initialize: function ( options ) {

			this.url = options.baseUrl + "/rest/admin/restrictions/search/";

			this.restrictionTypes = options.restrictionTypes;
			this.restrictionStatuses = options.restrictionStatuses;
			this.translations = options.translations;
			this.historyEntryTranslations = options.historyEntryTranslations;
		}

	 } );

	return { RestrictionListModel :RestrictionListModel, RestrictionSearchResultEntryModel: RestrictionSearchResultEntryModel };
} );
