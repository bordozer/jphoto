define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list/templates/photo-list-entry-container.html"
		, "components/menu/context-menu-model"
		, "components/menu/context-menu-view"
		, "modules/icon/entry-icon-model"
		, "modules/icon/entry-icon-view"
		], function ( Backbone, $, _
		, photoListEntryContainer
		, ContextMenuModel, ContextMenuView
		, EntryIconModel, EntryIconView
		) {

	'use strict';

	var PhotoListEntryModelView = Backbone.View.extend( {

		photoListEntryContainer:_.template( photoListEntryContainer ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.photoListEntryContainer( modelJSON ) );

			this.renderBookmarkIcons( this.$( '.photo-icons' ) );
		}

		, events: {
			"click .photo-context-menu-icon": "onPhotoContextMenuIconClick"
		}

		, photoContextMenuIconClick: function() {
			var photoId = this.model.get( 'photoId' );

			var menuElement = $( '.context-menu-photo-' + photoId, this.$el );

			var photoContextMenuModel = new ContextMenuModel.ContextMenuModel( { entryId: photoId, entryMenuTypeId: 1, contextMenuEntryModel : this.model, contextMenuEntryView : this } );
			var photoContextMenuView = new ContextMenuView.ContextMenuView( { model: photoContextMenuModel, el: menuElement } );

			photoContextMenuModel.fetch( { cache: false } );
		}

		, renderBookmarkIcons: function( iconsContainer ) {

			var model = this.model;
			var el = this.$el;

			_.each( this.model.get( 'photoBookmarkIcons' ), function( photoBookmarkIcon ) {

				var container = $( '<div style="display: inline-block; margin-right: 3px;"></div>' );
				iconsContainer.append( container );

				var entryIconModel = new EntryIconModel.EntryIconModel( { userId: model.get( 'userId' ), bookmarkEntryId: model.get( 'photoId' ), bookmarkEntryTypeId: photoBookmarkIcon[ 'favoriteEntryTypeId' ] } );
				var entryIconView = new EntryIconView.EntryIconView( { model: entryIconModel, el: container } );

				entryIconModel.fetch( { cache: false } );
			});
		}

		, onPhotoContextMenuIconClick: function( evt ) {
			evt.stopPropagation();
			this.photoContextMenuIconClick();
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );
