define( ["modules/photo/upload/category/photo-category-handler-model"
		, "modules/photo/upload/category/photo-category-handler-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( authorId, photoId, categoryId, fileSize, container ) {

		var photoCategoryHandlerModel = new Model.PhotoCategoryHandlerModel( { authorId: authorId, photoId: photoId, categoryId: categoryId, fileSize: fileSize });
		photoCategoryHandlerModel.fetch( { cache: false } );

		var photoCategoryHandlerView = new View.PhotoCategoryHandlerView( { model: photoCategoryHandlerModel, el: container } );
	}

	return init;

} );
