define( ["backbone"], function ( Backbone ) {

	var PhotoAppraisalModel = Backbone.Model.extend( {

//		idAttribute: 'photoId',

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/photos/" + options.photoId + "/appraisal/";
		}
	});

	return { PhotoAppraisalModel:PhotoAppraisalModel };
} );