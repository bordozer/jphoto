define( ["backbone"], function ( Backbone ) {

	var PhotoListEntryModel = Backbone.Model.extend( {

		idAttribute: 'photoId',

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/photos/" + options.photoId + "/";
		}
	});

	return { PhotoListEntryModel:PhotoListEntryModel };
} );
