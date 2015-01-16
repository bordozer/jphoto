define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list-big/templates/photo-list-big.html"
		, "components/menu/context-menu-model"
		, "components/menu/context-menu-view"
		], function ( Backbone, $, _
		, template
		, ContextMenuModel, ContextMenuView
		) {

	'use strict';

	var PhotoListEntryModelView = Backbone.View.extend( {

		template:_.template( template ),

		events: {
			"click .photo-context-menu-icon": "onPhotoContextMenuIconClick"
		},

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			var photoId = this.model.get( 'photoId' );

			this.$el.html( this.template( modelJSON ) );
		},

		photoContextMenuIconClick: function() {
			var photoId = this.model.get( 'photoId' );

			var menuElement = $( '.context-menu-photo-' + photoId, this.$el );

			var photoContextMenuModel = new ContextMenuModel.ContextMenuModel( { entryId: photoId, entryMenuTypeId: 1, contextMenuEntryModel : this.model, contextMenuEntryView : this } );
			var photoContextMenuView = new ContextMenuView.ContextMenuView( { model: photoContextMenuModel, el: menuElement } );

			photoContextMenuModel.fetch( { cache: false } );
		},

		onPhotoContextMenuIconClick: function( evt ) {
			evt.stopPropagation();
			this.photoContextMenuIconClick();
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );