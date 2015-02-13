define( ["modules/photo/list/photo-list-entry-model"
		, "modules/photo/list/photo-list-entry-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, photoListId, isGroupOperationEnabled, container ) {

		var photoListEntryModel = new Model.PhotoListEntryModel( { photoId: photoId });
		photoListEntryModel.set( { isGroupOperationEnabled: isGroupOperationEnabled, photoListId: photoListId } );
		photoListEntryModel.fetch( { cache: false } );

		var photoListEntryView = new View.PhotoListEntryModelView( { model: photoListEntryModel, el: container } );
	}

	return init;

} );