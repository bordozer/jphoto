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

			console.log( 'The menu is being initialized... ', menuId, element );

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
			menuIconElement.click();
		}

		, renderItems: function( entryMenuItemDTOs, container ) {

			for ( var i in entryMenuItemDTOs ) {

				var entryMenuItemDTO = entryMenuItemDTOs[ i ];

				var menuItemId = entryMenuItemDTO[ 'menuItemId' ];

				if ( entryMenuItemDTO[ 'menuTypeSeparator' ] ) {
					container.append( "<li><div class='floatleft block-background' style='height: 2px; margin: 2px; width: 95%;'></div></li>" );
					continue;
				}

				var liID = 'li-' + menuItemId;
				container.append( "<li style='font-size: 10px;' class='" + liID + "'></li>" );
				var liElement = $( '.' + liID, container );

				liElement.append( this.contextMenuItemTemplate( entryMenuItemDTO ) );

				if ( entryMenuItemDTO[ 'hasSumMenu' ] ) {

					var ulID = 'ul-' + menuItemId;
					liElement.append( "<ul class='top-menu-item " + ulID + "'></ul>" );
					var ulElement = $( "." + ulID, liElement );
					this.renderItems( entryMenuItemDTO[ 'entrySubMenuItemDTOs' ], ulElement );
				}
			}
		}

		/*
		events: {
			"click .entry-context-menu-icon": "onClickMenuIcon"
			, "click .entry-menu-item": "onClickMenuItem"
		}

		,clickMenuIcon: function() {
//			showAlertMessage( 'Menu icon has been clicked' );
			alert( 'Menu icon has been clicked' );
		}

		,clickMenuItem: function() {
//			eval( this.model.get( 'menuItemCommand' ) );
//			showAlertMessage( 'Menu item has been clicked' );
			alert( 'Menu item has been clicked' );
		}

		,onClickMenuIcon: function( evt ) {
			evt.stopPropagation();
			this.clickMenuIcon();
		}

		,onClickMenuItem: function( evt ) {
			evt.stopPropagation();
			this.clickMenuItem();
		}*/
	});

	return { ContextMenuView:ContextMenuView };
});