define( ["backbone", "jquery", "underscore"
		], function ( Backbone, $, _ ) {

	'use strict';

	var PhotoUploadAllowanceView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( "Photo Upload Allowance" );
		}
	});

	return { PhotoUploadAllowanceView:PhotoUploadAllowanceView };
} );