define( ["backbone"], function ( Backbone ) {

	var PhotoAppraisalModel = Backbone.Model.extend( {

//		idAttribute: 'photoId',

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/photos/" + options.photoId + "/appraisal/";
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	return { PhotoAppraisalModel:PhotoAppraisalModel };
} );