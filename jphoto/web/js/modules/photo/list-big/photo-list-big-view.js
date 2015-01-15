define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list-big/templates/photo-list-big.html"
		, "components/menu/context-menu-model"
		, "components/menu/context-menu-view"
		, "modules/icon/entry-icon-model"
		, "modules/icon/entry-icon-view"
		], function ( Backbone, $, _
		, template
		, ContextMenuModel, ContextMenuView
		, EntryIconModel, EntryIconView
		) {

	'use strict';

	var PhotoListEntryModelView = Backbone.View.extend( {

		template:_.template( template ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			var photoId = this.model.get( 'photoId' );

			this.$el.html( this.template( modelJSON ) );
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );