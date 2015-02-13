define( ["backbone"], function ( Backbone ) {

	var PhotoListEntryModel = Backbone.Model.extend( {

		idAttribute: 'photoId',

		initialize:function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/photos/" + options.photoId + "/" );

			this.displayOptions = options.displayOptions;
		}

		, refresh: function() {
			this.fetch( { data: this.displayOptions, reset: true } );
		}

	});

	return { PhotoListEntryModel:PhotoListEntryModel };
} );
