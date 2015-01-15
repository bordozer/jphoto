define( ["backbone", "jquery", "underscore"
		, "text!modules/photo/list-big/templates/photo-list-big.html"
		, "components/menu/context-menu-model"
		, "components/menu/context-menu-view"
		, "modules/icon/entry-icon-model"
		, "modules/icon/entry-icon-view"
		], function ( Backbone, $, _
		, photoListEntryContainer
		, ContextMenuModel, ContextMenuView
		, EntryIconModel, EntryIconView
		) {

	'use strict';

	var PhotoListEntryModelView = Backbone.View.extend( {

	} );

	return { PhotoListEntryModelView:PhotoListEntryModelView };
} );