define( ["modules/photo/list-big/photo-list-big-model"
		, "modules/photo/list-big/photo-list-big-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, photoListId, container ) {

		var photoListEntryModel = new Model.PhotoListEntryModel( { photoId: photoId });
		photoListEntryModel.set( { photoListId: photoListId } );
		photoListEntryModel.fetch( { cache: false } );

		var photoListEntryView = new View.PhotoListEntryModelView( { model: photoListEntryModel, el: container } );
	}

	return init;

} );
