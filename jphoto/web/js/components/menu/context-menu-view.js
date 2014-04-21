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
			this.$el.html( this.contextMenuTemplate( modelJSON ) );

			var menuId = this.model.get( 'menuId' );
			var menuDivId = this.model.get( 'menuDivId' );
			var entryMenuHeight = this.model.get( 'entryMenuHeight' );

			this.renderItems( modelJSON[ 'entryMenuItems' ], $( '.entry-context-menu-items', this.$el ) );

			$( function () {
				$( '#' + menuId ).context_menu( {
					content:$( '#' + menuDivId ).html()
					  , showSpeed:400
					  , width:350
					  , maxHeight: entryMenuHeight
				} );
			} );
		}

		, renderItems: function( contextMenuItems, container ) {

			for ( var i in contextMenuItems ) {
				var menuItem = contextMenuItems[ i ];

				console.log( menuItem );

				if ( menuItem[ 'entryMenuType' ] == "SEPARATOR" ) {
					container.append( "<li><div class='floatleft block-background' style='height: 2px; margin: 2px; width: 95%;'></div></li>" );
					continue;
				}

				container.append( this.contextMenuItemTemplate( menuItem ) );
				if ( menuItem[ 'subMenu' ] ) {
					this.renderItems( menuItem[ 'entrySubMenu' ][ 'entryMenuItems' ], container )
//					container.append( "<ul class='top-menu-item entry-context-menu-items'>" + this.renderItems( menuItem.entrySubMenu.entryMenuItems, container ) + '</ul>' );
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