define( ["backbone", "jquery", "underscore" ], function ( Backbone, $, _ ) {

	'use strict';

	var EntryIconView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
//			this.model.fetch( { cache: false } );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			console.log( modelJSON );

			this.$el.html( "<img src='" + this.model.get( 'icon' ) + "' width='16' height='16' title='" + this.model.get( 'title' ) + "' class='bookmark-entry-icon' />" );
		}

		, events: {
			"click .bookmark-entry-icon": "onBookmarkEntryIconClick"
		}

		, bookmarkEntryIconClick: function() {
			console.log( 'click' );
		}

		, onBookmarkEntryIconClick: function( evt ) {
			evt.preventDefault();
			evt.stopPropagation();

			this.bookmarkEntryIconClick();
		}
	} );

	return { EntryIconView:EntryIconView };
} );