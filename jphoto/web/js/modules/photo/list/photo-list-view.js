define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list/templates/photo-list-entry-container.html"
		, "text!modules/photo/list/templates/statistics.html"
		, "text!modules/photo/list/templates/photo-name.html"
		, "text!modules/photo/list/templates/author-link.html"
		, "text!modules/photo/list/templates/author-rank.html"
		], function ( Backbone, $, _, photoListEntryContainer, statisticsTemplate, photoNameTemplate, authorLinkTemplate, authorRankTemplate ) {

	'use strict';

	var PhotoListEntryModelView = Backbone.View.extend( {

		photoListEntryContainer:_.template( photoListEntryContainer ),
		statisticsTemplate:_.template( statisticsTemplate ),
		photoNameTemplate:_.template( photoNameTemplate ),
		authorLinkTemplate:_.template( authorLinkTemplate ),
		authorRankTemplate:_.template( authorRankTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.photoListEntryContainer( modelJSON ) );

			if ( this.model.get( 'showStatistics' ) ) {
				this.$el.append( this.statisticsTemplate( modelJSON ) );
			}

			this.$el.append( this.photoNameTemplate( modelJSON ) );

			this.$el.append( this.authorLinkTemplate( modelJSON ) );

			this.$el.append( this.authorRankTemplate( modelJSON ) );
		}
	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );
