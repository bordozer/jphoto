define( ["modules/photo/upload/photo-upload-nude-content-model"
		, "modules/photo/upload/photo-upload-nude-content-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, genreId, baseUrl, container ) {

		var photoUploadNudeContentModel = new Model.PhotoUploadNudeContentModel( { photoId: photoId, genreId: genreId, baseUrl: baseUrl });
		photoUploadNudeContentModel.fetch( { cache: false } );

		var photoUploadNudeContentView = new View.PhotoUploadNudeContentView( { model: photoUploadNudeContentModel, el: container } );
	}

	return init;

} );
