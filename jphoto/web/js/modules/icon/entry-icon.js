define( ["modules/icon/entry-icon-model"
		, "modules/icon/entry-icon-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( userId, bookmarkEntryId, bookmarkEntryTypeId, baseUrl, container ) {
		var entryIconModel = new Model.EntryIconModel( { userId: userId, bookmarkEntryId: bookmarkEntryId, bookmarkEntryTypeId: bookmarkEntryTypeId, baseUrl: baseUrl } );
		var entryIconView = new View.EntryIconView( { model: entryIconModel, el: container } );

		entryIconModel.fetch( { cache: false } );
	}

	return init;
} );