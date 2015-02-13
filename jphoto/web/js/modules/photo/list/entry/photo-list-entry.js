define( ["modules/photo/list/entry/photo-list-entry-model"
		, "modules/photo/list/entry/photo-list-entry-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, photoListId, displayOptions, container ) {

		var photoListEntryModel = new Model.PhotoListEntryModel( { photoId: photoId });
		photoListEntryModel.set( { photoListId: photoListId, displayOptions: displayOptions } );
		photoListEntryModel.fetch( { cache: false } );

		var photoListEntryView = new View.PhotoListEntryModelView( { model: photoListEntryModel, el: container } );
	}

	return init;

} );