define( ["backbone", "jquery", "underscore", "text!admin/user/lock/templates/lockAreaTemplate.html"], function ( Backbone, $, _, userLockAreaTemplate ) {

	var UserLockAreaView = Backbone.View.extend( {
												   userLockAreaTemplate:_.template( userLockAreaTemplate ),

												   initialize:function () {
													   this.listenTo( this.model, 'change', this.renderUserLockArea );
													   this.model.fetch();
												   },

												   renderUserLockArea:function ( userLockAreaModel ) {
													   var json = userLockAreaModel.toJSON();
													   var rendered = this.userLockAreaTemplate( json );
													   $( ".userLockAreaDiv" ).html( $( rendered ) );
												   }
											   } );

	return { UserLockAreaView:UserLockAreaView };
} );
