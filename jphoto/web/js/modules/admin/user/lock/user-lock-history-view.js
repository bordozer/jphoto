define( ["backbone", "jquery", "underscore"
		, "text!modules/admin/user/lock/templates/user-lock-history-template.html"], function ( Backbone, $, _, userLockTemplate ) {

	'use strict';

	var UserLockHistoryView = Backbone.View.extend( {

		template:_.template( userLockTemplate ),

		initialize: function(){
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );
		}

	} );

	return { UserLockHistoryView:UserLockHistoryView };
} );
