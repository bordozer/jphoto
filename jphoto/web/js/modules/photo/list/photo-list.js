define( ["modules/photo/list/photo-list-model"
		, "modules/photo/list/photo-list-view"
		, "components/menu/context-menu-model"
		, "components/menu/context-menu-view"
		, "jquery"], function ( Model, View, ContextMenuModel, ContextMenuView, $ ) {

	function init( photoId, isGroupOperationEnabled, baseUrl, container ) {

		var photoContextMenuModel = new ContextMenuModel.ContextMenuModel( { entryId: photoId, entryMenuTypeId: 1, baseUrl: baseUrl });
		var photoContextMenuView = new ContextMenuView.ContextMenuView( { model: photoContextMenuModel } );

		var photoListEntryModel = new Model.PhotoListEntryModel( { photoId: photoId, baseUrl: baseUrl });
		photoListEntryModel.set( { isGroupOperationEnabled: isGroupOperationEnabled, photoContextMenuModel: photoContextMenuModel } );
		photoListEntryModel.fetch( { cache: false } );

		var photoListEntryView = new View.PhotoListEntryModelView( { model: photoListEntryModel, el: container } );
	}

	return init;

} );