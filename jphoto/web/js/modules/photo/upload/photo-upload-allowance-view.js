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

				var div = $( "<div style='float: left; width: 99%; margin-bottom: 10px;'></div>" );

				if ( ! allowance[ 'passed' ] ) {
					div.addClass( 'redfont' );
				}

				div.html( '&nbsp;&nbsp;' + allowance[ 'uploadRuleDescription' ] );

				el.append( div );
			});
		}
	});

	return { PhotoUploadAllowanceView:PhotoUploadAllowanceView };
} );