define( ["backbone"], function ( Backbone ) {

	var RemoteSiteCategoryModel = Backbone.Model.extend( {

		idAttribute: 'remotePhotoSiteCategoryId'
	});

	var RemoteSiteCategoriesModel = Backbone.Collection.extend( {

		model: RemoteSiteCategoryModel,

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/admin/jobs/photos-import/" + options.importSourceId + "/categories/" );
		}

	 } );

	return { RemoteSiteCategoriesModel:RemoteSiteCategoriesModel };
} );
