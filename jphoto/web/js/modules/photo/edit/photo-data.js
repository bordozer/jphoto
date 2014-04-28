define( ["modules/photo/edit/photo-data-model"
		, "modules/photo/edit/photo-data-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, baseUrl, container ) {

		var photoEditModel = new Model.PhotoEditModel( { photoId: photoId, baseUrl: baseUrl } );
//		photoListEntryModel.set( { isGroupOperationEnabled: isGroupOperationEnabled, baseUrl: baseUrl, photoListId: photoListId } );
//		photoListEntryModel.fetch( { cache: false } );

		var photoEditView = new View.PhotoEditView( { model: photoEditModel, el: container } );
	}

	return init;

} );