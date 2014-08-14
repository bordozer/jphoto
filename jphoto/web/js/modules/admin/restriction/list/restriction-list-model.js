define( ["backbone"], function ( Backbone ) {

	var RestrictionSearchResultEntryModel = Backbone.Model.extend( {

	});

	var RestrictionListModel = Backbone.Collection.extend( {

		model: RestrictionSearchResultEntryModel,

		initialize: function ( options ) {

			this.url = Backbone.JPhoto.url( "/rest/admin/restrictions/search/" );

			this.restrictionTypesUser = options.restrictionTypesUser;
			this.restrictionTypesPhoto = options.restrictionTypesPhoto;
			this.restrictionStatuses = options.restrictionStatuses;
			this.translations = options.translations;
			this.historyEntryTranslations = options.historyEntryTranslations;
		}

	 } );

	return { RestrictionListModel :RestrictionListModel, RestrictionSearchResultEntryModel: RestrictionSearchResultEntryModel };
} );
