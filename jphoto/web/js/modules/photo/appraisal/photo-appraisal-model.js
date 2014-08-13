define( ["backbone"], function ( Backbone ) {

	var PhotoAppraisalModel = Backbone.Model.extend( {

//		idAttribute: 'photoId',

		initialize: function ( options ) {
			this.url = Backbone.JPhoto.url( "/rest/photos/" + options.photoId + "/appraisal/" );
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	return { PhotoAppraisalModel:PhotoAppraisalModel };
} );