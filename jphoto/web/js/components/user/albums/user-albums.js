define( ["components/user/albums/user-albums-model"
		, "components/user/albums/user-albums-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( options ) {

		var entriesModel = new Model.EntriesModel( {
				userId: options.userId
				, selectedAlbumIds: options.selectedAlbumIds
				, groupSelectionClass: options.groupSelectionClass
				, translationDTO: options.translationDTO
			} );

		var entryListView = new View.EntryListView( {
				model: entriesModel
				, el: options.container
				, onEdit: options.onEdit
				, onDelete: options.onDelete
		} );
		entryListView.renderHeader();
		entryListView.render();
	}

	return init;
} );