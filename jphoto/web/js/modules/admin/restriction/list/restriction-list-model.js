define( ["backbone"], function ( Backbone ) {

	var RestrictionListModel = Backbone.Model.extend( {

		initialize: function ( options ) {

			this.url = Backbone.JPhoto.url( "/rest/admin/restrictions/search/" );

			this.set( {
				selectedRestrictionStatusIds : [ 1 ]
				, restrictionTypesUser : options.restrictionTypesUser
				, restrictionTypesPhoto : options.restrictionTypesPhoto
				, restrictionStatuses : options.restrictionStatuses
				, translations : options.translations
			}, { silent: true } );
		}

	 } );

	return { RestrictionListModel: RestrictionListModel };
} );
