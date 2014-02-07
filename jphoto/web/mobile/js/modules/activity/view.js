define( ["backbone", "jquery", "underscore"], function ( Backbone, $, _ ) {

	var ActivitiesView = Backbone.View.extend( {

												   render:function () {
													   this.$el.html( "Rendered" );
												   }
											   } );

	return { ActivitiesView:ActivitiesView };
} );
