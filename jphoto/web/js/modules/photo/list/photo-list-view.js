define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list/templates/photo-list-entry-template.html"
		], function ( Backbone, $, _, photoListEntryTemplate ) {

	'use strict';

	var PhotoListEntryModelView = Backbone.View.extend( {

		template:_.template( photoListEntryTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el.html( this.template( modelJSON ) );
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );
