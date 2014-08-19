define( ["backbone"], function ( Backbone ) {

	var RestrictionListModel = Backbone.Model.extend( {

		initialize: function ( options ) {

			this.url = Backbone.JPhoto.url( "/rest/admin/restrictions/search/" );

			this.set( {
				selectedUserRestrictionTypeIds : [ 1, 2, 3, 4, 5, 6 ]
				, selectedPhotoRestrictionTypeIds : [ 7, 8, 9, 10, 11 ]
				, selectedRestrictionStatusIds : [ 1 ]
				, restrictionTypesUser : options.restrictionTypesUser
				, restrictionTypesPhoto : options.restrictionTypesPhoto
				, restrictionStatuses : options.restrictionStatuses
				, translations : options.translations
			}, { silent: true } );
		}

	 } );

	return { RestrictionListModel: RestrictionListModel };
} );
