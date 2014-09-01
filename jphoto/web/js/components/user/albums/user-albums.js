define( ["components/user/albums/user-albums-model"
		, "components/user/albums/user-albums-view"
		, "jquery"], function ( Model, View, $ ) {

	function init( options ) {

		var albumsModel = new Model.AlbumsModel( {
				userId: options.userId
				, selectedAlbumIds: options.selectedAlbumIds
				, groupSelectionClass: options.groupSelectionClass
				, translationDTO: options.translationDTO
			} );

		var albumListView = new View.AlbumListView( {
				model: albumsModel
				, el: options.container
				, onEdit: options.onEdit
				, onDelete: options.onDelete
		} );
		albumListView.renderHeader();
		albumListView.render();
	}

	return init;
} );