define( ["backbone"], function ( Backbone ) {

	var Activity = Backbone.Model.extend( {

										  } );

	var Activities = Backbone.Collection.extend( {
													 model:Activity,
													 url:"/jphoto/app/activityStream/json/"
												 } );

	return {Activity:Activity, Activities:Activities};
} );
