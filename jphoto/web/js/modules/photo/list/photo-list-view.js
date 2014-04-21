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
		], function ( Backbone, $, _
		, photoListEntryContainer, groupOperationsTemplate, contextMenuTemplate, statisticsTemplate, photoNameTemplate, authorLinkTemplate, authorRankTemplate, anonymousPeriodExpirationTimeTemplate
		, ContextMenuModel, ContextMenuView ) {

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

			var element = this.$el;
			element.html( '' );

			if ( this.model.get( 'userOwnThePhoto' ) ) {
				element.addClass( 'block-user-photo' );
			}

			/*element = this.addAdminFlagDiv( photoId, 'admin-special-empty-flag', element );

			if ( this.model.get( 'showAdminFlag_Nude' ) ) {
				element = this.addAdminFlagDiv( photoId, 'admin-special-flag-nude-content', element );
			}

			if ( this.model.get( 'showAdminFlag_Anonymous' ) ) {
				element = this.addAdminFlagDiv( photoId, 'admin-special-flag-anonymous-posting', element );
			}*/

			if ( this.model.get( 'isGroupOperationEnabled' ) ) {
				element.append( this.groupOperationsTemplate( modelJSON ) );
			}

			element.append( this.photoListEntryContainer( modelJSON ) );

			if ( this.model.get( 'showPhotoContextMenu' ) ) {
				element.append( this.contextMenuTemplate( modelJSON ) );

				// TODO: uncomment to render menu immediately
				/*var menuElement = $( '.context-menu-photo-' + photoId, this.$el );

				var photoContextMenuModel = new ContextMenuModel.ContextMenuModel( { entryId: photoId, entryMenuTypeId: 1, baseUrl: this.model.get( 'baseUrl' ) } );
				var photoContextMenuView = new ContextMenuView.ContextMenuView( { model: photoContextMenuModel, el: menuElement } );

				photoContextMenuModel.fetch( { cache: false } );*/
			}

			if ( this.model.get( 'showStatistics' ) ) {
				element.append( this.statisticsTemplate( modelJSON ) );
			}

			element.append( this.photoNameTemplate( modelJSON ) );

			element.append( this.authorLinkTemplate( modelJSON ) );

			if ( this.model.get( 'showUserRank' ) ) {
				element.append( this.authorRankTemplate( modelJSON ) );
			}

			if ( this.model.get( 'showAnonymousPeriodExpirationInfo' ) ) {
				element.append( this.anonymousPeriodExpirationTimeTemplate( modelJSON ) );
			}

//			element.append( '</div>' );
//			element.append( '</div>' );
		},

		addAdminFlagDiv: function ( photoId, cssClass, el ) {
			var adminFlagDivId = "admin-flag-" + photoId + "-" + cssClass;
			el.append( "<div id='" + adminFlagDivId + "' class='" + cssClass + "'>" );

			return $( "#" + adminFlagDivId );
		}

		, events: {
			"click .photo-context-menu-icon": "onPhotoContextMenuIconClick"
		}

		, photoContextMenuIconClick: function() {
			var photoId = this.model.get( 'photoId' );
			initContextMenuForEntry( photoId, 1, $( '.context-menu-photo-' + photoId, this.$el ) ); // TODO: EntryMenuType.PHOTO.getId is hardcoded
		}

		, onPhotoContextMenuIconClick: function( evt ) {
			evt.stopPropagation();
			this.photoContextMenuIconClick();
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );
