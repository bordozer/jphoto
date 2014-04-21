define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list/templates/photo-list-entry-container.html"
		, "text!modules/photo/list/templates/group-operations.html"
		, "text!modules/photo/list/templates/context-menu.html"
		, "text!modules/photo/list/templates/statistics.html"
		, "text!modules/photo/list/templates/photo-name.html"
		, "text!modules/photo/list/templates/author-link.html"
		, "text!modules/photo/list/templates/author-rank.html"
		, "text!modules/photo/list/templates/anonymous-period-expiration-time.html"
		], function ( Backbone, $, _, photoListEntryContainer, groupOperationsTemplate, contextMenuTemplate, statisticsTemplate, photoNameTemplate, authorLinkTemplate
		, authorRankTemplate, anonymousPeriodExpirationTimeTemplate ) {

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

			element = this.addAdminFlagDiv( photoId, 'admin-special-empty-flag', element );

			if ( this.model.get( 'showAdminFlag_Nude' ) ) {
				element = this.addAdminFlagDiv( photoId, 'admin-special-flag-nude-content', element );
			}

			if ( this.model.get( 'showAdminFlag_Anonymous' ) ) {
				element = this.addAdminFlagDiv( photoId, 'admin-special-flag-anonymous-posting', element );
			}

			if ( this.model.get( 'isGroupOperationEnabled' ) ) {
				element.append( this.groupOperationsTemplate( modelJSON ) );
			}

			element.append( this.photoListEntryContainer( modelJSON ) );

			if ( this.model.get( 'showPhotoContextMenu' ) ) {
				element.append( this.contextMenuTemplate( modelJSON ) );

				var menuContainer = $( '.context-menu-photo-' + photoId, this.$el );
				var contextModel = this.model.get( 'photoContextMenuModel' );
				contextModel.set( { container: menuContainer } );
				contextModel.fetch( { cache: false } );
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

//			if ( this.model.get( 'showAdminFlag_Anonymous' ) ) {
//				this.$el.addClass( 'admin-special-flag-anonymous-posting' );
//			}

//			if ( this.model.get( 'showAdminFlag_Nude' ) ) {
//				element.append( "</div>" );
//				var div = $( "#photo-container-" + photoId );
//				console.log( div );
//				div.addClass( 'admin-special-flag-nude-content', this.$el );
//				this.$el.addClass( 'admin-special-flag-nude-content' );
//				this.addAdminFlag( 2, photoId, this.$el );
//			}

			element.append( '</div>' );
		},

		addAdminFlagDiv: function ( photoId, cssClass, el ) {
			var adminFlagDivId = "admin-flag-" + photoId + "-" + cssClass;
			el.append( "<div id='" + adminFlagDivId + "' class='" + cssClass + "'>" );

			return $( "#" + adminFlagDivId );
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );
