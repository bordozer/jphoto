define( ["backbone", "jquery", "underscore"
		, "text!components/menu/templates/context-menu-template.html"
		], function ( Backbone, $, _, contextMenuTemplate ) {

	'use strict';

	var ContextMenuView = Backbone.View.extend( {

		contextMenuTemplate:_.template( contextMenuTemplate ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		events: {
			"click .entry-context-menu-icon": "onClickMenuIcon"
			, "click .entry-menu-item": "onClickMenuItem"
		},

		render:function () {
			var modelJSON = this.model.toJSON();
			this.$el = this.model.get( 'container' );
			this.$el.html( this.contextMenuTemplate( modelJSON ) );

			var menuId = this.model.get( 'menuId' );
			var menuDivId = this.model.get( 'menuDivId' );
			var entryMenuHeight = this.model.get( 'entryMenuHeight' );

			/*$( function () {
				$( '#' + menuId ).context_menu( {
					content:$( '#' + menuDivId ).html()
					  , showSpeed:400
					  , width:350
					  , maxHeight: entryMenuHeight
				} );
			} );*/
		},

		clickMenuIcon: function() {
			showAlertMessage( 'Menu icon has been clicked' );
//			alert( 'Is not implemented yet' );
		},

		clickMenuItem: function() {
			eval( this.model.get( 'menuItemCommand' ) );
			showAlertMessage( 'Menu item has been clicked' );
//			alert( 'Is not implemented yet' );
		},

		onClickMenuIcon: function( evt ) {
			evt.stopPropagation();
			this.clickMenuIcon();
		},

		onClickMenuItem: function( evt ) {
			evt.stopPropagation();
			this.clickMenuItem();
		}
	});

	return { ContextMenuView:ContextMenuView };
});