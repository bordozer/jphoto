define( ["modules/photo/list/photo-list-model"
		, "modules/photo/list/photo-list-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, isGroupOperationEnabled, baseUrl, container ) {

		var photoListEntryModel = new Model.PhotoListEntryModel( { photoId: photoId, baseUrl: baseUrl });
		photoListEntryModel.set( { isGroupOperationEnabled: isGroupOperationEnabled, baseUrl: baseUrl } );
		photoListEntryModel.fetch( { cache: false } );

		var photoListEntryView = new View.PhotoListEntryModelView( { model: photoListEntryModel, el: container } );
	}

	return init;

} );