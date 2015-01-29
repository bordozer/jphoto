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

//			var photoId = this.model.get( 'photoId' );
//			console.log( modelJSON );

			this.$el.html( this.photoListEntryContainer( modelJSON ) );

			/*if ( this.model.get( 'userOwnThePhoto' ) ) {
				this.$el.addClass( 'block-user-photo' );
			}

			var element = $( "<div class='panel panel-primary photo-list-entry-container admin-special-empty-flag'></div>" );

			var element_admin_flag_1 = $( "<div class='admin-special-empty-flag'></div>" );
			if ( this.model.get( 'showAdminFlag_Nude' ) ) {
				element_admin_flag_1 = $( "<div class='admin-special-empty-flag admin-special-flag-nude-content'></div>" );
			}
			element.append( element_admin_flag_1 );

			var element_admin_flag_2 = $( "<div class='admin-special-empty-flag'></div>" );
			if ( this.model.get( 'showAdminFlag_Anonymous' ) ) {
				element_admin_flag_2 = $( "<div class='admin-special-empty-flag admin-special-flag-anonymous-posting'></div>" );
			}
			element_admin_flag_1.append( element_admin_flag_2 );

			if ( this.model.get( 'memberOfAlbum' ) ) {
				element_admin_flag_2.append( $( "<div class='photo-is-in-album'></div>" ) );
			}

			var top = 280;
			_.each( this.model.get( 'specialRestrictionIcons' ), function( iconDTO ) {
				top = top + 20;
				element_admin_flag_2.append( $( "<div class='admin-special-flag-restriction' title='" + iconDTO.restrictionTypeName + "' style='top: " + top + "px; background-image: url(" + Backbone.JPhoto.imageFolder() + "/" + iconDTO.icon + ");'></div>" ) );
			} );

			if ( this.model.get( 'isGroupOperationEnabled' ) ) {
				element_admin_flag_2.append( this.groupOperationsTemplate( modelJSON ) );
			}

			element_admin_flag_2.append( this.photoListEntryContainer( modelJSON ) );

			if ( this.model.get( 'showPhotoContextMenu' ) ) {
				element_admin_flag_2.append( this.contextMenuTemplate( modelJSON ) );
			}

			if ( this.model.get( 'showStatistics' ) ) {
				element_admin_flag_2.append( this.statisticsTemplate( modelJSON ) );
			}

			element_admin_flag_2.append( this.photoNameTemplate( modelJSON ) );

			element_admin_flag_2.append( this.authorLinkTemplate( modelJSON ) );

			if ( this.model.get( 'showUserRank' ) ) {
				element_admin_flag_2.append( this.authorRankTemplate( modelJSON ) );
			}

			if ( this.model.get( 'showAnonymousPeriodExpirationInfo' ) ) {
				element_admin_flag_2.append( this.anonymousPeriodExpirationTimeTemplate( modelJSON ) );
			}

			this.$el.html( element );*/

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
