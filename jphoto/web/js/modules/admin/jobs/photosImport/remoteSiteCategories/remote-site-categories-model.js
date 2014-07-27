define( ["backbone"], function ( Backbone ) {

	var RemoteSiteCategoryModel = Backbone.Model.extend( {

		idAttribute: 'remotePhotoSiteCategoryId'
	});

	var RemoteSiteCategoriesModel = Backbone.Collection.extend( {

		model: RemoteSiteCategoryModel,

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/admin/jobs/photos-import/" + options.importSourceId + "/categories/";
		}

	 } );

	return { RemoteSiteCategoriesModel:RemoteSiteCategoriesModel };
} );