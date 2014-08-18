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

			var entryId = modelJSON[ 'entryId' ];

			this.$el.html( this.contextMenuTemplate( modelJSON ) );

			var entryMenuHeight = this.model.get( 'entryMenuHeight' );

			var ul = this.$( '.entry-context-menu-items-ul' );
			this.renderItems( entryId, modelJSON[ 'entryMenuItemDTOs' ], ul );

			var menu_a = this.$( '#entry-context-menu-icon-a' );
			var menu_content = this.$( '.entry-popup-menu' ).html();

			$( function () {
				menu_a.context_menu( {
					content: menu_content
					  , showSpeed:400
					  , width:350
					  , maxHeight: entryMenuHeight
				} );
			} );

			menu_a.click();
		}

		, renderItems: function( entryId, entryMenuItemDTOs, ul_container ) {

			for ( var i in entryMenuItemDTOs ) {

				var entryMenuItemDTO = entryMenuItemDTOs[ i ];

				if ( entryMenuItemDTO[ 'menuTypeSeparator' ] ) {
					ul_container.append( "<li><div class='floatleft block-background' style='height: 2px; margin: 2px; width: 99%;'></div></li>" );
					continue;
				}

				var li = $( "<li class='" + entryMenuItemDTO[ 'menuCssClassBG' ] + "' style='font-size: 10px;'></li>" );
				var menuItemElement = $( this.contextMenuItemTemplate( entryMenuItemDTO ) );
				li.append( menuItemElement );

				this.bindMenuElementClick( entryId, menuItemElement, entryMenuItemDTO[ 'menuCommand' ], entryMenuItemDTO[ 'callbackMessage' ] );

				if ( entryMenuItemDTO[ 'hasSumMenu' ] ) {
					var ul = $( "<ul class='top-menu-item'></ul>" );
					this.renderItems( entryId, entryMenuItemDTO[ 'entrySubMenuItemDTOs' ], ul );
					li.append( ul );
				}

				ul_container.append( li );

			}
		}

		, bindMenuElementClick: function( entryId, menuElement, menuItemCommand, callbackMessage ) {

			var model = this.model;
			var view = this;

			menuElement.click( function( evt ) {

				evt.preventDefault();
				evt.stopPropagation();

				function reloadPhotoCallback() {

					if ( model.get( "contextMenuEntryModel" ) != undefined ) {
						model.get( "contextMenuEntryModel" ).refresh();
					}

					if ( callbackMessage ) {
						showUIMessage_Notification( callbackMessage );
					}
				}

				function deletePhotoFromContextMenu() {

					var view = model.get( "contextMenuEntryView" );

					if ( view == undefined && confirm( Backbone.JPhoto.translate( "Context menu item: Delete photo?" ) ) ) {
						$.ajax( {
							type: 'DELETE',
							url: Backbone.JPhoto.url( '/rest/photos/' + entryId + '/' ),
							success: function ( response ) {
								document.location.href = Backbone.JPhoto.url( 'photos/' );
							},
							error: function () {
								showUIMessage_Error( Backbone.JPhoto.translate( "Context menu item: Server error on photo deletion" ) );
							}
						} );
						return;
					}

					var photoName = view.model.get( 'photoName' );
					if ( ! confirm( photoName + ': ' + Backbone.JPhoto.translate( "Context menu item: Delete photo?" ) ) ) {
						return;
					}

					model.get( "contextMenuEntryModel" ).destroy();
					view.remove();
					view.remove();

					if ( callbackMessage ) {
						showUIMessage_Notification( callbackMessage );
					}
				}

				eval( menuItemCommand );
			});
		}
	});

	return { ContextMenuView:ContextMenuView };
});