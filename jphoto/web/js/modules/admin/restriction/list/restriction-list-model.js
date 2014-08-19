define( ["backbone"], function ( Backbone ) {

	var RestrictionListModel = Backbone.Model.extend( {

		initialize: function ( options ) {

			this.url = Backbone.JPhoto.url( "/rest/admin/restrictions/search/" );

			this.restrictionTypesUser = options.restrictionTypesUser;
			this.restrictionTypesPhoto = options.restrictionTypesPhoto;
			this.restrictionStatuses = options.restrictionStatuses;
			this.translations = options.translations;
		}

	 } );

	return { RestrictionListModel: RestrictionListModel };
} );
