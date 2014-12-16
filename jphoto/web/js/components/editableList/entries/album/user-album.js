define( [ "jquery"
	, "components/editableList/entries/album/user-album-model"
	, "components/editableList/entries/album/user-album-view"
	, "components/editableList/editable-list-view"
], function ( $, Model, CompositeView, View ) {

	function init( options ) {

		var userAlbumsModel = new Model.UserAlbumsModel( {
			userId: options.userId
			, selectedAlbumIds: options.selectedAlbumIds

			, groupSelectionClass: options.groupSelectionClass
			, translationDTO: options.translationDTO
		} );

		var userAlbumCompositeView = new CompositeView.UserAlbumCompositeView( {
			model: userAlbumsModel
		});

		var userAlbumsEntryListView = new View.EntryListView( {
			model: userAlbumsModel
			, el: options.container
			, entryCompositeView: userAlbumCompositeView
			, onEdit: options.onEdit
			, onDelete: options.onDelete
		} );
		userAlbumsEntryListView.renderHeader();
		userAlbumsEntryListView.render();
	}

	return init;
} );
