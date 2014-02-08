define( ["backbone", "jquery", "underscore", "text!activity/templates/list.html", "text!activity/templates/activity.html"], function ( Backbone, $, _, listTemplate, activityTemplate ) {

	var ActivitiesView = Backbone.View.extend( {
												   template:_.template( activityTemplate ),
												   initialize:function () {
//													   console.log( this.model );
													   this.render(); // draw an empty list template
													   this.listenTo( this.model, "add", this.renderActivity );
													   //													   this.$list = this.$( ".activity-stream-entries" );
													   this.model.fetch();
												   },

												   render:function () {
													   this.$el.html( $( listTemplate ) );
												   },

												   renderActivity:function ( activity ) {
													   var rendered = this.template( activity.toJSON() );
													   this.$( ".activity-stream-entries" ).append( $( rendered ) );
												   }
											   } );

	return { ActivitiesView:ActivitiesView };
} );
