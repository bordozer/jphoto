define( ["modules/photo/list/list/photo-list-model"
		, "modules/photo/list/list/photo-list-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, photoListId, isGroupOperationEnabled, container ) {

		var photoListModel = new Model.PhotoListModel( { photoId: photoId });
		photoListModel.set( { isGroupOperationEnabled: isGroupOperationEnabled, photoListId: photoListId } );
		photoListModel.fetch( { cache: false } );

		var photoListEntryView = new View.PhotoListEntryModelView( { model: photoListModel, el: container } );
	}

	return init;

} );
