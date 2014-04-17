define( ["backbone", "jquery", "underscore", "text!modules/admin/user/lock/templates/lockAreaTemplate.html"], function ( Backbone, $, _, userLockTemplate ) {

	'use strict';

	var UserLockView = Backbone.View.extend( {

		template:_.template( userLockTemplate ),

		initialize: function(){
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
//			console.log( this.$el );
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );
		}

	} );

	return { UserLockView:UserLockView };
} );
