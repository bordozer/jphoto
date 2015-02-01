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

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.photoListEntryContainer( modelJSON ) );

			this.renderBookmarkIcons( this.$( '.photo-icons' ) );

			var hasNude = this.model.get( 'showAdminFlag_Nude' );
			var photoCategoryCanContainNudeContent = this.model.get( 'photoCategoryCanContainNudeContent' );

			if ( ! photoCategoryCanContainNudeContent && hasNude ) {
				console.log( 'invalid!' );
				this.$( '.photo-list-entry-icon-nude-content' ).addClass( 'invalid-nude-content' );
			}
		}

		, events: {
			"click .photo-context-menu-icon": "onPhotoContextMenuIconClick"
			, "click .photo-list-entry-icon-nude-content": 'onNudeContentIconClick'
		}

		, photoContextMenuIconClick: function() {
			var photoId = this.model.get( 'photoId' );

			var menuElement = $( '.context-menu-photo-' + photoId, this.$el );

			var photoContextMenuModel = new ContextMenuModel.ContextMenuModel( { entryId: photoId, entryMenuTypeId: 1, contextMenuEntryModel : this.model, contextMenuEntryView : this } );
			var photoContextMenuView = new ContextMenuView.ContextMenuView( { model: photoContextMenuModel, el: menuElement } );

			photoContextMenuModel.fetch( { cache: false } );
		}

		, refreshPreview: function() {
			this.model.refresh();
		}

		, nudeContentIconClick: function() {
			var photoId = this.model.get( 'photoId' );
			var hasNude = this.model.get( 'showAdminFlag_Nude' );

			var photoCategoryCanContainNudeContent = this.model.get( 'photoCategoryCanContainNudeContent' );
			var photoCategoryContainsNudeContent = this.model.get( 'photoCategoryContainsNudeContent' );

			var refresh = _.bind( this.refreshPreview, this );

			if ( hasNude ) {
				if ( confirm( this.model.get( 'textConfirmRemovingNudeContent' ) ) ) {
					adminPhotoNudeContentRemove( photoId, refresh );
				}

				return;
			}

			if ( ! photoCategoryCanContainNudeContent ) {
				showUIMessage_Information( this.model.get( 'textCategoryCanNotContainNudeContent' ) );
			}

			if ( confirm( this.model.get( 'textConfirmSettingNudeContent' ) ) ) {
				adminPhotoNudeContentSet( photoId, refresh );
			}
		}

		, renderBookmarkIcons: function( iconsContainer ) {

			var model = this.model;
			var el = this.$el;

			_.each( this.model.get( 'photoBookmarkIcons' ), function( photoBookmarkIcon ) {

//				var container = this.$( '.photo-icons' );
//				iconsContainer.append( container );

				var entryIconModel = new EntryIconModel.EntryIconModel( { userId: model.get( 'userId' ), bookmarkEntryId: model.get( 'photoId' ), bookmarkEntryTypeId: photoBookmarkIcon[ 'favoriteEntryTypeId' ] } );
				var entryIconView = new EntryIconView.EntryIconView( { model: entryIconModel, el: iconsContainer } );

				entryIconModel.fetch( { cache: false } );
			});
		}

		, onPhotoContextMenuIconClick: function( evt ) {
			evt.stopPropagation();
			this.photoContextMenuIconClick();
		}

		, onNudeContentIconClick: function( evt ) {
			evt.stopPropagation();
			this.nudeContentIconClick();
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );
