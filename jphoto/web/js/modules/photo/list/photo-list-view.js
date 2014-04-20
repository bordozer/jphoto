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

			this.$el.html( '<div' + ( this.model.get( 'userOwnThePhoto' ) ? " class='block-user-photo'" : "") + ' style="float: left; width: 100%; height: 100%;">' );

			if ( this.model.get( 'isGroupOperationEnabled' ) ) {
				this.$el.append( this.groupOperationsTemplate( modelJSON ) );
			}

			this.$el.append( this.photoListEntryContainer( modelJSON ) );

			if ( this.model.get( 'showPhotoContextMenu' ) ) {
				this.$el.append( this.contextMenuTemplate( modelJSON ) );
			}

			if ( this.model.get( 'showStatistics' ) ) {
				this.$el.append( this.statisticsTemplate( modelJSON ) );
			}

			this.$el.append( this.photoNameTemplate( modelJSON ) );

			this.$el.append( this.authorLinkTemplate( modelJSON ) );

			if ( this.model.get( 'showUserRank' ) ) {
				this.$el.append( this.authorRankTemplate( modelJSON ) );
			}

			if ( this.model.get( 'showAnonymousPeriodExpirationInfo' ) ) {
				this.$el.append( this.anonymousPeriodExpirationTimeTemplate( modelJSON ) );
			}

			if ( this.model.get( 'showAdminFlag_Anonymous' ) ) {
				this.$el.addClass( 'admin-special-flag-anonymous-posting' );
			}

			if ( this.model.get( 'showAdminFlag_Nude' ) ) {
				this.$el.addClass( 'admin-special-flag-nude-content' );
			}

			this.$el.append( '</div>' );
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );
