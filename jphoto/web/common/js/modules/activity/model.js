define( ["backbone"], function ( Backbone ) {

	var Activity = Backbone.Model.extend( {

										  } );

	var Activities = Backbone.Collection.extend( {
													 model:Activity,
													 url:"/activityStream/json/" /* TODO: pass context here */
												 } );

	return {Activity:Activity, Activities:Activities};
} );
