define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/edit/templates/photo-edit-data-template.html"
		], function ( Backbone, $, _, photoEditTemplate ) {

	'use strict';

	var PhotoEditView = Backbone.View.extend( {

		photoEditTemplate:_.template( photoEditTemplate ),

		initialize: function() {
			this.model.fetch( { cache: false } );
			this.render();
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.photoEditTemplate( modelJSON ) );
		}
	});

	return { PhotoEditView:PhotoEditView };
});