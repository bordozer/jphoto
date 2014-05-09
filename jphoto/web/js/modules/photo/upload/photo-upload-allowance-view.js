define( ["backbone", "jquery", "underscore"
		], function ( Backbone, $, _ ) {

	'use strict';

	var PhotoUploadAllowanceView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			var el = this.$el;

			el.html( '' );

			_.each( this.model.get( 'photoUploadAllowance' ), function( allowance ) {
				el.append( '&nbsp;&nbsp;' );
				el.append( allowance[ 'uploadRuleDescription' ] );
				el.append( '<br />' );
			});
		}
	});

	return { PhotoUploadAllowanceView:PhotoUploadAllowanceView };
} );