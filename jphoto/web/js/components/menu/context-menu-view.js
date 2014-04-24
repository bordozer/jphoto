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

//			console.log( 'The menu is being initialized... ', menuId, element );

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


//			console.log( menu_a );
			menu_a.click();
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

				var li = $( "<li style='font-size: 10px;'></li>" );
				var menuItemElement = $( this.contextMenuItemTemplate( entryMenuItemDTO ) );
				li.append( menuItemElement );

				this.bindMenuElementClick( menuItemElement, entryMenuItemDTO[ 'menuCommand' ] );

				if ( entryMenuItemDTO[ 'hasSumMenu' ] ) {
					var ul = $( "<ul class='top-menu-item'></ul>" );
					this.renderItems( entryMenuItemDTO[ 'entrySubMenuItemDTOs' ], ul );
					li.append( ul );
				}

				container.append( li );

//				console.log( 'bind event: ', menuItemElement, entryMenuItemDTO[ 'menuCommand' ], $._data( menuItemElement[0], "events" ) );
			}
		}

		, bindMenuElementClick: function( menuElement, menuItemCommand ) {

			var model = this.model;

//			console.log( menuItemCommand );

			menuElement.click( function( evt ) {

				function nudeContentChangeCallback() {
					model.get( "contextMenuEntryModel" ).refresh();
				}

				evt.preventDefault();
				evt.stopPropagation();

				eval( menuItemCommand );
			});
		}
	});

	return { ContextMenuView:ContextMenuView };
});