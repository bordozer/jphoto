define( ["backbone", "jquery", "underscore", "text!activity/templates/list.html", "text!activity/templates/activity.html"], function ( Backbone, $, _, listTemplate, activityTemplate ) {

	var ActivitiesView = Backbone.View.extend( {
												   initialize:function () {
													   console.log( this.model );
													   this.listenTo( this.model, "add", this.renderActivity );
													   this.model.fetch();
												   },

												   render:function () {
													   this.$el.html( $( listTemplate ) );
												   },

												   renderActivity:function ( activity ) {
													   console.log( activity );
												   }
											   } );

	return { ActivitiesView:ActivitiesView };
} );
