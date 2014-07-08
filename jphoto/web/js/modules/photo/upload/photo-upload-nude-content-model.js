define( ["backbone"], function ( Backbone ) {

	var PhotoUploadNudeContentModel = Backbone.Model.extend( {

		idAttribute: 'userId',

		initialize: function ( options ) {
			this.url = options.baseUrl + "/json/genres/photo-upload-nude-content/genre/" + options.genreId + "/photo/" + options.photoId + "/";
		}

		, refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PhotoUploadNudeContentModel:PhotoUploadNudeContentModel };
} );