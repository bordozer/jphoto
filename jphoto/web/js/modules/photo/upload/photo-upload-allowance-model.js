define( ["backbone"], function ( Backbone ) {

	var PhotoUploadAllowanceModel = Backbone.Model.extend( {

		idAttribute: 'userId',

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/users/" + options.userId + "/photo-upload-allowance/" + options.genreId + "/";
		}

		, refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PhotoUploadAllowanceModel:PhotoUploadAllowanceModel };
} );