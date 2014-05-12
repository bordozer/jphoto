define( ["backbone", "jquery", "underscore" ], function ( Backbone, $, _ ) {

	'use strict';

	var EntryIconView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			console.log( modelJSON );

			this.$el.html( "<img src='" + this.model.get( 'icon' ) + "' width='16' height='16' title='" + this.model.get( 'title' ) + "' class='bookmark-entry-icon' />" );
		}

		, events: {
			"click .bookmark-entry-icon": "onBookmarkEntryIconClick"
		}

		, save: function() {
			this.model.save()
				.done( _.bind( this.onSave, this ) )
				.fail( _.bind( this.onSaveError, this ) );
		}

		, onSave: function( response ){
			showUIMessage_Notification( this.model.get( 'saveCallbackMessage' ) );
		}

		, onSaveError: function( response ){
			if ( response.status === 422 ) {
				var errorText = '';
				var errors = response[ 'responseJSON' ];
				for ( var i = 0; i < errors.length; i++ ) {
					errorText += errors[ i ][ 'defaultMessage' ] + "\n";
				}

				showUIMessage_Error( errorText );

				this.model.refresh();
			}
		}

		, bookmarkEntryIconClick: function() {
			this.save();
		}

		, onBookmarkEntryIconClick: function( evt ) {
			evt.preventDefault();
			evt.stopPropagation();

			this.bookmarkEntryIconClick();
		}
	} );

	return { EntryIconView:EntryIconView };
} );