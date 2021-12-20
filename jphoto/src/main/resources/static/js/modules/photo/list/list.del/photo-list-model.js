define( ["backbone"], function ( Backbone ) {

	var PhotoListModel = Backbone.Collection.extend( {

		idAttribute: 'photoListId',

		initialize:function ( options ) {

		}
	});

	return { PhotoListModel: PhotoListModel };
} );

