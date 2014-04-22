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

			var menu_a = $( '#entry-context-menu-icon-a', element );

			$( function () {
				menu_a.context_menu( {
					content:$( '#' + menuDivId ).html()
					  , showSpeed:400
					  , width:350
					  , maxHeight: entryMenuHeight
				} );
			} );

//			console.log( menuIconElement );
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

				this.bindMenuElementClick( menuItemElement );

				if ( entryMenuItemDTO[ 'hasSumMenu' ] ) {
					var ul = $( "<ul class='top-menu-item'></ul>" );
					this.renderItems( entryMenuItemDTO[ 'entrySubMenuItemDTOs' ], ul );
					li.append( ul );
				}

				container.append( li );
			}
		}

		, bindMenuElementClick: function( menuElement ) {
			menuElement.on( 'click',  function( evt ) {

				var model = this.model;

				console.log( model.get( 'menuCommand' ) );

				evt.preventDefault();
				evt.stopPropagation();

				eval( model.get( 'menuCommand' ) );
				model.get( "contextMenuEntryModel" ).refresh();
			});
		}
	});

	return { ContextMenuView:ContextMenuView };
});