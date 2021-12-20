define( ["backbone"], function ( Backbone ) {

	var PhotoCategoryHandlerModel = Backbone.Model.extend( {

		idAttribute: 'photoId',

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/users/" + options.authorId + "/photos/" + options.photoId + "/edit/category/" + options.categoryId + "/" );
		}

		, refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PhotoCategoryHandlerModel:PhotoCategoryHandlerModel };
} );
