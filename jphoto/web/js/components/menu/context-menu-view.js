define( ["backbone", "jquery", "underscore"
		, "text!components/menu/templates/context-menu-template.html"
		], function ( Backbone, $, _, contextMenuTemplate ) {

	'use strict';

	var ContextMenuView = Backbone.View.extend( {

		contextMenuTemplate:_.template( contextMenuTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el = this.model.get( 'container' );
			this.$el.html( this.contextMenuTemplate( modelJSON ) );
		}
	});

	return { ContextMenuView:ContextMenuView };
});