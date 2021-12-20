define( [ "modules/photo/appraisal/photo-appraisal-model"
		, "modules/photo/appraisal/photo-appraisal-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, userId, container ) {

		var photoAppraisalModel = new Model.PhotoAppraisalModel( { photoId: photoId, userId: userId } );
		photoAppraisalModel.fetch( { cache: false } );

		var photoAppraisalCompositeView = new View.PhotoAppraisalCompositeView( { model: photoAppraisalModel, el: container } );
	}

	return init;

} );