define( ["backbone"], function ( Backbone ) {

	var PhotoCategoryHandlerModel = Backbone.Model.extend( {

		idAttribute: 'photoId',

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/photos/" + options.photoId + "/edit/category/" + options.categoryId + "/";
		}

		, refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PhotoCategoryHandlerModel:PhotoCategoryHandlerModel };
} );