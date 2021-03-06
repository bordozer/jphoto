define( ["backbone"], function ( Backbone ) {

	var EntryIconModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = Backbone.JPhoto.url( 'rest/bookmarks/' + options.userId + '/' + options.bookmarkEntryId + '/' + options.bookmarkEntryTypeId + '/' );
		}

		, refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	return { EntryIconModel:EntryIconModel };
} );
