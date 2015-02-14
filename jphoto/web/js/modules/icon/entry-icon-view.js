define( ["backbone", "jquery", "underscore" ], function ( Backbone, $, _ ) {

	'use strict';

	var EntryIconView = Backbone.View.extend( {

		initialize: function( options ) {
			this.listenTo( this.model, "sync", this.render );

			this.iconSize = options.iconSize;
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			console.log( this.model.get( 'icon' ), this.iconSize );
			this.$el.html( _.template( "<img src='<%=imageURL%>/favorites/<%=iconSize%>/<%=icon%>' width='<%=iconSize%>' height='<%=iconSize%>' title='<%=title%>' class='bookmark-entry-icon' />", {
				imageURL: Backbone.JPhoto.imageFolder()
				, icon: this.model.get( 'icon' )
				, title: this.model.get( 'title' )
				, iconSize: this.iconSize
			} ) );
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

			if ( confirm( this.model.get( 'confirmation' ) + '?' ) ) {
				this.bookmarkEntryIconClick();
			}
		}
	} );

	return { EntryIconView:EntryIconView };
} );