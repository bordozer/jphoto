define( ["modules/photo/upload/photo-upload-allowance-model"
		, "modules/photo/upload/photo-upload-allowance-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( userId, genreId, fileSize, baseUrl, container ) {

		var photoUploadAllowanceModel = new Model.PhotoUploadAllowanceModel( { userId: userId, genreId: genreId, fileSize: fileSize, baseUrl: baseUrl });
		photoUploadAllowanceModel.fetch( { cache: false } );

		var photoUploadAllowanceView = new View.PhotoUploadAllowanceView( { model: photoUploadAllowanceModel, el: container } );
	}

	return init;

} );