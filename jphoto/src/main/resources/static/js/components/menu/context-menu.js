define( ["components/menu/context-menu-model"
		, "components/menu/context-menu-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( entryId, entryMenuTypeId, container ) {

		var contextMenuModel = new Model.ContextMenuModel( { entryId: entryId, entryMenuTypeId: entryMenuTypeId });
		contextMenuModel.fetch( {cache: false} );

		var contextMenuView = new View.ContextMenuView( { model: contextMenuModel, el: container } );
	}

	return init;

} );