define( ["backbone"], function ( Backbone ) {

	var PhotoListEntryModel = Backbone.Model.extend( {

		idAttribute: 'photoId',

		initialize:function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/photos/" + options.photoId + "/" );
		}

		, refresh: function() {
			this.fetch( { reset: true } );
		}

	});

	return { PhotoListEntryModel:PhotoListEntryModel };
} );
