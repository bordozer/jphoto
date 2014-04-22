define( ["backbone", "jquery", "underscore"
		, "text!components/menu/templates/context-menu-template.html"
		, "text!components/menu/templates/context-menu-item-template.html"
		], function ( Backbone, $, _, contextMenuTemplate, contextMenuItemTemplate ) {

	'use strict';

	var ContextMenuView = Backbone.View.extend( {

		contextMenuTemplate:_.template( contextMenuTemplate ),
		contextMenuItemTemplate:_.template( contextMenuItemTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		}

		, render:function () {
			var modelJSON = this.model.toJSON();

			var element = this.$el;
			element.html( this.contextMenuTemplate( modelJSON ) );

			var menuId = this.model.get( 'menuId' );
			var menuDivId = this.model.get( 'menuDivId' );
			var entryMenuHeight = this.model.get( 'entryMenuHeight' );

//			console.log( 'The menu is being initialized... ', menuId, element );

			this.renderItems( modelJSON[ 'entryMenuItemDTOs' ], $( '.entry-context-menu-items-ul', element ) );

			$( function () {
				$( '#' + menuId, element ).context_menu( {
					content:$( '#' + menuDivId ).html()
					  , showSpeed:400
					  , width:350
					  , maxHeight: entryMenuHeight
				} );
			} );

			var menuIconElement = $( '#' + menuId, this.$el );
//			console.log( menuIconElement );
			menuIconElement.click();
		}

		, renderItems: function( entryMenuItemDTOs, container ) {

			var model = this.model;

			for ( var i in entryMenuItemDTOs ) {

				var entryMenuItemDTO = entryMenuItemDTOs[ i ];

				var menuItemId = entryMenuItemDTO[ 'menuItemId' ];

				if ( entryMenuItemDTO[ 'menuTypeSeparator' ] ) {
					container.append( "<li><div class='floatleft block-background' style='height: 2px; margin: 2px; width: 95%;'></div></li>" );
					continue;
				}

				var liID = 'li-' + menuItemId;
				container.append( "<li style='font-size: 10px;' class='" + liID + "'></li>" );

				var menuElement = $( this.contextMenuItemTemplate( entryMenuItemDTO ) );

				menuElement.on( 'click',  function( evt ) {

					console.log( model.get( 'menuCommand' ) );

					evt.preventDefault();
					evt.stopPropagation();

					eval( model.get( 'menuCommand' ) );
					model.get( "contextMenuEntryModel" ).refresh();
				});
//				console.log( menuElement.data( 'events' ) );
//				console.log( menuElement );

				var liElement = $( '.' + liID, container );
				liElement.append( menuElement );

//				$( ".entry-menu-item-" + this.model.get( 'uniqueMenuItemId' ), this.$el ).bind( "", );

				if ( entryMenuItemDTO[ 'hasSumMenu' ] ) {

					var ulID = 'ul-' + menuItemId;
					liElement.append( "<ul class='top-menu-item " + ulID + "'></ul>" );
					var ulElement = $( "." + ulID, liElement );
					this.renderItems( entryMenuItemDTO[ 'entrySubMenuItemDTOs' ], ulElement );
				}
			}
		}
	});

	return { ContextMenuView:ContextMenuView };
});