define( ["backbone"], function ( Backbone ) {

	var EntryIconModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = options.baseUrl + '/json/bookmarks/' + options.userId + '/' + options.bookmarkEntryId + '/' + options.bookmarkEntryTypeId + '/?isAdding=' + options.isAdding;
		}

		, refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	return { EntryIconModel:EntryIconModel };
} );
