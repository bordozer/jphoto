define( ["modules/photo/appraisal/photo-appraisal-model"
		, "modules/photo/appraisal/photo-appraisal-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( photoId, userId, baseUrl, container ) {

		var photoAppraisalModel = new Model.PhotoAppraisalModel( { photoId: photoId, userId: userId, baseUrl: baseUrl } );
		photoAppraisalModel.fetch( { cache: false } );

		var photoAppraisalCompositeView = new View.PhotoAppraisalCompositeView( { model: photoAppraisalModel, el: container } );
	}

	return init;

} );