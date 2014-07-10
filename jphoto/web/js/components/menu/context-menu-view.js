define( ["backbone", "jquery", "underscore", 'context_menu'
		, "text!components/menu/templates/context-menu-template.html"
		, "text!components/menu/templates/context-menu-item-template.html"
		], function ( Backbone, $, _, context_menu, contextMenuTemplate, contextMenuItemTemplate ) {

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

			var ul = $( '.entry-context-menu-items-ul', element );
			this.renderItems( modelJSON[ 'entryMenuItemDTOs' ], ul );

			var menu_a = $( '#entry-context-menu-icon-a', element );

			$( function () {
				menu_a.context_menu( {
					content:$( '#' + menuDivId ).html()
					  , showSpeed:400
					  , width:350
					  , maxHeight: entryMenuHeight
				} );
			} );

			menu_a.click();
		}

		, renderItems: function( entryMenuItemDTOs, ul_container ) {

			var model = this.model;
			for ( var i in entryMenuItemDTOs ) {

				var entryMenuItemDTO = entryMenuItemDTOs[ i ];

//				var menuItemId = entryMenuItemDTO[ 'menuItemId' ];

				if ( entryMenuItemDTO[ 'menuTypeSeparator' ] ) {
					ul_container.append( "<li><div class='floatleft block-background' style='height: 2px; margin: 2px; width: 95%;'></div></li>" );
					continue;
				}

				var li = $( "<li class='" + entryMenuItemDTO[ 'menuCssClassBG' ] + "' style='font-size: 10px;'></li>" );
				var menuItemElement = $( this.contextMenuItemTemplate( entryMenuItemDTO ) );
				li.append( menuItemElement );
				console.log( menuItemElement );

				this.bindMenuElementClick( menuItemElement, entryMenuItemDTO[ 'menuCommand' ], entryMenuItemDTO[ 'callbackMessage' ] );

				if ( entryMenuItemDTO[ 'hasSumMenu' ] ) {
					var ul = $( "<ul class='top-menu-item'></ul>" );
					this.renderItems( entryMenuItemDTO[ 'entrySubMenuItemDTOs' ], ul );
					li.append( ul );
				}

				ul_container.append( li );

			}
		}

		, bindMenuElementClick: function( menuElement, menuItemCommand, callbackMessage ) {

			var model = this.model;

			menuElement.click( function( evt ) {

				function reloadPhotoCallback() {
					model.get( "contextMenuEntryModel" ).refresh();
					if ( callbackMessage ) {
						showUIMessage_Notification( callbackMessage );
					}
				}

				evt.preventDefault();
				evt.stopPropagation();

				eval( menuItemCommand );
			});
		}
	});

	return { ContextMenuView:ContextMenuView };
});