define( ["modules/icon/entry-icon-model"
		, "modules/icon/entry-icon-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( userId, bookmarkEntryId, bookmarkEntryTypeId, iconSize, container ) {

		var entryIconModel = new Model.EntryIconModel( { userId: userId, bookmarkEntryId: bookmarkEntryId, bookmarkEntryTypeId: bookmarkEntryTypeId } );
		var entryIconView = new View.EntryIconView( { model: entryIconModel, el: container, iconSize: iconSize } );

		entryIconModel.fetch( { cache: false } );
	}

	return init;
} );