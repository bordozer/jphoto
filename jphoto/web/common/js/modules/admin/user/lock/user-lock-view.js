define( ["backbone", "jquery", "underscore", "text!modules/admin/user/lock/templates/lockAreaTemplate.html"], function ( Backbone, $, _, userLockTemplate ) {

	'use strict';

	var UserLockView = Backbone.View.extend( {

		template:_.template( userLockTemplate ),

		initialize: function(){
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			console.log( modelJSON );
			console.log( this.$el );

			this.$el.html( this.template( modelJSON ) );
		}
	} );

	return { UserLockView:UserLockView };
} );
