define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list/templates/photo-list-entry-container.html"
		, "text!modules/photo/list/templates/group-operations.html"
		, "text!modules/photo/list/templates/context-menu.html"
		, "text!modules/photo/list/templates/statistics.html"
		, "text!modules/photo/list/templates/photo-name.html"
		, "text!modules/photo/list/templates/author-link.html"
		, "text!modules/photo/list/templates/author-rank.html"
		, "text!modules/photo/list/templates/anonymous-period-expiration-time.html"
		, "components/menu/context-menu-model"
		, "components/menu/context-menu-view"
		, "modules/icon/entry-icon-model"
		, "modules/icon/entry-icon-view"
		], function ( Backbone, $, _
		, photoListEntryContainer, groupOperationsTemplate, contextMenuTemplate, statisticsTemplate, photoNameTemplate, authorLinkTemplate, authorRankTemplate, anonymousPeriodExpirationTimeTemplate
		, ContextMenuModel, ContextMenuView
		, EntryIconModel, EntryIconView
		) {

	'use strict';

	var PhotoListEntryModelView = Backbone.View.extend( {

		photoListEntryContainer:_.template( photoListEntryContainer ),
		groupOperationsTemplate:_.template( groupOperationsTemplate ),
		contextMenuTemplate:_.template( contextMenuTemplate ),
		statisticsTemplate:_.template( statisticsTemplate ),
		photoNameTemplate:_.template( photoNameTemplate ),
		authorLinkTemplate:_.template( authorLinkTemplate ),
		authorRankTemplate:_.template( authorRankTemplate ),
		anonymousPeriodExpirationTimeTemplate:_.template( anonymousPeriodExpirationTimeTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			var photoId = this.model.get( 'photoId' );

			if ( this.model.get( 'userOwnThePhoto' ) ) {
				this.$el.addClass( 'block-user-photo' );
			}

			var element = $( "<div class='admin-special-empty-flag'></div>" );

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

			this.$el.html( element );

			this.renderBookmarkIcons();
		}

		, events: {
			"click .photo-context-menu-icon": "onPhotoContextMenuIconClick"
		}

		, photoContextMenuIconClick: function() {
			var photoId = this.model.get( 'photoId' );

			var menuElement = $( '.context-menu-photo-' + photoId, this.$el );
//			console.log( 'Initializing a context menu for ', photoId, menuElement );

			var photoContextMenuModel = new ContextMenuModel.ContextMenuModel( { entryId: photoId, entryMenuTypeId: 1, baseUrl: this.model.get( 'baseUrl' ), contextMenuEntryModel : this.model } );
			var photoContextMenuView = new ContextMenuView.ContextMenuView( { model: photoContextMenuModel, el: menuElement } );

			photoContextMenuModel.fetch( { cache: false } );
		}

		, renderBookmarkIcons: function() {
			var container = $( '.photo-icons', this.$el );
			var model = this.model;

			_.each( this.model.get( 'photoBookmarkIcons' ), function( photoBookmarkIcon ) {
				var entryIconModel = new EntryIconModel.EntryIconModel( { userId: model.get( 'userId' ), bookmarkEntryId: model.get( 'photoId' ), bookmarkEntryTypeId: photoBookmarkIcon[ 'favoriteEntryTypeId' ], baseUrl: model.get( 'baseUrl' ) } );
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
