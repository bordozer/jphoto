define( ["modules/photo/list/photo-list-model"
		, "modules/photo/list/photo-list-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, baseUrl, container ) {

		var photoListEntryModel = new Model.PhotoListEntryModel( { photoId: photoId, baseUrl: baseUrl });
		photoListEntryModel.fetch( {cache: false} );

		var photoListEntryModelView = new View.PhotoListEntryModelView( { model: photoListEntryModel, el: container } );
	}

	return init;

} );