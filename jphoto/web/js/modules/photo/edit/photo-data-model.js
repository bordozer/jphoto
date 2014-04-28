define( ["backbone"], function ( Backbone ) {

	var PhotoEditModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/photos/" + options.photoId + "/edit/";
		}
	});

	return { PhotoEditModel:PhotoEditModel };
} );